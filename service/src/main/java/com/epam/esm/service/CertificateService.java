package com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.SaveException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.Repository;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final DtoMapper dtoMapper;

    @Autowired
    public CertificateService(GiftCertificateRepository certificateRepository, TagRepository tagRepository, DtoMapper dtoMapper) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.dtoMapper = dtoMapper;
    }

    public void addCertificate(GiftCertificateDto giftCertificateDto) {
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = dtoMapper.dtoToGiftCertificate(giftCertificateDto);
        List<String> tags = giftCertificateDto.getTags();

        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);

        certificateRepository.save(certificate);
        tags.forEach(name -> tagRepository.save(new Tag(name)));
    }

    public void updateCertificate(GiftCertificateDto newEntity, int id) {
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

        if (newEntity.getTags() != null) {
            updateTags(newEntity, id);
        }

        oldEntity.setLastUpdateDate(LocalDateTime.now());
        certificateRepository.update(oldEntity);
    }

    private void updateTags(GiftCertificateDto newEntity, int id) {
        List<String> oldTags = tagRepository.getTagsByCertificateId(id);
        List<String> newTags = newEntity.getTags();

        Set<String> tagsForRemove = new HashSet<>(oldTags);
        tagsForRemove.removeAll(newTags);

        Set<String> tagsToAdd = new HashSet<>(newTags);
        tagsToAdd.removeAll(oldTags);

        tagsToAdd.stream().map(Tag::new).forEach(tagRepository::save);
        tagsToAdd.forEach(tag -> certificateRepository.addTagToCertificate(id, tag));
        tagsForRemove.forEach(tag -> certificateRepository.removeTagFromCertificate(id, tag));
    }

    public GiftCertificateDto getById(int id) {
        Optional<GiftCertificate> certificate = certificateRepository.getById(id);
        if (!certificate.isPresent()) {
            throw new ResourceNotFoundException(id);
        }
        List<String> tags = tagRepository.getTagsByCertificateId(id);
        return dtoMapper.mapCertificateToDto(certificate.get(), tags);
    }

    public List<GiftCertificateDto> getAll() {
        return certificateRepository.getAll()
                .stream()
                .map(cert -> dtoMapper.mapCertificateToDto(cert, tagRepository.getTagsByCertificateId(cert.getId())))
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> getByNameOrDescription(String searchString) {
        return certificateRepository.getByNameOrDescription(searchString)
                .stream()
                .map(cert -> dtoMapper.mapCertificateToDto(cert, tagRepository.getTagsByCertificateId(cert.getId())))
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> getByTagName(String tagName) {
        return certificateRepository.getByTagName(tagName)
                .stream()
                .map(cert -> dtoMapper.mapCertificateToDto(cert, tagRepository.getTagsByCertificateId(cert.getId())))
                .collect(Collectors.toList());
    }

    public void deleteCertificate(int id) {
        if (!certificateRepository.delete(id)) {
            throw new ResourceNotFoundException(id);
        }
    }

}
