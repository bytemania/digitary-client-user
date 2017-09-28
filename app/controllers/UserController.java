package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import repository.UserRepository;

import java.text.MessageFormat;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.libs.Json.toJson;

public class UserController extends Controller {

    private UserRepository userRepository;
    private final HttpExecutionContext ec;
    private final FormFactory formFactory;

    @Inject
    public UserController(UserRepository userRepository, HttpExecutionContext ec, FormFactory formFactory) {
        this.userRepository = userRepository;
        this.ec = ec;
        this.formFactory = formFactory;
    }

    public CompletionStage<Result> add() {
        JsonNode jsonNode = request().body().asJson();
        Form<User> userForm = formFactory.form(User.class).bind(jsonNode);
        Messages messages = Http.Context.current().messages();

        if (userForm.hasErrors()) {
            return supplyAsync(() -> {
                ObjectNode result = Json.newObject();
                result.set("message", userForm.errorsAsJson());
                return badRequest(result);
            });
        } else {
            return userRepository.create(userForm.get())
                    .thenApplyAsync(user -> user.isPresent() ?
                            ok(sucessfullyMessage(messages, user.get().getId(), "inserted")):
                            badRequest(userAlreadyExistsMessage(messages, userForm.get().getId())));
        }
    }

    public CompletionStage<Result> update() throws Exception {
        JsonNode jsonNode = request().body().asJson();
        Form<User> userForm = formFactory.form(User.class).bind(jsonNode);
        Messages messages = Http.Context.current().messages();
        if (userForm.hasErrors()) {
            return supplyAsync(() -> {
                ObjectNode result = Json.newObject();
                result.set("message", userForm.errorsAsJson());
                return badRequest(result);
            });
        } else {
            return userRepository.update(userForm.get())
                    .thenApply(user -> user.isPresent() ? ok(sucessfullyMessage(messages, user.get().getId(), "updated")) :
                            notFound(userNotFoundMessage(messages, userForm.get().getId())));
        }
    }

    public CompletionStage<Result> get(Integer id){
        Messages messages = Http.Context.current().messages();
        return userRepository.get(id)
                .thenApplyAsync(user ->
                                user.isPresent() ? ok(toJson(user.get())) : badRequest(userNotFoundMessage(messages, id))
                , ec.current());
    }

    public CompletionStage<Result> list(){
        return userRepository.list().thenApplyAsync(
                userStream -> ok(toJson(userStream.collect(Collectors.toList())))
                , ec.current());
    }

    public CompletionStage<Result> delete(Integer id) {
        Messages messages = Http.Context.current().messages();
        return userRepository.delete(id)
                .thenApplyAsync(user -> user.isPresent() ? ok(sucessfullyMessage(messages, id, "deleted")) :
                                badRequest(userNotFoundMessage(messages, id))
                        , ec.current());
    }

    private String userAlreadyExistsMessage(Messages messages, Integer id)
    {
        String message = messages.at("user.alreadyExists");
        return MessageFormat.format(message, id);
    }

    private String userNotFoundMessage(Messages messages, Integer id)
    {
        String message = messages.at("user.notFound");
        return MessageFormat.format(message, id);
    }

    private String sucessfullyMessage(Messages messages, Integer id, String operation)
    {
        String message = messages.at("user.ok");
        return MessageFormat.format(message, id ,operation);
    }

}
