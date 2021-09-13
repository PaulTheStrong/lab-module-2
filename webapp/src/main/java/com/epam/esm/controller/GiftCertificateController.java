package com.epam.esm.controller;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private static final String CERTIFICATE_HAS_BEEN_ADDED = "Certificate has been added";
    private static final String CERTIFICATE_HAS_BEEN_UPDATED = "Certificate has been updated";
    private static final String CERTIFICATE_HAS_BEEN_DELETED = "Certificate has been deleted";
    private final CertificateService certificateService;

    @Autowired
    public GiftCertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping(value = "/{id}")
    public GiftCertificateDto getById(@PathVariable int id) {
        return certificateService.getById(id);
    }

    @GetMapping
    public List<GiftCertificateDto> getAll() {
        return certificateService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addCertificate(@RequestBody GiftCertificate giftCertificate) {
        certificateService.addCertificate(giftCertificate);
        return CERTIFICATE_HAS_BEEN_ADDED;
    }

    @PatchMapping(value = "/{id}")
    public String updateCertificate(@RequestBody GiftCertificate certificate, @PathVariable int id) {
        certificateService.updateCertificate(certificate, id);
        return CERTIFICATE_HAS_BEEN_UPDATED;
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String deleteCertificate(@PathVariable int id) {
        certificateService.deleteCertificate(id);
        return CERTIFICATE_HAS_BEEN_DELETED;
    }
}
