package com.epam.esm.service;

import com.epam.esm.data.PageInfo;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.TagCertificateUtil;
import com.epam.esm.repository.api.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.epam.esm.exception.ExceptionCodes.*;

@Component
@RequestMapping("Tag")
@Transactional
public class TagService {

    private final TagRepository tagRepository;
    private final TagCertificateUtil tagCertificateUtil;

    @Autowired
    public TagService(TagRepository tagRepository, TagCertificateUtil tagCertificateUtil) {
        this.tagRepository = tagRepository;
        this.tagCertificateUtil = tagCertificateUtil;
    }

    /**
     * @param id {@link Tag} object's id stored in database.
     * @return {@link Tag} with specified id from database if exists.
     * @throws ServiceException if {@link Tag} not exists.
     */
    public Tag getById(int id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (!tagOptional.isPresent()) {
            throw new ServiceException(TAG_NOT_FOUND, id);
        }
        return tagOptional.get();
    }

    /**
     * Deletes {@link Tag} from database if it exists and is not associated with any
     * {@link GiftCertificate}.
     * @param id - {@link Tag} object's id to be deleted.
     * @throws ServiceException if {@link Tag} doesn't exists or
     * associated with {@link GiftCertificate}
     */
    public void delete(int id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (!tagOptional.isPresent()) {
            throw new ServiceException(TAG_NOT_FOUND, id);
        }
        if (tagCertificateUtil.countAssociatedCertificates(id) != 0) {
            throw new ServiceException(UNABLE_TO_DELETE_ASSOCIATED_TAG, id);
        }
        tagRepository.delete(tagOptional.get());
    }

    /**
     * Saves {@link Tag} object with lowercase name in database.
     * @param tag - {@link Tag} object to be saved in database.
     * @return Updated Tag object saved in database with newly assigned id.
     * @throws ServiceException if error occurs during saving process.
     */
    public Tag save(Tag tag) {
        String tagName = tag.getName();
        Optional<Tag> tagByNameFromRepo = tagRepository.findByName(tagName);
        if (tagByNameFromRepo.isPresent()) {
            throw new ServiceException(TAG_ALREADY_EXISTS);
        }
        String lowerCaseName = tagName.toLowerCase(Locale.ROOT);
        tag.setName(lowerCaseName);
        return tagRepository.save(tag);
    }

    /**
     * @param pageNumber the number of the page
     * @param pageSize count of {@link Tag} entities on single page.
     * @return {@link Tag} objects in pageable format.
     */
    public List<Tag> getTags(int pageNumber, int pageSize) {
        return tagRepository.findAll(PageRequest.of(pageNumber - 1, pageSize)).getContent();
    }

    public PageInfo tagPageInfo(int pageNumber, int pageSize) {
        int tagCount = (int)tagRepository.count();
        return new PageInfo(pageSize, pageNumber, tagCount);
    }
}
