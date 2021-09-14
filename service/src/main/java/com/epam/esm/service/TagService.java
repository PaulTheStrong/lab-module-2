package com.epam.esm.service;

import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.exception.SaveException;
import com.epam.esm.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Component
@RequestMapping("Tag")
public class TagService {

    private final Repository<Tag> tagRepository;

    @Autowired
    public TagService(Repository<Tag> tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getById(int id) {
        Optional<Tag> tagOptional = tagRepository.getById(id);
        if (!tagOptional.isPresent()) {
            throw new ResourceNotFoundException(id);
        }
        return tagOptional.get();
    }

    public void delete(int id) {
        if (!tagRepository.delete(id)) {
            throw new ResourceNotFoundException(id);
        }
    }

    public void save(String tagName) {
        Tag tag = new Tag(tagName);
        tagRepository.save(tag);
    }

    public List<Tag> getAll() {
        return tagRepository.getAll();
    }
}
