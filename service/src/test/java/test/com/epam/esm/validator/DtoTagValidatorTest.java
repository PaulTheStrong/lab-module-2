package test.com.epam.esm.validator;

import com.epam.esm.entities.Tag;
import com.epam.esm.validator.DtoTagValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.AssertTrue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class DtoTagValidatorTest {

    private DtoTagValidator validator = new DtoTagValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Test
    public void testIsValidShouldReturnTrueWhenIdExists() {
        Tag tag = new Tag(1, null);
        boolean actual = validator.isValid(tag, context);
        assertTrue(actual);
    }

    @Test
    public void testIsValidShouldReturnTrueWhenNameIsOneChar() {
        Tag tag = new Tag(null, "a");
        boolean actual = validator.isValid(tag, context);
        assertTrue(actual);
    }

    @Test
    public void testIsValidShouldReturnTrueWhenNameLengthIsUpperBound() {
        Tag tag = new Tag(null, "aaaaaaaaaaaaaaaaaaaa");
        boolean actual = validator.isValid(tag, context);
        assertTrue(actual);
    }

    @Test
    public void testIsValidShouldReturnFalseWhenNameTooLong() {
        Tag tag = new Tag(null, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        boolean actual = validator.isValid(tag, context);
        assertFalse(actual);
    }

    @Test
    public void testIsValidShouldReturnFalseWhenNameAndIdNull() {
        Tag tag = new Tag(null, null, null);
        boolean actual = validator.isValid(tag, context);
        assertFalse(actual);
    }
}
