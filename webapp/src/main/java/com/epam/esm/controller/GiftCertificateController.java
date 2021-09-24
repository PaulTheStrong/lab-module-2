package com.epam.esm.controller;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.validator.PatchDto;
import com.epam.esm.validator.SaveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping(value = "/{id}")
    public GiftCertificateDto getById(@PathVariable int id) {
        return giftCertificateService.getById(id);
    }

    @GetMapping
    public List<GiftCertificateDto> getCertificates(@RequestParam Optional<String> search,
                                                    @RequestParam Optional<String> tag,
                                                    @RequestParam Optional<List<String>> sortColumns,
                                                    @RequestParam Optional<List<String>> sortTypes) {
        if (!search.isPresent() && !tag.isPresent() && !sortColumns.isPresent() && !sortTypes.isPresent()) {
            return giftCertificateService.getAll();
        }
        return giftCertificateService.getWithParameters(search, tag, sortColumns.orElse(Collections.emptyList()), sortTypes.orElse(Collections.emptyList()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public GiftCertificateDto addCertificate(@Validated(SaveDto.class) @RequestBody GiftCertificateDto giftCertificateDto) {
        return giftCertificateService.addCertificate(giftCertificateDto);
    }

    @PatchMapping(value = "/{id}")
    public GiftCertificateDto updateCertificate(@Validated(PatchDto.class) @RequestBody GiftCertificateDto certificate, @PathVariable int id) {
        return giftCertificateService.updateCertificate(certificate, id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable int id) {
        giftCertificateService.deleteCertificate(id);
    }
}
