package com.example.jlupin.session.listener;

import com.jlupin.interfaces.logger.JLupinLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
public class SessionListenerImpl implements HttpSessionListener {
    @Autowired
    private JLupinLogger jLupinLogger;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        jLupinLogger.info("[ZZZ] SESSION CREATED");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        jLupinLogger.info("[ZZZ] SESSION DESTROYED");
    }
}
