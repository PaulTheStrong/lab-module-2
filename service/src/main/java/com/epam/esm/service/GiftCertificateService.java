package com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.impl.GiftCertificatePreparedStatementCreator;
import com.epam.esm.repository.impl.SortColumn;
import com.epam.esm.repository.impl.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.epam.esm.exception.ExceptionCodes.CERTIFICATE_NOT_FOUND;
import static com.epam.esm.exception.ExceptionCodes.SORT_TYPES_MUST_BE_LESS_OR_EQUALS_THAN_COLUMNS;
import static com.epam.esm.exception.ExceptionCodes.TAG_NOT_FOUND;
import static com.epam.esm.exception.ExceptionCodes.UNABLE_TO_SAVE_CERTIFICATE;

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

    /**
     * Sets createDate and lastUpdateDate for gift certificate, saves it in repository,
     * assigns new id. Then updates tags - creates new if needed or find old.
     *
     * @param giftCertificateDto - data to be saved
     * @return updated gift certificate dto with id, tags
     */
    public GiftCertificateDto addCertificate(GiftCertificateDto giftCertificateDto) {
        GiftCertificate certificate = dtoMapper.dtoToGiftCertificate(giftCertificateDto);

        LocalDateTime now = LocalDateTime.now();
        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);

        Optional<GiftCertificate> save = certificateRepository.save(certificate);
        if (!save.isPresent()) {
            String name = giftCertificateDto.getName();
            throw new ServiceException(UNABLE_TO_SAVE_CERTIFICATE, name);
        }

        Integer certificateId = save.get().getId();
        giftCertificateDto.setId(certificateId);
        giftCertificateDto.setCreateDate(certificate.getCreateDate());
        giftCertificateDto.setLastUpdateDate(certificate.getLastUpdateDate());
        updateTags(giftCertificateDto);
        return giftCertificateDto;
    }

    /**
     * Updates GiftCertificate in database and tags, passed in dto
     * @param certificateDto - Dto contains data to be updated
     * @param id identifies gift certificate record in database
     * @return updated dto if certificate with given dto exists. Otherwise, throws
     * service exception.
     */
    public GiftCertificateDto updateCertificate(GiftCertificateDto certificateDto, int id) {
        Optional<GiftCertificate> certificateOpt = certificateRepository.findById(id);
        if (!certificateOpt.isPresent()) {
            throw new ServiceException(CERTIFICATE_NOT_FOUND, id);
        }
        certificateDto.setId(id);
        GiftCertificate oldEntity = certificateOpt.get();
        Double duration = certificateDto.getDuration();
        if (duration != null) {
            oldEntity.setDuration(duration);
        }
        String description = certificateDto.getDescription();
        if (description != null) {
            oldEntity.setDescription(description);
        }

        String name = certificateDto.getName();
        if (name != null) {
            oldEntity.setName(name);
        }

        BigDecimal price = certificateDto.getPrice();
        if (price != null) {
            oldEntity.setPrice(price);
        }

        if (certificateDto.getTags() != null) {
            updateTags(certificateDto);
        }

        oldEntity.setLastUpdateDate(LocalDateTime.now());
        certificateRepository.update(oldEntity);

        List<Tag> newTags = certificateDto.getTags();
        return dtoMapper.giftCertificateToDto(oldEntity, newTags);
    }

    private void updateTags(GiftCertificateDto newEntity) {
        int certificateId = newEntity.getId();
        Set<Tag> oldTags = new HashSet<>(tagRepository.findTagsByCertificateId(certificateId));
        Set<Tag> newTags = new HashSet<>(newEntity.getTags());

        getTagNamesByIdOrSaveInRepository(newTags);
        addAndRemoveAssociationsForCertificate(certificateId, oldTags, newTags);
    }

    private void addAndRemoveAssociationsForCertificate(int certificateId, Set<Tag> oldTags, Set<Tag> newTags) {
        Set<Tag> tagsToRemove = new HashSet<>(oldTags);
        tagsToRemove.removeAll(newTags);

        Set<Tag> tagsToAdd = new HashSet<>(newTags);
        tagsToAdd.removeAll(oldTags);

        tagsToRemove.forEach(tag -> tagRepository.removeCertificateTagAssociation(certificateId, tag.getId()));
        tagsToAdd.forEach(tag -> tagRepository.addCertificateTagAssociation(certificateId, tag.getId()));
    }

    //For each tag, sent by user, find tag in repository by id
    //if provided. Otherwise, try to save tag by name and set created id.
    private void getTagNamesByIdOrSaveInRepository(Set<Tag> newTags) {
        newTags.forEach(tag -> {
            Integer tagId = tag.getId();
            String tagName = tag.getName();
            if (tagId != null) {
                getTagNameByIdAndSave(tag);
            } else if (tagName != null) {
                saveTagInRepositoryAndSetId(tag);
            }
        });
    }

    private void saveTagInRepositoryAndSetId(Tag tag) {
        Optional<Tag> savedTag = tagRepository.save(tag);
        savedTag.ifPresent(t -> tag.setId(t.getId()));
    }

    private void getTagNameByIdAndSave(Tag tag) {
        int tagId = tag.getId();
        Optional<Tag> tagByIdInRepository = tagRepository.findById(tagId);
        if (!tagByIdInRepository.isPresent()) {
            throw new ServiceException(TAG_NOT_FOUND, tagId);
        }
        tag.setName(tagByIdInRepository.get().getName());
    }

    /**
     * @param id - gift certificate id in repository
     * @return gift certificate dto with tags, associated with it.
     * If not found throws Service Exception
     */
    public GiftCertificateDto getById(int id) {
        Optional<GiftCertificate> certificate = certificateRepository.findById(id);
        if (!certificate.isPresent()) {
            throw new ServiceException(CERTIFICATE_NOT_FOUND, id);
        }
        List<Tag> tags = tagRepository.findTagsByCertificateId(id);
        return dtoMapper.giftCertificateToDto(certificate.get(), tags);
    }

    /**
     * @return all GiftCertificateDto found in database with associated tags.
     */
    public List<GiftCertificateDto> getAll() {
        return certificateRepository.findAll()
                .stream()
                .map(cert -> {
                    List<Tag> tagsByCertificateId = tagRepository.findTagsByCertificateId(cert.getId());
                    return dtoMapper.giftCertificateToDto(cert, tagsByCertificateId);
                })
                .collect(Collectors.toList());
    }

    /**
     * Builds a query with given parameters and gets List of GiftCertificateDto
     * that satisfies these parameters.
     * @param nameOrDescription - a part of name or description
     * @param tag - tag name that certificate should contain
     * @param sortColumns - columns by which sorting should be performed {@link SortColumn}
     * @param sortTypes - Ascending or descending order {@link SortType}
     */
    public List<GiftCertificateDto> getWithParameters(Optional<String> nameOrDescription, Optional<String> tag, List<String> sortColumns, List<String> sortTypes) {
        GiftCertificatePreparedStatementCreator.GiftCertificateQueryBuilder queryBuilder = GiftCertificatePreparedStatementCreator.createBuilder();
        nameOrDescription.ifPresent(queryBuilder::searchByNameOrDescription);
        tag.ifPresent(queryBuilder::searchByTag);
        if (sortColumns.size() < sortTypes.size()) {
            throw new ServiceException(SORT_TYPES_MUST_BE_LESS_OR_EQUALS_THAN_COLUMNS);
        }
        IntStream.range(0, sortColumns.size()).forEach(i -> {
            String columnName = sortColumns.get(i).toLowerCase(Locale.ROOT);
            SortColumn column = SortColumn.createColumn(columnName);
            SortType type = SortType.ASC;
            if (i < sortTypes.size()) {
                String sortTypeName = sortTypes.get(i).toLowerCase(Locale.ROOT);
                type = SortType.createType(sortTypeName);
            }
            queryBuilder.sort(column, type);
        });
        List<GiftCertificate> foundCertificates = certificateRepository.findBySpecification(queryBuilder.build());
        return foundCertificates.stream()
                .map(cert -> dtoMapper.giftCertificateToDto(cert, tagRepository.findTagsByCertificateId(cert.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Deletes certificate with given id from database. If certificate not found
     * throws ServiceException.
     * @param id of certificate to be deleted.
     */
    public void deleteCertificate(int id) {
        boolean deleteResult = certificateRepository.delete(id);
        if (!deleteResult) {
            throw new ServiceException(CERTIFICATE_NOT_FOUND, id);
        }
    }
}
