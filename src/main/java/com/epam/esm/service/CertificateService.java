package com.epam.esm.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.exception.CertificateNotFoundException;
import com.epam.esm.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    private final Repository<GiftCertificate> certificateRepository;

    @Autowired
    public CertificateService(Repository<GiftCertificate> certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public void addCertificate(GiftCertificate giftCertificate) {
        LocalDateTime now = LocalDateTime.now();
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);
        certificateRepository.save(giftCertificate);
    }

    public void updateCertificate(GiftCertificate newEntity, int id) {
        Optional<GiftCertificate> certificateOpt = certificateRepository.getById(id);
        if (!certificateOpt.isPresent()) {
            throw new CertificateNotFoundException();
        }
        GiftCertificate oldEntity = certificateOpt.get();
        Double duration = newEntity.getDuration();
        if (duration != null) {
            oldEntity.setDuration(duration);
        }
        String description = newEntity.getDescription();
        if (description != null) {
            oldEntity.setDescription(description);
        }

        String name = newEntity.getName();
        if (name != null) {
            oldEntity.setName(name);
        }

        BigDecimal price = newEntity.getPrice();
        if (price != null) {
            oldEntity.setPrice(price);
        }
        oldEntity.setLastUpdateDate(LocalDateTime.now());
        certificateRepository.update(oldEntity);
    }

    public GiftCertificate getById(int id) {
        Optional<GiftCertificate> certificate = certificateRepository.getById(id);
        if (!certificate.isPresent()) {
            throw new CertificateNotFoundException();
        }
        return certificate.get();
    }

    public List<GiftCertificate> getAll() {
        return certificateRepository.getAll();
    }

    public void deleteCertificate(int id) {
        certificateRepository.delete(id);
    }
}