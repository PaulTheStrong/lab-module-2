package com.epam.esm.hateoas.processor;

import com.epam.esm.controller.TagController;
import com.epam.esm.data.PageInfo;
import com.epam.esm.hateoas.model.TagModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelProcessor implements RepresentationModelProcessor<TagModel>, CollectionModelProcessor<TagModel> {

    @Override
    public TagModel process(TagModel model) {
        return model;
    }

    @Override
    public CollectionModel<TagModel> process(CollectionModel<TagModel> collectionModel, PageInfo currentPage) {
        PageInfo first = currentPage.first();
        PageInfo last = currentPage.last();
        PageInfo next = currentPage.nextOrLast();
        PageInfo prev = currentPage.prevOrFirst();
        int pageSize = currentPage.getPageSize();
        Link linkFirst = linkTo(getTagMethod(first, pageSize)).withRel("first");
        Link linkLast = linkTo(getTagMethod(last, pageSize)).withRel("last");
        Link linkPrev = linkTo(getTagMethod(prev, pageSize)).withRel("prev");
        Link linkNext = linkTo(getTagMethod(next, pageSize)).withRel("next");
        Link linkSelf = linkTo(getTagMethod(currentPage, pageSize)).withSelfRel();
        return collectionModel.add(linkFirst, linkLast, linkSelf, linkPrev, linkNext);
    }

    private CollectionModel<TagModel> getTagMethod(PageInfo first, int pageSize) {
        return methodOn(TagController.class).getAll(first.getCurrentPage(), pageSize);
    }
}
