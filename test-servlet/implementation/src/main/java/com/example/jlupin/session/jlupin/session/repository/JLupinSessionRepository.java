package com.example.jlupin.session.jlupin.session.repository;

import com.example.jlupin.session.jlupin.session.repository.session.JLupinSerializableSession;
import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;

/**
 * @author Piotr Heilman
 */
public class JLupinSessionRepository implements SessionRepository<JLupinSerializableSession> {
    private final SessionRepository<ExpiringSession> sessionRepository;

    public JLupinSessionRepository(final SessionRepository<ExpiringSession> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public JLupinSerializableSession createSession() {
        final ExpiringSession expiringSession = sessionRepository.createSession();
        if (expiringSession == null) {
            return null;
        }
        return new JLupinSerializableSession(expiringSession);
    }

    @Override
    public void save(JLupinSerializableSession jLupinSerializableSession) {
        sessionRepository.save(jLupinSerializableSession.getExpiringSession());
    }

    @Override
    public JLupinSerializableSession getSession(String s) {
        final ExpiringSession expiringSession = sessionRepository.getSession(s);
        if (expiringSession == null) {
            return null;
        }
        return new JLupinSerializableSession(expiringSession);
    }

    @Override
    public void delete(String s) {
        sessionRepository.delete(s);
    }
}
