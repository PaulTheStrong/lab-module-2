package com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.*;
import com.epam.esm.repository.impl.GiftCertificatePreparedStatementCreator;
import com.epam.esm.repository.impl.SortColumn;
import com.epam.esm.repository.impl.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ExceptionCodes.PROVIDE_CERTIFICATE_NAME;
import static com.epam.esm.exception.ExceptionCodes.RESOURCE_NOT_FOUND;

@Service
public class GiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final DtoMapper dtoMapper;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository certificateRepository, TagRepository tagRepository, DtoMapper dtoMapper) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.dtoMapper = dtoMapper;
    }

    public void addCertificate(GiftCertificateDto giftCertificateDto) {
        if (giftCertificateDto == null || giftCertificateDto.getName() == null) {
            throw new ServiceException(PROVIDE_CERTIFICATE_NAME);
        }
        LocalDateTime now = LocalDateTime.now();
        GiftCertificate certificate = dtoMapper.dtoToGiftCertificate(giftCertificateDto);
        List<String> tags = giftCertificateDto.getTags();

        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);

        certificateRepository.save(certificate);
        tags.forEach(name -> tagRepository.save(new Tag(name)));
    }

    public void updateCertificate(GiftCertificateDto newEntity, int id) {
        Optional<GiftCertificate> certificateOpt = certificateRepository.findById(id);
        if (!certificateOpt.isPresent()) {
            throw new ServiceException(RESOURCE_NOT_FOUND, id);
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
        List<String> oldTags = tagRepository.findTagsByCertificateId(id);
        List<String> newTags = newEntity.getTags();

        Set<String> tagsToRemove = new HashSet<>(oldTags);
        tagsToRemove.removeAll(newTags);

        Set<String> tagsToAdd = new HashSet<>(newTags);
        tagsToAdd.removeAll(oldTags);

        tagsToAdd.stream().map(Tag::new).forEach(tagRepository::save);
        tagsToAdd.forEach(tag -> tagRepository.addCertificateTagAssociation(id, tag));
        tagsToRemove.forEach(tag -> tagRepository.removeCertificateTagAssociation(id, tag));
    }

    public GiftCertificateDto getById(int id) {
        Optional<GiftCertificate> certificate = certificateRepository.findById(id);
        if (!certificate.isPresent()) {
            throw new ServiceException(RESOURCE_NOT_FOUND, id);
        }
        List<String> tags = tagRepository.findTagsByCertificateId(id);
        return dtoMapper.giftCertificateToDto(certificate.get(), tags);
    }

    public List<GiftCertificateDto> getAll() {
        return certificateRepository.findAll()
                .stream()
                .map(cert -> dtoMapper.giftCertificateToDto(cert, tagRepository.findTagsByCertificateId(cert.getId())))
                .collect(Collectors.toList());
    }

    public List<GiftCertificateDto> getWithParameters(Optional<String> search, Optional<String> tag, List<String> sort) {
        GiftCertificatePreparedStatementCreator.GiftCertificateQueryBuilder queryBuilder = GiftCertificatePreparedStatementCreator.createBuilder();
        search.ifPresent(queryBuilder::searchByNameOrDescription);
        tag.ifPresent(queryBuilder::searchByTag);
        sort.forEach(s -> {
            String columnName = s.substring(1).toLowerCase(Locale.ROOT);
            SortColumn column = SortColumn.createColumn(columnName);
            SortType type = SortType.createType(s.substring(0, 1));
            queryBuilder.sort(column, type);
        });
        List<GiftCertificate> foundCertificates = certificateRepository.findBySpecification(queryBuilder.build());
        return foundCertificates.stream()
                .map(cert -> dtoMapper.giftCertificateToDto(cert, tagRepository.findTagsByCertificateId(cert.getId())))
                .collect(Collectors.toList());
    }

    public void deleteCertificate(int id) {
        if (!certificateRepository.delete(id)) {
            throw new ServiceException(RESOURCE_NOT_FOUND, id);
        }
    }
}
