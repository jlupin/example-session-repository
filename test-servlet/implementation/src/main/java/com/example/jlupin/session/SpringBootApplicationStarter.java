package com.example.jlupin.session;

import com.example.jlupin.session.configuration.TestServletSpringConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Piotr Heilman
 */

@SpringBootApplication
public class SpringBootApplicationStarter {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(TestServletSpringConfiguration.class, args);
    }
}

