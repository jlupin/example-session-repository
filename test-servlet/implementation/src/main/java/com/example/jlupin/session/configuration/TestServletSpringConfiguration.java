package com.example.jlupin.session.configuration;

import com.example.jlupin.session.service.interfaces.GetDefaultDataService;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.common.enums.PortType;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

/**
 * @author Piotr Heilman
 */

@Configuration
@EnableSpringHttpSession
@ComponentScan({
        "com.example.jlupin.session",
        "com.jlupin.impl.microservice.partofjlupin.httpsessionrepository.configuration",
        "com.jlupin.servlet.monitor.configuration"
})
@ServletComponentScan("com.example.jlupin.session")
public class TestServletSpringConfiguration {
    @Bean
    public JLupinDelegator getJLupinDelegator() {
        return JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(PortType.JLRMC);
    }

    @Bean
    public GetDefaultDataService getGetDefaultDataService() {
        return JLupinClientUtil.generateRemote(getJLupinDelegator(), "data-repository", GetDefaultDataService.class);
    }
}

