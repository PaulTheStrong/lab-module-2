package test.com.epam.esm.service;

import com.epam.esm.data.PageInfo;
import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    private static final Tag[] TEST_TAGS = {
            new Tag(1, "sport"),
            new Tag(2, "food"),
            new Tag(3, "beauty")
    };

    @Test
    public void testGetByIdShouldReturnTagWhenTagInDb() {
        when(tagRepository.findById(1)).thenReturn(Optional.of(TEST_TAGS[0]));

        Tag result = tagService.getById(1);

        assertEquals(TEST_TAGS[0], result);
    }

    @Test
    public void testGetByIdShouldThrowServiceExceptionWhenNoResultFound() {
        when(tagRepository.findById(0)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> tagService.getById(0));
    }

    @Test
    public void testGetAllShouldReturnAllWhenDbContainsTags() {
        List<Tag> tagList = Arrays.asList(TEST_TAGS);
        when(tagRepository.findAll(1, 10)).thenReturn(tagList);

        List<Tag> result = tagService.getTags(1, 10);

        Assertions.assertTrue(result.containsAll(tagList));
    }

    @Test
    public void testGetAllShouldReturnEmptyListWhenNothingFound() {
        when(tagRepository.findAll(1, 10)).thenReturn(Collections.emptyList());

        List<Tag> result = tagService.getTags(1, 10);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeleteShouldThrowServiceExceptionWhenRequestedRecordNotFound() {
        when(tagRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ServiceException.class, () -> tagService.delete(1));
    }

    @Test
    public void testSaveShouldReturnTag() {
        Tag testTag = TEST_TAGS[0];
        when(tagRepository.save(testTag)).thenReturn(Optional.of(testTag));

        Tag saved = tagService.save(testTag);

        assertEquals(testTag, saved);
    }

    @Test
    public void testGetTagPageInfo() {
        when(tagRepository.countAll()).thenReturn(15);
        PageInfo expected = new PageInfo(5, 2, 15);
        PageInfo actual = tagService.tagPageInfo(2, 5);
        assertEquals(expected.getCurrentPage(), actual.getCurrentPage());
        assertEquals(expected.getEntityCount(), actual.getEntityCount());
        assertEquals(expected.getPageSize(), actual.getPageSize());
    }
}
