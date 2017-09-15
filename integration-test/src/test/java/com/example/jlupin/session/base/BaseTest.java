package com.example.jlupin.session.base;

import com.jlupin.common.communication.common.various.JLupinMainServerInZoneConfiguration;
import com.jlupin.impl.client.util.JLupinClientUtil;
import com.jlupin.impl.logger.impl.log4j.JLupinLoggerOverLog4jImpl;
import com.jlupin.impl.serialize.JLupinFSTSerializerImpl;
import com.jlupin.interfaces.client.delegator.JLupinDelegator;
import com.jlupin.interfaces.common.enums.PortType;
import com.jlupin.interfaces.logger.JLupinLogger;
import com.jlupin.interfaces.serialize.JLupinSerializer;
import org.junit.Before;

/**
 * @author Piotr Heilman
 */
public abstract class BaseTest {
    private static final int HOW_OFTEN_CHECKING_SERVER_IN_MILLIS = 5000;
    private static final int REPEATS_AMOUNT = 3;
    private static final int CHANGE_SERVER_INTERVAL_IN_MILLIS = 5000;

    private final JLupinLogger jLupinLogger;
    private final JLupinSerializer jLupinSerializer;
    private final JLupinDelegator jLupinDelegator;

    public BaseTest() {
        jLupinLogger = JLupinLoggerOverLog4jImpl.getInstance();
        jLupinSerializer = JLupinFSTSerializerImpl.getInstance();
        jLupinDelegator = JLupinClientUtil.generateOuterClientLoadBalancerDelegator(
                HOW_OFTEN_CHECKING_SERVER_IN_MILLIS,
                REPEATS_AMOUNT,
                CHANGE_SERVER_INTERVAL_IN_MILLIS,
                PortType.JLRMC,
                new JLupinMainServerInZoneConfiguration[]{
                        new JLupinMainServerInZoneConfiguration("NODE_1", "127.0.0.1", 9090, 9095, 9096, 9097)
                },
                jLupinLogger,
                jLupinSerializer
        );
    }

    @Before
    public void setUp() throws Exception {
        jLupinDelegator.before();
    }

    public JLupinLogger getJLupinLogger() {
        return jLupinLogger;
    }

    public JLupinSerializer getJLupinSerializer() {
        return jLupinSerializer;
    }

    public JLupinDelegator getJLupinDelegator() {
        return jLupinDelegator;
    }
}