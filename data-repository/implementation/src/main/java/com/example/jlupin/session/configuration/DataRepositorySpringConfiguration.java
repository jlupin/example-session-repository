package com.example.jlupin.session.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Piotr Heilman
 */

@Configuration
@ComponentScan("com.example.jlupin.session")
public class DataRepositorySpringConfiguration {
    @Bean(name = "jLupinRegularExpressionToRemotelyEnabled")
    public List getRemotelyBeanList() {
        List<String> list = new ArrayList<>();
        list.add("getDefaultDataService");
        // list.add("<REMOTE_SERVICE_NAME>");
        return list;
    }
}

