package org.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

@RestController
public class UserController {

    @RequestMapping("/send/message")
    public String testGreeting() {
        Client client = ClientBuilder.newClient();
        //这里目标服务写死，线上的情况一般是通过配置或注册中心
        Response response = client.target("http://localhost:8080").path("myapp").path("messages").path("next").request().get();
        String entity = response.readEntity(String.class);
        return entity;
    }
}
