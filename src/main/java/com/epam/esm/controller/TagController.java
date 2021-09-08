package com.epam.esm.controller;

import com.epam.esm.entities.Tag;
import com.epam.esm.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {

    private static final String TAG_HAS_BEEN_DELETED = "Tag has been deleted";
    private static final String TAG_HAS_BEEN_ADDED = "Tag has been added";
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tag getById(@PathVariable int id) {
        return tagService.getById(id);
    }

    @GetMapping("/all")
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @PostMapping("/{name}")
    public ResponseEntity<String> addTag(@PathVariable String name) {
        tagService.save(name);
        return new ResponseEntity<>(TAG_HAS_BEEN_ADDED, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable int id) {
        tagService.delete(id);
        return new ResponseEntity<>(TAG_HAS_BEEN_DELETED, HttpStatus.OK);
    }
}
