package com.epam.esm.controller;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final String CERTIFICATE_HAS_BEEN_ADDED = "Certificate has been added";
    private static final String CERTIFICATE_HAS_BEEN_UPDATED = "Certificate has been updated";
    private static final String CERTIFICATE_HAS_BEEN_DELETED = "Certificate has been deleted";
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
                                                    @RequestParam Optional<List<String>> sort) {
        if (!search.isPresent() && !tag.isPresent() && !sort.isPresent()) {
            return giftCertificateService.getAll();
        }
        return giftCertificateService.getWithParameters(search, tag, sort.orElse(Collections.emptyList()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addCertificate(@RequestBody GiftCertificateDto giftCertificateDto) {
        giftCertificateService.addCertificate(giftCertificateDto);
        return CERTIFICATE_HAS_BEEN_ADDED;
    }

    @PatchMapping(value = "/{id}")
    public String updateCertificate(@RequestBody GiftCertificateDto certificate, @PathVariable int id) {
        giftCertificateService.updateCertificate(certificate, id);
        return CERTIFICATE_HAS_BEEN_UPDATED;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteCertificate(@PathVariable int id) {
        giftCertificateService.deleteCertificate(id);
        return CERTIFICATE_HAS_BEEN_DELETED;
    }
}
