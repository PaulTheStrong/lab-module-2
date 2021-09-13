package com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.SaveException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final DtoMapper dtoMapper;

    @Autowired
    public CertificateService(GiftCertificateRepository certificateRepository, DtoMapper dtoMapper) {
        this.certificateRepository = certificateRepository;
        this.dtoMapper = dtoMapper;
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
            throw new ResourceNotFoundException(id);
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

    public GiftCertificateDto getById(int id) {
        Optional<GiftCertificate> certificate = certificateRepository.getById(id);
        if (!certificate.isPresent()) {
            throw new ResourceNotFoundException(id);
        }
        return dtoMapper.mapCertificateToDto(certificate.get());
    }

    public List<GiftCertificateDto> getAll() {
        return certificateRepository.getAll().stream().map(dtoMapper::mapCertificateToDto).collect(Collectors.toList());
    }

    public void deleteCertificate(int id) {
        if (!certificateRepository.delete(id)) {
            throw new ResourceNotFoundException(id);
        }
    }
}
