package com.epam.esm.controller;

import com.epam.esm.entities.Tag;
import com.epam.esm.hateoas.assembler.TagModelAssembler;
import com.epam.esm.hateoas.model.TagModel;
import com.epam.esm.hateoas.processor.TagModelProcessor;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tags")
@Validated
public class TagController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private final TagService tagService;
    private final TagModelProcessor tagModelProcessor;
    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public TagController(TagService tagService, TagModelProcessor tagModelProcessor, TagModelAssembler tagModelAssembler) {
        this.tagService = tagService;
        this.tagModelProcessor = tagModelProcessor;
        this.tagModelAssembler = tagModelAssembler;
    }

    /**
     * @param id - tag object's id in database
     * @return Tag object from database
     */
    @GetMapping(value = "/{id}")
    public TagModel getById(@PathVariable int id) {
        Tag tag = tagService.getById(id);
        return tagModelAssembler.toModel(tag);
    }

    /**
     * @return List of all Tags stored in database.
     */
    @GetMapping
    public CollectionModel<TagModel> getAll(@RequestParam(defaultValue = "1") int page) {
        List<Tag> tags = tagService.getTags(page, DEFAULT_PAGE_SIZE);
        return tagModelAssembler.toCollectionModel(tags);
    }

    /**
     * Saves {@link Tag} passed in request in database.
     * @param tag {@link Tag} object to be saved in database.
     * @return Saved tag object with updated id from database.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public TagModel addTag(@Valid @RequestBody Tag tag) {
        Tag saved = tagService.save(tag);
        return tagModelAssembler.toModel(saved);
    }

    /**
     * Deletes {@link Tag} from database
     * @param id - Tag object's id to be deleted
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable int id) {
        tagService.delete(id);
    }
}
