package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.*;
import repository.UserRepository;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;
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
        Supplier<Result> fun= () -> {
            JsonNode jsonNode = request().body().asJson();
            Form<User> userForm = formFactory.form(User.class).bind(jsonNode);

            if (userForm.hasErrors()) {
                ObjectNode result = Json.newObject();
                result.set("message", userForm.errorsAsJson());
                return badRequest(result);
            } else {
                userRepository.create(userForm.get());
                return ok("ok");
            }
        };
        return supplyAsync(fun, ec.current());
    }

    public CompletionStage<Result> get(int id){
        return userRepository.get(id).thenApplyAsync(user -> {
            if (user.isPresent()) {
                return ok(toJson(user.get()));
            } else {
                return badRequest("User with id " + id + " not found");
            }
        }, ec.current());
    }

    public CompletionStage<Result> list(){
        return userRepository.list().thenApplyAsync(
                userStream -> ok(toJson(userStream.collect(Collectors.toList())))
                , ec.current());
    }

}
