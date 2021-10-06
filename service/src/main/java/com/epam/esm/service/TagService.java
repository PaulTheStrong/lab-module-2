package com.epam.esm.service;

import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.TagCertificateUtil;
import com.epam.esm.repository.api.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.epam.esm.exception.ExceptionCodes.TAG_NOT_FOUND;
import static com.epam.esm.exception.ExceptionCodes.UNABLE_TO_DELETE_ASSOCIATED_TAG;
import static com.epam.esm.exception.ExceptionCodes.UNABLE_TO_SAVE_TAG;

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
     * @param id - tag object's id stored in database.
     * @return tag with specified id from database if exists.
     * Otherwise, throws ServiceException.
     */
    public Tag getById(int id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (!tagOptional.isPresent()) {
            throw new ServiceException(TAG_NOT_FOUND, id);
        }
        return tagOptional.get();
    }

    /**
     * Deletes tag from database if it exists and is not associated with any
     * certificate. Otherwise, throws ServiceException.
     * @param id - tag object's id to be deleted.
     */
    public void delete(int id) {
        if (tagCertificateUtil.countAssociatedCertificates(id) != 0) {
            throw new ServiceException(UNABLE_TO_DELETE_ASSOCIATED_TAG, id);
        }
        if (!tagRepository.delete(id)) {
            throw new ServiceException(TAG_NOT_FOUND, id);
        }
    }

    /**
     * Saves tag object with lowercase name in database.
     * If error occurs during saving process, ServiceException is thrown.
     * @param tag - tag object to be saved in database.
     * @return Updated Tag object saved in database with newly assigned id.
     */
    public Tag save(Tag tag) {
        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new ServiceException("Tag already exists");
        }
        String lowerCaseName = tag.getName().toLowerCase(Locale.ROOT);
        tag.setName(lowerCaseName);
        Optional<Tag> updatedTag = tagRepository.save(tag);
        if (!updatedTag.isPresent()) {
            throw new ServiceException(UNABLE_TO_SAVE_TAG, lowerCaseName);
        }
        return updatedTag.get();
    }

    /**
     * @return All tag objects found in database.
     */
    public List<Tag> getTags(int pageNumber, int pageSize) {
        return tagRepository.findAll(pageNumber, pageSize);
    }
}
