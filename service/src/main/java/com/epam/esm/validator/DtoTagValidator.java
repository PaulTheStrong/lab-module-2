package com.epam.esm.validator;

import com.epam.esm.entities.Tag;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DtoTagValidator implements ConstraintValidator<DtoTag, Tag> {

    @Override
    public boolean isValid(Tag tag, ConstraintValidatorContext context) {
        boolean valid = true;
        if (!((tag.getName() != null
                && tag.getName().length() < 20
                && tag.getName().length() > 1)
                || (tag.getId() != null))) {
            context.buildConstraintViolationWithTemplate("{tag.dto.constraint.message}");
            valid = false;
        }
        return valid;
    }
}
