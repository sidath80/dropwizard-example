package com.personal.dw.example.resource;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

import com.personal.dw.example.config.MessagesConfiguration;

@Path(value = "/hello")
public class HelloResource {

    private final MessagesConfiguration conf;

    @Inject
    public HelloResource(final MessagesConfiguration conf) {
        this.conf = conf;
    }

    @GET
    public String sayHello() {
        return conf.getHello();
    }

    @POST
    @Path("/{id}")
    public String parameterDemoMethod(String body,
                                      @PathParam("id") long id,
                                      @QueryParam("foo") String foo,
                                      @HeaderParam("X-Auth-Token") String token,
                                      @Context HttpServletRequest request) {
        String response;
        response = "id = " + id + "\n";
        response += "body = " + body + "\n";
        response += "foo = " + foo + "\n";
        response += "token = " + token + "\n";
        response += "ip = " + request.getRemoteAddr() + "\n";
        return response;
    }

}
