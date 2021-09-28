package com.epam.esm.controller;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.PatchDto;
import com.epam.esm.validator.SaveDto;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    /**
     * @param id GiftCertificate object's id stored in database.
     * @return GiftCertificateDto object with tags associated with requested gift certificate.
     */
    @GetMapping(value = "/{id}")
    public GiftCertificateDto getById(@PathVariable int id) {
        return giftCertificateService.getById(id);
    }

    /**
     * @param nameOrDescription - a part of name or description of GiftCertificate
     * @param tag - tag name associated with certificate
     * @param sortColumns - column name by which sorting is performed
     * @param sortTypes - soring order - ascending or descending
     * @return Filtered list of GiftCertificateDto if any of parameters is presented.
     * Otherwise, all GiftCertificateDtos are returned.
     */
    @GetMapping
    public List<GiftCertificateDto> getCertificates(
            @RequestParam Optional<String> nameOrDescription,
            @RequestParam Optional<String> tag,
            @RequestParam Optional<List<String>> sortColumns,
            @RequestParam Optional<List<String>> sortTypes
    ) {
        if (!nameOrDescription.isPresent() && !tag.isPresent() && !sortColumns.isPresent() && !sortTypes.isPresent()) {
            return giftCertificateService.getAll();
        }
        return giftCertificateService.getWithParameters(nameOrDescription, tag, sortColumns.orElse(Collections.emptyList()), sortTypes.orElse(Collections.emptyList()));
    }

    /**
     * Saves certificate and associated with it tags in database.
     * @param giftCertificateDto - data to be saved in database.
     * @return Saved gift certificate with updated id, dates and tags
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto addCertificate(@Validated(SaveDto.class) @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.addCertificate(giftCertificateDto);
    }

    /**
     * Updates certificate and associated with it tags in database.
     * @param giftCertificateDto - data to be updated in database.
     * @param id - id of GiftCertificate in database
     * @return Updated GiftCertificateDto.
     */
    @PatchMapping(value = "/{id}")
    public GiftCertificateDto updateCertificate(@Validated(PatchDto.class) @RequestBody GiftCertificateDto giftCertificateDto, @PathVariable int id) {
        return giftCertificateService.updateCertificate(giftCertificateDto, id);
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
