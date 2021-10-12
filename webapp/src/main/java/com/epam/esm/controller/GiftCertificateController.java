package com.epam.esm.controller;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.hateoas.assembler.GiftCertificateModelAssembler;
import com.epam.esm.hateoas.model.GiftCertificateModel;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private final GiftCertificateService giftCertificateService;
    private final GiftCertificateModelAssembler giftCertificateModelAssembler;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, GiftCertificateModelAssembler giftCertificateModelAssembler) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateModelAssembler = giftCertificateModelAssembler;
    }

    /**
     * @param id GiftCertificate object's id stored in database.
     * @return GiftCertificateDto object with tags associated with requested gift certificate.
     */
    @GetMapping(value = "/{id}")
    public GiftCertificateModel getById(@PathVariable int id) {
        GiftCertificateDto certificate = giftCertificateService.getById(id);
        return giftCertificateModelAssembler.toModel(certificate);
    }

    /**
     * @param nameOrDescription - a part of name or description of GiftCertificate
     * @param tags - tag name associated with certificate
     * @param sortColumns - column name by which sorting is performed
     * @param sortTypes - soring order - ascending or descending
     * @return Filtered list of GiftCertificateDto if any of parameters is presented.
     * Otherwise, all GiftCertificateDtos are returned.
     */
    @GetMapping
    public CollectionModel<GiftCertificateModel> getCertificates(
            @RequestParam Optional<String> nameOrDescription,
            @RequestParam Optional<Set<String>> tags,
            @RequestParam Optional<List<String>> sortColumns,
            @RequestParam Optional<List<String>> sortTypes,
            @RequestParam(defaultValue = "1") int page
    ) {
        List<GiftCertificateDto> certificates;
        if (!nameOrDescription.isPresent() && !tags.isPresent() && !sortColumns.isPresent() && !sortTypes.isPresent()) {
            certificates = giftCertificateService.getCertificates(page, DEFAULT_PAGE_SIZE);
        } else {
            List<String> emptyList = Collections.emptyList();
            certificates = giftCertificateService.getWithParameters(nameOrDescription, tags,
                    sortColumns.orElse(emptyList), sortTypes.orElse(emptyList), page, DEFAULT_PAGE_SIZE);
        }
        return giftCertificateModelAssembler.toCollectionModel(certificates);
    }

    /**
     * Saves certificate and associated with it tags in database.
     * @param giftCertificateDto - data to be saved in database.
     * @return Saved gift certificate with updated id, dates and tags
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateModel addCertificate(@Validated(SaveDto.class) @RequestBody GiftCertificateDto giftCertificateDto) {
        GiftCertificateDto certificate = giftCertificateService.addCertificate(giftCertificateDto);
        return giftCertificateModelAssembler.toModel(certificate);
    }

    /**
     * Updates certificate and associated with it tags in database.
     * @param giftCertificateDto - data to be updated in database.
     * @param id - id of GiftCertificate in database
     * @return Updated GiftCertificateDto.
     */
    @PatchMapping(value = "/{id}")
    public GiftCertificateModel updateCertificate(@Validated(PatchDto.class) @RequestBody GiftCertificateDto giftCertificateDto, @PathVariable int id) {
        GiftCertificateDto certificate = giftCertificateService.updateCertificate(giftCertificateDto, id);
        return giftCertificateModelAssembler.toModel(certificate);
    }

    /**
     * Deletes certificate with specified id from database.
     * @param id - GiftCertifcate's id in database to be deleted.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable int id) {
        giftCertificateService.deleteCertificate(id);
    }
}
