package com.epam.esm.hateoas.processor;

import com.epam.esm.data.PageInfo;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

public interface CollectionModelProcessor<T extends RepresentationModel<T>> {

    CollectionModel<T> process(CollectionModel<T> collectionModel, PageInfo currentPage);

}
