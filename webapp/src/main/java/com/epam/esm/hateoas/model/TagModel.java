package com.epam.esm.hateoas.model;

import com.epam.esm.entities.Tag;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@Relation(collectionRelation = "tags", itemRelation = "tag")
public class TagModel extends RepresentationModel<TagModel> {
    private int id;
    private String name;
}
