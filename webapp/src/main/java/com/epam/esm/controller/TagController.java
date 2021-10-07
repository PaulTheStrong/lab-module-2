package com.epam.esm.controller;

import com.epam.esm.entities.Tag;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
@Validated
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * @param id - tag object's id in database
     * @return Tag object from database
     */
    @GetMapping(value = "/{id}")
    public Tag getById(@PathVariable int id) {
        return tagService.getById(id);
    }

    /**
     * @return List of all Tags stored in database.
     */
    @GetMapping
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    /**
     * Saves Tag passed in request in database.
     * @param tag Tag object to be saved in database.
     * @return Saved tag object with updated id from database.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Tag addTag(@Valid @RequestBody Tag tag) {
        return tagService.save(tag);
    }

    /**
     * Deletes tag from database
     * @param id - Tag object's id to be deleted
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable int id) {
        tagService.delete(id);
    }
}
