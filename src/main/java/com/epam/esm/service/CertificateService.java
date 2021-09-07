package com.epam.esm.service;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CertificateService {

    private Repository<GiftCertificate> certificateRepository;

    @Autowired
    public void setCertificateRepository(Repository<GiftCertificate> certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public ResponseEntity<String> addCertificate(GiftCertificate giftCertificate) {
        Date now = new Date(System.currentTimeMillis());
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);
        certificateRepository.save(giftCertificate);
        return new ResponseEntity<>("Gift certificate has been added", HttpStatus.OK);
    }

    public ResponseEntity<String> updateCertificate(GiftCertificate newEntity, int id) {
        GiftCertificate oldEntity = certificateRepository.getById(id);
        if (oldEntity != null) {
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
            oldEntity.setLastUpdateDate(new Date(System.currentTimeMillis()));
            certificateRepository.update(oldEntity);
            return new ResponseEntity<>("Gift Certificate(id=" + id + ") has been updated", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot find Gift Certificate(id=" + id + ")", HttpStatus.NOT_FOUND);
        }
    }

    public GiftCertificate getById(int id) {
        return certificateRepository.getById(id);
    }

    public List<GiftCertificate> getAll() {
        return certificateRepository.getAll();
    }

    public ResponseEntity<String> deleteCertificate(int id) {
        certificateRepository.delete(id);
        return new ResponseEntity<>("Certificate has been deleted", HttpStatus.OK);
    }
}
