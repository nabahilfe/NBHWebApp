/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp;

import java.lang.reflect.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import eu.nabahilfe.webapp.members.Member;
import eu.nabahilfe.webapp.security.SecurityUtils;

/**
 * JPA Entity Listener to automatically set createdBy and updatedBy fields based on the currently authenticated user.
 * It uses reflection to check if the entity has these fields and sets them if they are null (for createdBy) or always (for updatedBy).
 * This allows us to have audit information on who created or last updated an entity without having to manually set it in each service method.
 * 
 * To actually activate this listener on an entity, the entity class needs to be annotated with: @EntityListeners(GlobalAuditListener.class)
 */
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
            e.printStackTrace();
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
                e.printStackTrace();
                type = type.getSuperclass();
            }
        }

        return null;
    }

}
