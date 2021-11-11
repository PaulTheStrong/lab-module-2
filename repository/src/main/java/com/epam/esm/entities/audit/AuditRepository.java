package com.epam.esm.entities.audit;

import com.epam.esm.entities.Identifiable;

public interface AuditRepository<T extends Identifiable> {

    void auditEntity(T entity);

}
