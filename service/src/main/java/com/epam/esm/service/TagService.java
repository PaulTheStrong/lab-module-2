package com.epam.esm.service;

import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.epam.esm.exception.ExceptionCodes.*;

@Component
@RequestMapping("Tag")
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getById(int id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (!tagOptional.isPresent()) {
            throw new ServiceException(TAG_NOT_FOUND, id);
        }
        return tagOptional.get();
    }

    public void delete(int id) {
        if (tagRepository.countAssociatedCertificates(id) != 0) {
            throw new ServiceException(UNABLE_TO_DELETE_ASSOCIATED_TAG, id);
        }
        if (!tagRepository.delete(id)) {
            throw new ServiceException(TAG_NOT_FOUND, id);
        }
    }

    public Tag save(Tag tag) {
        String lowerCaseName = tag.getName().toLowerCase(Locale.ROOT);
        tag.setName(lowerCaseName);
        tagRepository.save(tag);
        Optional<Tag> updatedTag = tagRepository.findByName(lowerCaseName);
        if (!updatedTag.isPresent()) {
            throw new ServiceException(UNABLE_TO_SAVE_TAG, lowerCaseName);
        }
        return updatedTag.get();
    }

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }
}
