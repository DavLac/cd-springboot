package dev.courses.springdemo.revision;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.RevisionListener;

@RequiredArgsConstructor
public class UserAuditListener implements RevisionListener {

    private final AuditContextHolder auditContextHolder;

    @Override
    public void newRevision(Object revisionEntity) {
        RevEntity revEntity = (RevEntity) revisionEntity;
        revEntity.setAuthor(auditContextHolder.getContext().author());
    }
}
