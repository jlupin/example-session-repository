package com.example.jlupin.session.configuration;

import com.example.jlupin.session.jlupin.session.repository.JLupinSessionRepository;
import com.example.jlupin.session.jlupin.session.repository.session.JLupinSerializableSession;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.client.delegator.exception.JLupinDelegatorException;
import com.jlupin.interfaces.client.proxy.producer.JLupinProxyObjectProducer;
import com.jlupin.interfaces.common.enums.PortType;
import com.jlupin.interfaces.container.system.JLupinSystemContainer;
import com.jlupin.interfaces.logger.JLupinLogger;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import javax.annotation.PostConstruct;

/**
 * @author Piotr Heilman
 */

@Configuration
@EnableSpringHttpSession
@ComponentScan("com.example.jlupin.session")
@ServletComponentScan("com.example.jlupin.session")
public class TestServletSpringConfiguration {
    private static final int HOW_OFTEN_CHECKING_SERVER_IN_MILLIS = 5000;
    private static final int REPEATS_AMOUNT = 3;
    private static final int CHANGE_SERVER_INTERVAL_IN_MILLIS = 5000;

    private final JLupinLogger jLupinLogger;
    private final JLupinDelegator jLupinDelegator;

    public TestServletSpringConfiguration() {
        final JLupinSystemContainer jLupinSystemContainer = JLupinSystemContainer.getInstance();

        jLupinLogger = jLupinSystemContainer.getJLupinLogger();
        jLupinDelegator = JLupinClientUtil.generateInnerMicroserviceLoadBalancerDelegator(
                HOW_OFTEN_CHECKING_SERVER_IN_MILLIS,
                REPEATS_AMOUNT,
                CHANGE_SERVER_INTERVAL_IN_MILLIS,
                PortType.JLRMC
        );
    }

    @PostConstruct
    public void startLoadBalancingDelegator() throws JLupinDelegatorException {
        jLupinDelegator.before();
    }

    // @Bean(name = "exampleService")
    // public ExampleService getExampleService() {
    //     JLupinProxyObjectProducer objectProducer =
    //             new JLupinRemoteProxyObjectSupportsExceptionProducerImpl("example-microservice", jLupinDelegator, jLupinLogger);
    //
    //     return objectProducer.produceObject(ExampleService.class);
    // }

    @Bean(name = "sessionRepositoryProxyObjectProducer")
    public JLupinProxyObjectProducer getSessionRepositoryProxyObjectProducer() {
        return JLupinClientUtil.generateProxyObjectProducer("session-repository", jLupinDelegator, jLupinLogger);
    }

    @Bean
    public SessionRepository getSessionRepository() {
        final SessionRepository<ExpiringSession> sessionRepository = getSessionRepositoryProxyObjectProducer().produceObject(SessionRepository.class);
        return new JLupinSessionRepository(sessionRepository);
    }
}
