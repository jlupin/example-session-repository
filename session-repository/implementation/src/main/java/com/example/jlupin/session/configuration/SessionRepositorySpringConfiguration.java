package com.example.jlupin.session.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr Heilman
 */

@Configuration
@ComponentScan("com.example.jlupin.session")
public class SessionRepositorySpringConfiguration {
    @Bean(name = "sessionRepository")
    public MapSessionRepository sessionRepository() {
        return new MapSessionRepository();
    }

    @Bean(name = "jLupinRegularExpressionToRemotelyEnabled")
    public List getRemotelyBeanList() {
        List<String> list = new ArrayList<>();
        list.add("sessionRepository");
        return list;
    }
}

