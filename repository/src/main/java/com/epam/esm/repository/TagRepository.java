package com.epam.esm.repository;

import com.epam.esm.entities.Tag;

import java.util.List;

public interface TagRepository extends Repository<Tag> {

    List<String> getTagsByCertificateId(int id);

}
