package com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.data.PageInfo;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.GiftCertificateRepository;
import com.epam.esm.repository.api.TagCertificateUtil;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.repository.impl.FilterParameters;
import com.epam.esm.repository.impl.SortColumn;
import com.epam.esm.repository.impl.SortType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

/**
 * Provides functionality for working with {@link GiftCertificate}
 */
@Service
@Transactional
public class GiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final DtoMapper dtoMapper;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository certificateRepository, TagRepository tagRepository, DtoMapper dtoMapper, TagCertificateUtil tagCertificateUtil) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Sets {@link LocalDateTime} createDate and {@link LocalDateTime} lastUpdateDate
     * for {@link GiftCertificate}, saves it in {@link GiftCertificateRepository},
     * assigns new id. Then updates {@link Tag} entities - creates new if needed or find old.
     *
     * @param giftCertificateDto {@link GiftCertificate} to be saved
     * @return updated {@link GiftCertificateDto} with setted id and tags
     */
    public GiftCertificateDto addCertificate(GiftCertificateDto giftCertificateDto) {
        GiftCertificate certificate = dtoMapper.dtoToGiftCertificate(giftCertificateDto);

        LocalDateTime now = LocalDateTime.now();
        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);

        List<Tag> tags = certificate.getTags();
        getTagNamesByIdOrSaveInRepository(new HashSet<>(tags));
        Optional<GiftCertificate> save = certificateRepository.save(certificate);
        if (!save.isPresent()) {
            throw new ServiceException(UNABLE_TO_SAVE_CERTIFICATE);
        }
        return dtoMapper.giftCertificateToDto(save.get());
    }

    /**
     * Updates {@link GiftCertificate} in database and tags, passed in dto
     * @param certificateDto {@link GiftCertificateDto} that contains data to be updated
     * @param id identifies {@link GiftCertificate} record in database
     * @return updated {@link GiftCertificateDto} if certificate with given dto exists.
     * Otherwise, throws {@link ServiceException}.
     */
    public GiftCertificateDto updateCertificate(GiftCertificateDto certificateDto, int id) {
        Optional<GiftCertificate> certificateOpt = certificateRepository.findById(id);
        if (!certificateOpt.isPresent()) {
            throw new ServiceException(CERTIFICATE_NOT_FOUND, id);
        }
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

        List<Tag> newTags = certificateDto.getTags();
        if (newTags != null) {
            updateTags(oldEntity, oldEntity.getTags(), newTags);
        }

        oldEntity.setLastUpdateDate(LocalDateTime.now());
        return dtoMapper.giftCertificateToDto(oldEntity);
    }

    private void updateTags(GiftCertificate certificate, List<Tag> oldTags, List<Tag> newTags) {
        Set<Tag> oldTagsSet = new HashSet<>(oldTags);
        Set<Tag> newTagsSet = new HashSet<>(newTags);

        getTagNamesByIdOrSaveInRepository(newTagsSet);
        addAndRemoveAssociationsForCertificate(certificate, oldTagsSet, newTagsSet);
    }

    private void addAndRemoveAssociationsForCertificate(GiftCertificate certificateEntity, Set<Tag> oldTags, Set<Tag> newTags) {
        Set<Tag> tagsToRemove = new HashSet<>(oldTags);
        tagsToRemove.removeAll(newTags);

        Set<Tag> tagsToAdd = new HashSet<>(newTags);
        tagsToAdd.removeAll(oldTags);

        tagsToRemove.forEach(certificateEntity::removeTag);
        tagsToAdd.forEach(certificateEntity::addTag);
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
        Optional<Tag> tagByName = tagRepository.findByName(tag.getName());
        if (!tagByName.isPresent()) {
            Optional<Tag> savedTag = tagRepository.save(tag);
            savedTag.ifPresent(t -> {
                tag.setId(t.getId());
            });
        } else {
            Tag savedTag = tagByName.get();
            tag.setId(savedTag.getId());
            tag.setCertificates(savedTag.getCertificates());
        }
    }

    private void getTagNameByIdAndSave(Tag tag) {
        int tagId = tag.getId();
        Optional<Tag> tagByIdInRepository = tagRepository.findById(tagId);
        if (!tagByIdInRepository.isPresent()) {
            throw new ServiceException(TAG_NOT_FOUND, tagId);
        }
        Tag tagFromDatabase = tagByIdInRepository.get();
        tag.setName(tagFromDatabase.getName());
        tag.setCertificates(tagFromDatabase.getCertificates());
    }

    /**
     * @param id {@link GiftCertificate} id in repository
     * @return {@link GiftCertificateDto} with {@link Tag}s, associated with it.
     * @throws ServiceException if not found.
     */
    public GiftCertificateDto getById(int id) {
        Optional<GiftCertificate> certificate = certificateRepository.findById(id);
        if (!certificate.isPresent()) {
            throw new ServiceException(CERTIFICATE_NOT_FOUND, id);
        }
        return dtoMapper.giftCertificateToDto(certificate.get());
    }

    /**
     * @param pageNumber the number of page
     * @param pageSize size of the single page
     * @return pageSize {@link GiftCertificateDto} entities found in database with
     * associated {@link Tag}s starting from (pageNumber - 1) * pageSize.
     */
    public List<GiftCertificateDto> getCertificates(int pageNumber, int pageSize) {
        return certificateRepository.findAll(pageNumber, pageSize)
                .stream()
                .map(dtoMapper::giftCertificateToDto)
                .collect(Collectors.toList());
    }

    /**
     * Builds a query with given parameters and gets {@link List} of pageSize
     * of {@link GiftCertificateDto} that satisfies these parameters starting from
     * (pageNumber - 1) * pageSize.
     * @param nameOrDescription a part of name or description
     * @param tags {@link Tag} names that certificate should contain
     * @param sortColumns columns by which sorting should be performed {@link SortColumn}
     * @param sortTypes Ascending or descending order {@link SortType}
     * @param pageNumber the number of the page
     * @param pageSize size of the single page.
     * @throws ServiceException if wrong parameters has benn passed
     */
    public List<GiftCertificateDto> getWithParameters(
            Optional<String> nameOrDescription,
            Optional<Set<String>> tags,
            List<String> sortColumns,
            List<String> sortTypes,
            int pageNumber,
            int pageSize
    ) {
        FilterParameters parameters = prepareParameters(nameOrDescription, tags, sortColumns, sortTypes);
        List<GiftCertificate> foundCertificates = certificateRepository.findBySpecification(parameters, pageNumber, pageSize);
        return foundCertificates.stream()
                .map(dtoMapper::giftCertificateToDto)
                .collect(Collectors.toList());
    }

    private FilterParameters prepareParameters(
            Optional<String> nameOrDescription,
            Optional<Set<String>> tags,
            List<String> sortColumns,
            List<String> sortTypes
    ) {
        FilterParameters.FilterParametersBuilder builder = FilterParameters.builder();
        nameOrDescription.ifPresent(builder::withNameOrDescription);
        tags.ifPresent(set -> set.forEach(builder::withTag));
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
            builder.withSort(column, type);
        });
        return builder.build();
    }

    /**
     * Deletes {@link GiftCertificate} with given id from database
     * @param id of certificate to be deleted.
     * @throws ServiceException if no {@link GiftCertificate} in database.
     */
    public void deleteCertificate(int id) {
        boolean deleteResult = certificateRepository.delete(id);
        if (!deleteResult) {
            throw new ServiceException(CERTIFICATE_NOT_FOUND, id);
        }
    }

    public PageInfo giftCertificatePageInfo(int pageNumber, int pageSize) {
        int usersCount = certificateRepository.countAll();
        return new PageInfo(pageSize, pageNumber, usersCount);
    }

    public PageInfo giftCertificatePageInfoWithParameters(
            Optional<String> nameOrDescription,
            Optional<Set<String>> tags,
            List<String> sortColumns,
            List<String> sortTypes,
            int pageNumber,
            int pageSize
    ) {
        FilterParameters filterParameters = prepareParameters(nameOrDescription, tags, sortColumns, sortTypes);
        int count = certificateRepository.countEntitiesBySpecification(filterParameters);
        return new PageInfo(pageSize, pageNumber, count);
    }
}
