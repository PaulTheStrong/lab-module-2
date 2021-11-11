package com.epam.esm.hateoas.model;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "tags", itemRelation = "tag")
public class TagModel extends RepresentationModel<TagModel> {
    private int id;
    private String name;
}
