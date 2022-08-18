package com.svydovets.bibirnate.session;

public interface Session {
    <T> T findById(Class<T> type, Object id);
}