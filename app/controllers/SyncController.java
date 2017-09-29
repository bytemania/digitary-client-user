package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.typesafe.config.ConfigFactory;
import models.User;
import play.Logger;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import repository.SyncDetailRepository;
import repository.UserRepository;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SyncController extends Controller {

    private final HttpExecutionContext ec;
    private WSClient ws;
    private SyncDetailRepository syncDetailRepository;
    private UserRepository userRepository;
    private ObjectMapper mapper;

    @Inject
    public SyncController(WSClient ws, HttpExecutionContext ec, SyncDetailRepository syncDetailRepository, UserRepository userRepository) {
        this.ws = ws;
        this.ec = ec;
        this.syncDetailRepository = syncDetailRepository;
        this.mapper = new ObjectMapper();
        this.userRepository = userRepository;
    }

    public Result load() throws Exception
    {
        return syncData();
    }

    private Result syncData() throws Exception
    {
        int pageSize = ConfigFactory.load().getInt("sync.users.pageSize.default");
        long diffMinutes = getDiffMinuter();

        int pageCount = getPageCount(diffMinutes, pageSize);

        processData(diffMinutes, pageSize, pageCount);

        return ok("Start Synchronization");
    }

    private long getDiffMinuter()
    {
        Optional<Timestamp> timestampOption = syncDetailRepository.getLast();
        long diffMinutes;
        if (timestampOption.isPresent()) {
            diffMinutes = getDiffMinutes(timestampOption.get().getTime());
        } else {
            diffMinutes = ConfigFactory.load().getLong("sync.users.since.default");
        }

        syncDetailRepository.setStamp();

        return diffMinutes;
    }

    private int getPageCount(long diffMinutes, int pageSize) throws Exception
    {
        String endPoint = ConfigFactory.load().getString("sync.users.pages.endpoint");

        String endpointUri = MessageFormat.format(endPoint, pageSize, diffMinutes);
        WSResponse response = ws.url(endpointUri).get().toCompletableFuture().get();
        int pageCount = response.asJson().get("totalPages").intValue();
        return pageCount;
    }

    private void processData(long diffMinutes, int pageSize, int pageCount){
        String endPoint = ConfigFactory.load().getString("sync.users.service.endpoint");

        for(int page = 1; page <= pageCount; page++){
            String endpointUri = MessageFormat.format(endPoint, pageSize, page, diffMinutes);
            ws.url(endpointUri).get().thenApplyAsync(wsResponse -> {
                List<User> users = processUsers(wsResponse);
                userRepository.sync(users);
                return null;
            });
        }
    }

    private long getDiffMinutes(long millis)
    {
        Date now = new Date();
        long diff = now.getTime() - millis;
        long diffMinutes = diff / (60 * 1000) % 60;
        return diffMinutes;
    }

    private List<User> processUsers(WSResponse wsResponse){
        JsonNode response = wsResponse.asJson();
        JsonNode data = response.get("data");

        List<User> users = new ArrayList<>();

        for(JsonNode userNode : data){
            try {
                User user = mapper.treeToValue(userNode, User.class);
                users.add(user);
            } catch (JsonProcessingException e) {
                Logger.error("Error processing DataSync", e);
            }
        }

        return users;
    }

}
