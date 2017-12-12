package com.example.jlupin.session.configuration;

import com.example.jlupin.session.service.interfaces.GetDefaultDataService;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.impl.logger.impl.log4j.JLupinLoggerOverLog4jImpl;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.client.delegator.exception.JLupinDelegatorException;
import com.jlupin.interfaces.client.proxy.producer.JLupinProxyObjectProducer;
import com.jlupin.interfaces.common.enums.PortType;
import com.jlupin.interfaces.logger.JLupinLogger;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import javax.annotation.PreDestroy;

/**
 * @author Piotr Heilman
 */

@Configuration
@EnableSpringHttpSession
@ComponentScan("com.example.jlupin.session")
@ServletComponentScan("com.example.jlupin.session")
@ComponentScan("com.jlupin.external.session.repository.configuration")
public class TestServletSpringConfiguration {
    private static final int HOW_OFTEN_CHECKING_SERVER_IN_MILLIS = 5000;
    private static final int REPEATS_AMOUNT = 3;
    private static final int CHANGE_SERVER_INTERVAL_IN_MILLIS = 5000;

    @Bean
    public JLupinDelegator getJLupinDelegator() {
        final JLupinDelegator jLupinDelegator = JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(
                HOW_OFTEN_CHECKING_SERVER_IN_MILLIS,
                REPEATS_AMOUNT,
                CHANGE_SERVER_INTERVAL_IN_MILLIS,
                PortType.JLRMC
        );

        try {
            jLupinDelegator.before();
        } catch (JLupinDelegatorException e) {
           throw new IllegalStateException("can not execute delegator's before method caused by:", e);
        }
        return jLupinDelegator;
    }

    @PreDestroy
    public void destroy() {
        try {
            getJLupinDelegator().after();
        } catch (JLupinDelegatorException e) {
            throw new IllegalStateException("can not execute delegator's after method caused by:", e);
        }
        JLupinClientUtil.closeResources();
    }

    @Bean
    public JLupinLogger getJLupinLogger() {
           return JLupinLoggerOverLog4jImpl.getInstance();
    }

    @Bean(name = "dataRepositoryProxyObjectProducer")
    public JLupinProxyObjectProducer getDataRepositoryProxyObjectProducer() {
        return JLupinClientUtil.generateProxyObjectProducer("data-repository", getJLupinDelegator(), getJLupinLogger());
    }

    @Bean
    public GetDefaultDataService getGetDefaultDataService() {
        return getDataRepositoryProxyObjectProducer().produceObject(GetDefaultDataService.class);
    }

    //For working session listener
    //@Bean
    //public SessionEventHttpSessionListenerAdapter getHttpSessionListenerAdapter(@Autowired HttpSessionListener httpSessionListener) {
    //    List<HttpSessionListener> listeners = new ArrayList<>();
    //    listeners.add(httpSessionListener);
    //    return new SessionEventHttpSessionListenerAdapter(listeners);
    //}
}

