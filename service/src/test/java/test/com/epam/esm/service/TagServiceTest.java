package test.com.epam.esm.service;

import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.com.epam.esm.config.ServiceTestConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
public class TagServiceTest {

    private final TagService tagService;
    private final TagRepository tagRepository;

    @Autowired
    public TagServiceTest(TagService tagService, TagRepository tagRepository) {
        this.tagService = tagService;
        this.tagRepository = tagRepository;
    }

    private static final Tag[] TEST_TAGS = {
            new Tag(1, "sport"),
            new Tag(2, "food"),
            new Tag(3, "beauty")
    };

    @Test
    public void testGetByIdShouldReturnTagWhenTagInDb() {
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.of(TEST_TAGS[0]));

        Tag result = tagService.getById(1);

        Assertions.assertEquals(TEST_TAGS[0], result);
    }

    @Test
    public void testGetByIdShouldThrowServiceExceptionWhenNoResultFound() {
        Mockito.when(tagRepository.findById(0)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> tagService.getById(0));
    }

    @Test
    public void testGetAllShouldReturnAllWhenDbContainsTags() {
        List<Tag> tagList = Arrays.asList(TEST_TAGS);
        Mockito.when(tagRepository.findAll()).thenReturn(tagList);

        List<Tag> result = tagService.getTags();

        Assertions.assertTrue(result.containsAll(tagList));
    }

    @Test
    public void testGetAllShouldReturnEmptyListWhenNothingFound() {
        Mockito.when(tagRepository.findAll()).thenReturn(Collections.emptyList());

        List<Tag> result = tagService.getTags();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteShouldThrowServiceExceptionWhenRequestedRecordNotFound() {
        Mockito.when(tagRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ServiceException.class, () -> tagService.delete(1));
    }

    @Test
    public void testSaveShouldReturnTag() {
        Tag testTag = TEST_TAGS[0];
        Mockito.when(tagRepository.save(testTag)).thenReturn(Optional.of(testTag));
        Mockito.when(tagService.save(testTag)).thenReturn(testTag);
    }
}
