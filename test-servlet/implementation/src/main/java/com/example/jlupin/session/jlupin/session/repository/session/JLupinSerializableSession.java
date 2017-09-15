package com.example.jlupin.session.jlupin.session.repository.session;

import org.nustaq.serialization.FSTConfiguration;
import org.springframework.session.ExpiringSession;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Piotr Heilman
 */
public class JLupinSerializableSession implements ExpiringSession, Serializable {
    private static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    private final ExpiringSession expiringSession;

    public JLupinSerializableSession(final ExpiringSession expiringSession) {
        this.expiringSession = expiringSession;
    }

    public ExpiringSession getExpiringSession() {
        return expiringSession;
    }

    @Override
    public String getId() {
        return expiringSession.getId();
    }

    @Override
    public <T> T getAttribute(String s) {
        final Object attribute = expiringSession.getAttribute(s);
        if (attribute == null) {
            return null;
        }
        final byte[] attributeAsByteArray = (byte[]) attribute;
        return (T) conf.asObject(attributeAsByteArray);
    }

    @Override
    public Set<String> getAttributeNames() {
        return expiringSession.getAttributeNames();
    }

    @Override
    public void setAttribute(String s, Object o) {
        if (o == null) {
            expiringSession.setAttribute(s, null);
            return;
        }

        final byte[] byteArray = conf.asByteArray(o);
        expiringSession.setAttribute(s, byteArray);
    }

    @Override
    public void removeAttribute(String s) {
        expiringSession.removeAttribute(s);
    }

    @Override
    public long getCreationTime() {
        return expiringSession.getCreationTime();
    }

    @Override
    public void setLastAccessedTime(long l) {
        expiringSession.setLastAccessedTime(l);
    }

    @Override
    public long getLastAccessedTime() {
        return expiringSession.getLastAccessedTime();
    }

    @Override
    public void setMaxInactiveIntervalInSeconds(int i) {
        expiringSession.setMaxInactiveIntervalInSeconds(i);
    }

    @Override
    public int getMaxInactiveIntervalInSeconds() {
        return expiringSession.getMaxInactiveIntervalInSeconds();
    }

    @Override
    public boolean isExpired() {
        return expiringSession.isExpired();
    }
}
