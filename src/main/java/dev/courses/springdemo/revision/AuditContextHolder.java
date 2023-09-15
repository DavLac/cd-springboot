package dev.courses.springdemo.revision;

import org.springframework.stereotype.Component;

@Component
public class AuditContextHolder {

    @SuppressWarnings("java:S5164")
    private final ThreadLocal<UserAuditContext> threadLocal = new ThreadLocal<>();

    public AuditContextCloseable setAuditContext(UserAuditContext auditContext) {
        threadLocal.set(auditContext);
        return threadLocal::remove;
    }

    public UserAuditContext getContext() {
        return threadLocal.get();
    }

    @FunctionalInterface
    public interface AuditContextCloseable extends AutoCloseable {
        @Override
        void close();
    }

}