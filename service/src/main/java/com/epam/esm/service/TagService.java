package com.epam.esm.service;

import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.exception.ExceptionCodes.RESOURCE_NOT_FOUND;

@Component
@RequestMapping("Tag")
public class TagService {

    private final Repository<Tag> tagRepository;

    @Autowired
    public TagService(Repository<Tag> tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getById(int id) {
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (!tagOptional.isPresent()) {
            throw new ServiceException(RESOURCE_NOT_FOUND, id);
        }
        return tagOptional.get();
    }

    public void delete(int id) {
        if (!tagRepository.delete(id)) {
            throw new ServiceException(RESOURCE_NOT_FOUND, id);
        }
    }

    public void save(String tagName) {
        Tag tag = new Tag(tagName);
        tagRepository.save(tag);
    }

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }
}
