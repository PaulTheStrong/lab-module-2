package com.epam.esm.repository.api;

public interface Countable {

    default int countAll() {
        throw new UnsupportedOperationException();
    }

}
