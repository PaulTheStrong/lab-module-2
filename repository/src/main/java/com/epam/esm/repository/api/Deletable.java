package com.epam.esm.repository.api;

import com.epam.esm.entities.Identifiable;

public interface Deletable<T extends Identifiable> {

    boolean delete(int id);

}
