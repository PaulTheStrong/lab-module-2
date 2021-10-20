package com.epam.esm.hateoas.processor;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.data.PageInfo;
import com.epam.esm.hateoas.model.GiftCertificateModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateModelProcessor implements RepresentationModelProcessor<GiftCertificateModel> {

    public CollectionModel<GiftCertificateModel> process(
            Optional<String> nameOrDescription,
            Optional<Set<String>> tags,
            Optional<List<String>> sortColumns,
            Optional<List<String>> sortTypes,
            CollectionModel<GiftCertificateModel> collectionModel,
            PageInfo currentPage
    ) {
        PageInfo first = currentPage.getFirst();
        PageInfo last = currentPage.getLast();
        PageInfo next = currentPage.getNextOrLast();
        PageInfo prev = currentPage.getPreviousOrFirst();
        Link linkFirst = linkTo(getCertificates(nameOrDescription, tags, sortColumns, sortTypes, first)).withRel("first");
        Link linkLast = linkTo(getCertificates(nameOrDescription, tags, sortColumns, sortTypes, last)).withRel("last");
        Link linkPrev = linkTo(getCertificates(nameOrDescription, tags, sortColumns, sortTypes, prev)).withRel("prev");
        Link linkNext = linkTo(getCertificates(nameOrDescription, tags, sortColumns, sortTypes, next)).withRel("next");
        Link linkSelf = linkTo(getCertificates(nameOrDescription, tags, sortColumns, sortTypes, currentPage)).withSelfRel();
        return collectionModel.add(linkFirst, linkLast, linkSelf, linkPrev, linkNext);
    }

    private CollectionModel<GiftCertificateModel> getCertificates(
            Optional<String> nameOrDescription,
            Optional<Set<String>> tags,
            Optional<List<String>> sortColumns,
            Optional<List<String>> sortTypes,
            PageInfo pageInfo
    ) {
        return methodOn(GiftCertificateController.class).getCertificates(nameOrDescription, tags, sortColumns, sortTypes, pageInfo.getCurrentPage(), pageInfo.getPageSize());
    }

    @Override
    public GiftCertificateModel process(GiftCertificateModel model) {
        return model;
    }
}
