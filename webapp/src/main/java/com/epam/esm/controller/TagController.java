package com.epam.esm.controller;

import com.epam.esm.data.PageInfo;
import com.epam.esm.entities.Tag;
import com.epam.esm.hateoas.assembler.TagModelAssembler;
import com.epam.esm.hateoas.model.TagModel;
import com.epam.esm.hateoas.processor.TagModelProcessor;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
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

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static com.epam.esm.exception.ExceptionCodes.PAGE_MUST_BE_POSITIVE;
import static com.epam.esm.exception.ExceptionCodes.PAGE_SIZE_MUST_BE_POSITIVE;

@RestController
@RequestMapping("/tags")
@Validated
public class TagController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String START_PAGE = "1";
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
    @PreAuthorize("permitAll()")
    public TagModel getById(@PathVariable int id) {
        Tag tag = tagService.getById(id);
        return tagModelAssembler.toModel(tag);
    }

    /**
     * @return List of all Tags stored in database.
     */
    @GetMapping
    @PreAuthorize("permitAll()")
    public CollectionModel<TagModel> getAll(
            @RequestParam(defaultValue = START_PAGE) @Min(value = 1, message = PAGE_MUST_BE_POSITIVE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Min(value = 1, message = PAGE_SIZE_MUST_BE_POSITIVE) int pageSize) {
        PageInfo pageInfo = tagService.tagPageInfo(page, pageSize);
        List<Tag> tags = tagService.getTags(page, pageSize);
        CollectionModel<TagModel> collectionModel = tagModelAssembler.toCollectionModel(tags);
        return tagModelProcessor.process(collectionModel, pageInfo);
    }

    /**
     * Saves {@link Tag} passed in request in database.
     * @param tag {@link Tag} object to be saved in database.
     * @return Saved tag object with updated id from database.
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTag(@PathVariable int id) {
        tagService.delete(id);
    }
}
