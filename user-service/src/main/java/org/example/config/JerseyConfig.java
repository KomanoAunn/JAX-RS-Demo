package org.example.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
//Jersey servlet将被注册，并默认映射到/*。可将@ApplicationPath添加到ResourceConfig来改变该映射。
@ApplicationPath("/myapp")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
      packages("org.example.resource"); // 通过packages注册。
    }
}
