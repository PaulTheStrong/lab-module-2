package com.epam.esm.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.epam.esm.exception.ExceptionCodes.TAG_NAME_OR_ID_MUST_BE_SPECIFIED;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, TYPE_PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = DtoTagValidator.class)
@Documented
public @interface DtoTag {

    String message() default TAG_NAME_OR_ID_MUST_BE_SPECIFIED;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
