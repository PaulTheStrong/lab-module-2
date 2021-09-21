package com.epam.esm.controller;

import com.epam.esm.entities.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private static final String TAG_HAS_BEEN_DELETED = "Tag has been deleted";
    private static final String TAG_HAS_BEEN_ADDED = "Tag has been added";

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/{id}")
    public Tag getById(@PathVariable int id) {
        return tagService.getById(id);
    }

    @GetMapping
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @PostMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public String addTag(@PathVariable String name) {
        tagService.save(name);
        return TAG_HAS_BEEN_ADDED;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteTag(@PathVariable int id) {
        tagService.delete(id);
        return TAG_HAS_BEEN_DELETED;
    }
}
