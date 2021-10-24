package com.epam.esm.controller;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.data.PageInfo;
import com.epam.esm.entities.Tag;
import com.epam.esm.hateoas.assembler.GiftCertificateModelAssembler;
import com.epam.esm.hateoas.model.GiftCertificateModel;
import com.epam.esm.hateoas.processor.GiftCertificateModelProcessor;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.PatchDto;
import com.epam.esm.validator.SaveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.exception.ExceptionCodes.PAGE_MUST_BE_POSITIVE;
import static com.epam.esm.exception.ExceptionCodes.PAGE_SIZE_MUST_BE_POSITIVE;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final String START_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "10";
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateModelAssembler giftCertificateModelAssembler;
    private final GiftCertificateModelProcessor giftCertificateModelProcessor;

//    @Autowired
//    private DatabaseFillApplicationRunner runner;
//    private boolean isFilled = false;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateModelAssembler giftCertificateModelAssembler, GiftCertificateModelProcessor giftCertificateModelProcessor) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateModelAssembler = giftCertificateModelAssembler;
        this.giftCertificateModelProcessor = giftCertificateModelProcessor;
    }

    /**
     * @param id {@link com.epam.esm.entities.GiftCertificate} object's id stored in database.
     * @return {@link GiftCertificateDto} object with tags associated with requested gift certificate.
     */
    @GetMapping(value = "/{id}")
    public GiftCertificateModel getById(@PathVariable int id) {
        GiftCertificateDto certificate = giftCertificateService.getById(id);
        return giftCertificateModelAssembler.toModel(certificate);
    }

    /**
     * @param nameOrDescription - a part of name or description of {@link com.epam.esm.entities.GiftCertificate}
     * @param tags - {@link Tag} name associated with certificate
     * @param sortColumns - column name by which sorting is performed {@link com.epam.esm.repository.impl.SortColumn}
     * @param sortTypes - soring order - ascending or descending {@link com.epam.esm.repository.impl.SortType}
     * @return Filtered {@link List} of {@link GiftCertificateDto} if any of the parameters is presented.
     * Otherwise, all {@link GiftCertificateDto}s are returned.
     */
    @GetMapping
    public CollectionModel<GiftCertificateModel> getCertificates(
            @RequestParam Optional<String> nameOrDescription,
            @RequestParam Optional<Set<String>> tags,
            @RequestParam Optional<List<String>> sortColumns,
            @RequestParam Optional<List<String>> sortTypes,
            @RequestParam(defaultValue = START_PAGE) @Min(value = 1, message = PAGE_MUST_BE_POSITIVE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Min(value = 1, message = PAGE_SIZE_MUST_BE_POSITIVE) int pageSize
    ) {
        List<GiftCertificateDto> certificates;
        PageInfo pageInfo;
        List<String> emptyList = Collections.emptyList();
        if (!nameOrDescription.isPresent() && !tags.isPresent() && !sortColumns.isPresent() && !sortTypes.isPresent()) {
            certificates = giftCertificateService.getCertificates(page, pageSize);
            pageInfo = giftCertificateService.giftCertificatePageInfo(page, pageSize);
        } else {
            certificates = giftCertificateService.getWithParameters(
                    nameOrDescription, tags, sortColumns.orElse(emptyList),
                    sortTypes.orElse(emptyList), page, pageSize);
            pageInfo = giftCertificateService.giftCertificatePageInfoWithParameters(
                    nameOrDescription, tags, sortColumns.orElse(emptyList),
                    sortTypes.orElse(emptyList), page, pageSize
            );
        }
        CollectionModel<GiftCertificateModel> collectionModel = giftCertificateModelAssembler.toCollectionModel(certificates);
        return giftCertificateModelProcessor.process(
                nameOrDescription, tags, sortColumns, sortTypes, collectionModel, pageInfo
        );
    }

    /**
     * Saves {@link com.epam.esm.entities.GiftCertificate} and associated with
     * it {@link Tag}s in database.
     * @param giftCertificateDto {@link GiftCertificateDto} to be saved in database.
     * @return Saved {@link GiftCertificateDto} with updated id, dates and tags
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateModel addCertificate(@Validated(SaveDto.class) @RequestBody GiftCertificateDto giftCertificateDto) {
//        if (!isFilled) {
//            runner.run();
//            isFilled = true;
//        }
        GiftCertificateDto certificate = giftCertificateService.addCertificate(giftCertificateDto);
        return giftCertificateModelAssembler.toModel(certificate);
    }

    /**
     * Updates {@link com.epam.esm.entities.GiftCertificate} and associated with it tags in database.
     * @param giftCertificateDto {@link GiftCertificateDto} to be updated in database.
     * @param id id of {@link com.epam.esm.entities.GiftCertificate} in database
     * @return Updated {@link GiftCertificateDto}.
     */
    @PatchMapping(value = "/{id}")
    public GiftCertificateModel updateCertificate(@Validated(PatchDto.class) @RequestBody GiftCertificateDto giftCertificateDto, @PathVariable int id) {
        GiftCertificateDto certificate = giftCertificateService.updateCertificate(giftCertificateDto, id);
        return giftCertificateModelAssembler.toModel(certificate);
    }

    /**
     * Deletes {@link com.epam.esm.entities.GiftCertificate} with specified id from database.
     * @param id {@link com.epam.esm.entities.GiftCertificate}'s id in database to be deleted.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable int id) {
        giftCertificateService.deleteCertificate(id);
    }
}
