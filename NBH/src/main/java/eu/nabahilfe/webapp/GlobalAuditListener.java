package eu.nabahilfe.webapp;

import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.security.SecurityUtils;


@Component
public class GlobalAuditListener {

    @Autowired
    private SecurityUtils securityUtils;


    @PrePersist
    public void prePersist(Object entity) {
        handleAudit(entity, true);
    }


    @PreUpdate
    public void preUpdate(Object entity) {
        handleAudit(entity, false);
    }


    private void handleAudit(Object entity, boolean isNew) {

        Member user = securityUtils.getCurrentUser();

        if (user == null) return;

        try {
            if (isNew) setIfExists(entity, "createdBy", user);
            setIfExists(entity, "updatedBy", user);

        } catch (Exception e) {
            throw new RuntimeException("Audit error", e);
        }
    }

    private void setIfExists(Object entity, String fieldName, Object value) throws Exception {

        Field field = findField(entity.getClass(), fieldName);

        if (field == null) return;
        field.setAccessible(true);

        Object current = field.get(entity);
        if (current == null) field.set(entity, value);

    }

    private Field findField(Class<?> type, String name) {

        while (type != null) {
            try {
                return type.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            }
        }

        return null;
    }

}
