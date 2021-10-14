package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.UserController;
import com.epam.esm.entities.User;
import com.epam.esm.hateoas.model.UserModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {

    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User entity) {
        UserModel model = createModelWithId(entity.getId(), entity);
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setBalance(entity.getBalance());
        return model;
    }

    @Override
    public CollectionModel<UserModel> toCollectionModel(Iterable<? extends User> entities) {
        List<UserModel> userModels = new ArrayList<>();
        for (User user : entities) {
            UserModel userModel = toModel(user);
            userModels.add(userModel);
        }
        return CollectionModel.of(userModels);
    }
}
