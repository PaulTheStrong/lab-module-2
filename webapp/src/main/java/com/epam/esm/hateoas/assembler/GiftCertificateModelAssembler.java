package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.hateoas.model.GiftCertificateModel;
import com.epam.esm.hateoas.model.TagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelAssembler extends RepresentationModelAssemblerSupport<GiftCertificateDto, GiftCertificateModel> {

    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public GiftCertificateModelAssembler(TagModelAssembler tagModelAssembler) {
        super(GiftCertificateController.class, GiftCertificateModel.class);
        this.tagModelAssembler = tagModelAssembler;
    }

    @Override
    public GiftCertificateModel toModel(GiftCertificateDto entity) {
        GiftCertificateModel model = createModelWithId(entity.getId(), entity);

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setPrice(entity.getPrice());
        model.setDuration(entity.getDuration());
        model.setCreateDate(entity.getCreateDate());
        model.setLastUpdateDate(entity.getLastUpdateDate());

        List<Tag> tags = entity.getTags();
        CollectionModel<TagModel> tagCollectionModel = tagModelAssembler.toCollectionModel(tags);
        List<TagModel> tagModels = new ArrayList<>(tagCollectionModel.getContent());
        model.setTags(tagModels);
        return model;
    }

    @Override
    public CollectionModel<GiftCertificateModel> toCollectionModel(Iterable<? extends GiftCertificateDto> entities) {
        List<GiftCertificateModel> giftCertificateModels = new ArrayList<>();
        for (GiftCertificateDto certificate : entities) {
            GiftCertificateModel certificateModel = toModel(certificate);
            giftCertificateModels.add(certificateModel);
        }
        Link selfLink = linkTo(methodOn(GiftCertificateController.class)
                .getCertificates(
                        Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.empty(),
                        1))
                .withSelfRel();
        return CollectionModel.of(giftCertificateModels, selfLink);
    }
}
