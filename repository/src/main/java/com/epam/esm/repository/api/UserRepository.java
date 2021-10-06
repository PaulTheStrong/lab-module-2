package com.epam.esm.repository.api;

import com.epam.esm.entities.Tag;
import com.epam.esm.entities.User;

public interface UserRepository extends Findable<User> {

    //Get the most widely used tag of a user with the highest cost of all orders.
    Tag getMostUsedTagOfUserWithHighestCostOfAllOrders();
}
