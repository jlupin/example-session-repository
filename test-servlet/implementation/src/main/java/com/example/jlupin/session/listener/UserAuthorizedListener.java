package com.example.jlupin.session.listener;

import com.example.jlupin.session.exception.CoreSystemUnavailableException;
import com.example.jlupin.session.service.interfaces.GetDefaultDataService;
import com.jlupin.impl.util.JLupinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author Piotr Heilman
 */
@Component
public class UserAuthorizedListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    private GetDefaultDataService getDefaultDataService;
    @Autowired
    private HttpSession httpSession;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        final Map<String, String> defaultSessionParameters;
        try {
            defaultSessionParameters = getDefaultDataService.getDefaultSessionParamters();
        } catch (Throwable th) {
            httpSession.invalidate();
            throw new CoreSystemUnavailableException(JLupinUtil.getHighestMessageFromThrowable(th), th);
        }

        for (Map.Entry<String, String> entry : defaultSessionParameters.entrySet()) {
            httpSession.setAttribute(entry.getKey(), entry.getValue());
        }
    }
}
