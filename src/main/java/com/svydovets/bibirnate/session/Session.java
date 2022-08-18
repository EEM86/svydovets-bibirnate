package com.svydovets.bibirnate.session;

public interface Session {
    <T> T findById(Object id, Class<T> type);
}
