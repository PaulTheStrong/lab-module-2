package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.TagController;
import com.epam.esm.entities.Tag;
import com.epam.esm.hateoas.model.TagModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagModelAssembler extends RepresentationModelAssemblerSupport<Tag, TagModel> {

    public TagModelAssembler() {
        super(TagController.class, TagModel.class);
    }

    @Override
    public TagModel toModel(Tag entity) {
        TagModel model = createModelWithId(entity.getId(), entity);
        model.setId(entity.getId());
        model.setName(entity.getName());
        return model;
    }

    @Override
    public CollectionModel<TagModel> toCollectionModel(Iterable<? extends Tag> entities) {
        List<TagModel> tagModels = new ArrayList<>();
        for (Tag tag : entities) {
            TagModel tagModel = toModel(tag);
            tagModels.add(tagModel);
        }
        return CollectionModel.of(tagModels);
    }
}
