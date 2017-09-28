package controllers;

import org.junit.Test;

import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.test.WithServer;

import java.util.concurrent.CompletionStage;

import static org.junit.Assert.assertEquals;

public class ServerFunctionalTest extends WithServer {

    @Test
    public void get_userNotFound_badrequest() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/user/1";

        try (WSClient ws = play.test.WSTestClient.newClient(this.testServer.port())) {
            CompletionStage<WSResponse> stage = ws.url(url).get();
            WSResponse response = stage.toCompletableFuture().get();
            assertEquals(400, response.getStatus());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete_userNotFound_badrequest() throws Exception {
        String url = "http://localhost:" + this.testServer.port() + "/user/1";

        try (WSClient ws = play.test.WSTestClient.newClient(this.testServer.port())) {
            CompletionStage<WSResponse> stage = ws.url(url).delete();
            WSResponse response = stage.toCompletableFuture().toCompletableFuture().get();
            assertEquals(400, response.getStatus());
            assertEquals("User with id 1 not found",response.getBody());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
