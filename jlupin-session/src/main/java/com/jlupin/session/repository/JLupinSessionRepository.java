package com.jlupin.session.repository;

import com.jlupin.impl.util.JLupinUtil;
import com.jlupin.session.JLupinSerializableSession;
import com.jlupin.session.error.SessionRepositoryUnavailableException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.session.events.SessionCreatedEvent;

/**
 * @author Piotr Heilman
 */
public class JLupinSessionRepository implements SessionRepository<JLupinSerializableSession> {
    private final SessionRepository<ExpiringSession> sessionRepository;
    private ApplicationEventPublisher eventPublisher;

    public JLupinSessionRepository(final SessionRepository<ExpiringSession> sessionRepository, final ApplicationEventPublisher eventPublisher) {
        this.sessionRepository = sessionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public JLupinSerializableSession createSession() {
        final ExpiringSession expiringSession;
        try {
            expiringSession = sessionRepository.createSession();
        } catch (Throwable th) {
            throw new SessionRepositoryUnavailableException(JLupinUtil.getHighestMessageFromThrowable(th), th);
        }

        if (expiringSession == null) {
            return null;
        }
        final JLupinSerializableSession createdSession = new JLupinSerializableSession(expiringSession);

        this.eventPublisher.publishEvent(new SessionCreatedEvent(this, createdSession));

        return createdSession;
    }

    @Override
    public void save(JLupinSerializableSession jLupinSerializableSession) {
        try {
            sessionRepository.save(jLupinSerializableSession.getExpiringSession());
        } catch (Throwable th) {
            throw new SessionRepositoryUnavailableException(JLupinUtil.getHighestMessageFromThrowable(th), th);
        }
    }

    @Override
    public JLupinSerializableSession getSession(String s) {
        final ExpiringSession expiringSession;
        try {
            expiringSession = sessionRepository.getSession(s);
        } catch (Throwable th) {
            throw new SessionRepositoryUnavailableException(JLupinUtil.getHighestMessageFromThrowable(th), th);
        }

        if (expiringSession == null) {
            return null;
        }

        return new JLupinSerializableSession(expiringSession);
    }

    @Override
    public void delete(String s) {
        try {
            sessionRepository.delete(s);
        } catch (Throwable th) {
            throw new SessionRepositoryUnavailableException(JLupinUtil.getHighestMessageFromThrowable(th), th);
        }
    }
}
