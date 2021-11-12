package com.epam.esm.controller;

import com.epam.esm.entities.Tag;
import com.epam.esm.hateoas.assembler.TagModelAssembler;
import com.epam.esm.hateoas.model.TagModel;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CrossEntityController {

    private final TagModelAssembler tagModelAssembler;
    private final UserService userService;

    @Autowired
    public CrossEntityController(TagModelAssembler tagModelAssembler, UserService userService) {
        this.tagModelAssembler = tagModelAssembler;
        this.userService = userService;
    }

    @GetMapping("/most_popular_tag")
    @PreAuthorize("permitAll()")
    public TagModel getMostUsedTagOfUserWithHighestCostOfAllOrders() {
        Tag tag = userService.getMostUsedTagOfUserWithHighestCostOfAllOrders();
        return tagModelAssembler.toModel(tag);
    }
}
