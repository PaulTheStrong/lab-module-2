package com.epam.esm.hateoas.processor;

import com.epam.esm.data.PageInfo;
import com.epam.esm.hateoas.model.OrderModel;
import org.springframework.hateoas.CollectionModel;

public interface UserOrderCollectionModelProcessor {

    CollectionModel<OrderModel> process(CollectionModel<OrderModel> collectionModel, PageInfo pageInfo, int userId);

}
