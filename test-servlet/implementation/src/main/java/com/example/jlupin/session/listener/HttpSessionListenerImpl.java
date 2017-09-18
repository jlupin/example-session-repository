package com.example.jlupin.session.listener;

import com.example.jlupin.session.service.interfaces.GetDefaultDataService;
import com.jlupin.impl.util.JLupinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

/**
 * @author Piotr Heilman
 */
@Component
public class HttpSessionListenerImpl implements HttpSessionListener {
    @Autowired
    private GetDefaultDataService getDefaultDataService;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        final Map<String, String> defaultSessionParamters;
        try {
            defaultSessionParamters = getDefaultDataService.getDefaultSessionParamters();
        } catch (Throwable th) {
            throw new IllegalStateException(JLupinUtil.getHighestMessageFromThrowable(th), th);
        }

        final HttpSession session = httpSessionEvent.getSession();
        for (Map.Entry<String, String> entry : defaultSessionParamters.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
