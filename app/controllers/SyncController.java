package controllers;

import com.google.inject.Inject;
import com.typesafe.config.ConfigFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;


import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;


public class SyncController extends Controller {

    private final HttpExecutionContext ec;
    private WSClient ws;

    @Inject
    public SyncController(WSClient ws, HttpExecutionContext ec) {
        this.ws = ws;
        this.ec = ec;
    }

    public CompletionStage<Result> load()
    {
        String endPoint = ConfigFactory.load().getString("sync.data.service.endpoint");

        return ws.url(endPoint).get()
                .thenApplyAsync(wsResponse -> ok(wsResponse.asJson()), ec.current());
    }



}
