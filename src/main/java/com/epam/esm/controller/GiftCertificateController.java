package com.epam.esm.controller;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto getById(@PathVariable int id) {
        return certificateService.getById(id);
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GiftCertificateDto> getAll() {
        return certificateService.getAll();
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCertificate(@RequestBody GiftCertificate giftCertificate) {
        certificateService.addCertificate(giftCertificate);
        return new ResponseEntity<>(CERTIFICATE_HAS_BEEN_ADDED, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCertificate(@RequestBody GiftCertificate certificate, @PathVariable int id) {
        certificateService.updateCertificate(certificate, id);
        return new ResponseEntity<>(CERTIFICATE_HAS_BEEN_UPDATED, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteCertificate(@PathVariable int id) {
        certificateService.deleteCertificate(id);
        return new ResponseEntity<>(CERTIFICATE_HAS_BEEN_DELETED, HttpStatus.OK);
    }
}
