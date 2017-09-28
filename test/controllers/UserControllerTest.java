package controllers;

import models.Contact;
import models.User;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.BAD_GATEWAY;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.route;

public class UserControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void add() {

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri("/user")
                .bodyJson(Json.toJson(createUser(1)));

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }


    private User createUser(int id)
    {
        User user = new User();
        user.setId(id);
        user.setName("name" + id);

        Contact contact = new Contact();
        contact.setEmail("email@example.org" +id);
        contact.setAddress1("address1_" + id);
        contact.setAddress2("address2_" + id);
        contact.setCity("city" + id);
        contact.setPostalCode("postalCode" + id);
        contact.setCountry("country" + id);
        List<String> phones = Arrays.asList("000000" + id, "111111" + id, "222222" + id);
        contact.setPhones(phones);

        user.setContact(contact);

        return user;
    }
}
