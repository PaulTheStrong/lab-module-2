package com.epam.esm.controller;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("certificate")
public class GiftCertificateController {

    private CertificateService certificateService;

    @Autowired
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificate getById(@PathVariable int id) {
        return certificateService.getById(id);
    }

    @GetMapping(value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GiftCertificate> getAll() {
        return certificateService.getAll();
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCertificate(@RequestBody GiftCertificate giftCertificate) {
        return certificateService.addCertificate(giftCertificate);
    }

    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCertificate(@RequestBody GiftCertificate certificate, @PathVariable int id) {
        return certificateService.updateCertificate(certificate, id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteCertificate(@PathVariable int id) {
        return certificateService.deleteCertificate(id);
    }
}
