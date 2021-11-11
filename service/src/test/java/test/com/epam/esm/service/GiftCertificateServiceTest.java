package test.com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ExceptionCodes;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.GiftCertificateRepository;
import com.epam.esm.repository.api.TagCertificateUtil;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.DtoMapper;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GiftCertificateServiceTest {

    @Mock
    private GiftCertificateRepository mockCertificateRepository;

    @Mock
    private TagRepository mockTagRepository;

    @Mock
    private DtoMapper mockDtoMapper;

    @Mock
    private TagCertificateUtil mockTagCertificateUtil;

    @InjectMocks
    private GiftCertificateService service;

    private static final Tag[] TEST_TAGS = {
        new Tag(1, "sport"),
        new Tag(2, "food"),
        new Tag(3, "beauty")
    };
    private static final List<Tag> TAGS_FOR_CERTIFICATE_1 = Arrays.asList(TEST_TAGS[0], TEST_TAGS[2]);
    private static final List<Tag> TAGS_FOR_CERTIFICATE_2 = Arrays.asList(TEST_TAGS[2]);

    private static final GiftCertificate[] TEST_GIFT_CERTIFICATES = {
        new GiftCertificate(0, "Name1", "Descr1", new BigDecimal("10"), 10.0, LocalDateTime.now(), LocalDateTime.now(), TAGS_FOR_CERTIFICATE_1),
        new GiftCertificate(1, "Name2", "Descr2", new BigDecimal("10"), 10.0, LocalDateTime.now(), LocalDateTime.now(), TAGS_FOR_CERTIFICATE_2),
    };

    private static final GiftCertificateDto[] TEST_DTOS = {
            new GiftCertificateDto(TEST_GIFT_CERTIFICATES[0].getId(), TEST_GIFT_CERTIFICATES[0].getName(), TEST_GIFT_CERTIFICATES[0].getDescription(), TEST_GIFT_CERTIFICATES[0].getPrice(), TEST_GIFT_CERTIFICATES[0].getDuration(), TEST_GIFT_CERTIFICATES[0].getCreateDate(), TEST_GIFT_CERTIFICATES[0].getLastUpdateDate(), TAGS_FOR_CERTIFICATE_1),
            new GiftCertificateDto(TEST_GIFT_CERTIFICATES[1].getId(), TEST_GIFT_CERTIFICATES[1].getName(), TEST_GIFT_CERTIFICATES[1].getDescription(), TEST_GIFT_CERTIFICATES[1].getPrice(), TEST_GIFT_CERTIFICATES[1].getDuration(), TEST_GIFT_CERTIFICATES[1].getCreateDate(), TEST_GIFT_CERTIFICATES[1].getLastUpdateDate(), TAGS_FOR_CERTIFICATE_2)
    };

    @BeforeEach
    public void setWhens() {
        GiftCertificate certificate1 = TEST_GIFT_CERTIFICATES[0];
        GiftCertificate certificate2 = TEST_GIFT_CERTIFICATES[1];

        when(mockDtoMapper.dtoToGiftCertificate(TEST_DTOS[0])).thenReturn(certificate1);
        when(mockDtoMapper.dtoToGiftCertificate(TEST_DTOS[1])).thenReturn(certificate2);
        when(mockDtoMapper.giftCertificateToDto(certificate1)).thenReturn(TEST_DTOS[0]);
        when(mockDtoMapper.giftCertificateToDto(certificate2)).thenReturn(TEST_DTOS[1]);

        when(mockTagRepository.findById(TEST_TAGS[0].getId())).thenReturn(Optional.of(TEST_TAGS[0]));
        when(mockTagRepository.findById(TEST_TAGS[1].getId())).thenReturn(Optional.of(TEST_TAGS[1]));
        when(mockTagRepository.findById(TEST_TAGS[2].getId())).thenReturn(Optional.of(TEST_TAGS[2]));

        when(mockCertificateRepository.findById(0)).thenReturn(Optional.of(certificate1));
        when(mockCertificateRepository.findById(1)).thenReturn(Optional.of(certificate2));

        when(mockCertificateRepository.findAll(PageRequest.of(1, 10))).thenReturn(new PageImpl<>(Arrays.asList(TEST_GIFT_CERTIFICATES)));

        when(mockCertificateRepository.findById(certificate1.getId())).thenReturn(Optional.of(certificate1));
        when(mockCertificateRepository.findById(certificate2.getId())).thenReturn(Optional.of(certificate2));

        when(mockCertificateRepository.save(certificate1)).thenReturn(certificate1);
    }

    @Test
    public void testGetAllCertificatesWhenRepositoryNotEmptyShouldReturnAllDtosWithTags() {
        List<GiftCertificateDto> result = service.getCertificates(1, 10);

        assertEquals(TEST_DTOS[0], result.get(0));
        assertEquals(TEST_DTOS[1], result.get(1));
        assertEquals(2, result.size());
        Assertions.assertTrue(result.get(0).getTags().containsAll(TAGS_FOR_CERTIFICATE_1));
        Assertions.assertTrue(result.get(1).getTags().containsAll(TAGS_FOR_CERTIFICATE_2));
    }

    @Test
    public void testGetAllCertificatesWhenRepositoryEmptyShouldReturnEmptyList() {
        when(mockCertificateRepository.findAll(PageRequest.of(1, 10))).thenReturn(Page.empty());

        List<GiftCertificateDto> result = service.getCertificates(1, 10);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCertificateByIdWhenCertificateInDatabaseShouldReturnNonEmptyResult() {

        GiftCertificateDto result = service.getById(TEST_GIFT_CERTIFICATES[0].getId());

        assertEquals(TEST_DTOS[0], result);
    }

    @Test
    public void testGetGiftCertificatesByIdWhenCertificateNotFoundShouldThrowServiceException() {
        when(mockCertificateRepository.findById(0)).thenReturn(Optional.empty());
        assertThrows(ServiceException.class, () -> service.getById(0));
    }

    @Test
    public void testDeleteShouldThrowServiceExceptionWhenRequestedRecordNotFound() {
        when(mockCertificateRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> service.deleteCertificate(1));
    }

    @Test
    public void testGiftCertificateShouldReturnCertificateWhenSave() {
        GiftCertificate testGiftCertificate = TEST_GIFT_CERTIFICATES[1];
        GiftCertificateDto testDto = TEST_DTOS[1];
        Tag firstTag = TAGS_FOR_CERTIFICATE_2.get(0);
        Integer firstTagId = firstTag.getId();

        when(mockTagRepository.findById(firstTagId)).thenReturn(Optional.of(firstTag));
        when(mockCertificateRepository.save(testGiftCertificate)).thenReturn(testGiftCertificate);

        GiftCertificateDto giftCertificateDto = service.addCertificate(testDto);

        assertEquals(testGiftCertificate.getName(), giftCertificateDto.getName());
        assertEquals(firstTag, giftCertificateDto.getTags().get(0));
        assertEquals(testGiftCertificate.getId(), giftCertificateDto.getId());
    }

    @Test
    public void testGetWithParametersThrowServiceExceptionWhenSortTypesMoreThanSortColumns() {
        ServiceException serviceException = assertThrows(ServiceException.class,
                () -> service.getWithParameters(Optional.empty(), Optional.empty(),
                        Arrays.asList("a", "b"), Arrays.asList("a", "b", "c"), 1, 10
                        ));
        assertEquals(ExceptionCodes.SORT_TYPES_MUST_BE_LESS_OR_EQUALS_THAN_COLUMNS, serviceException.getErrorCode());
    }

    @Test
    public void testGetWithParametersShouldReturnCertificatesByParametersWithValidParameters() {
        when(mockCertificateRepository.findBySpecification(any(), eq(1), eq(10))).thenReturn(Arrays.asList(TEST_GIFT_CERTIFICATES));

        List<GiftCertificateDto> actual = service.getWithParameters(Optional.empty(), Optional.empty(),
                Arrays.asList("date"), Arrays.asList("DESC"),
                1, 10);

        assertEquals(2, actual.size());
        Assertions.assertArrayEquals(TEST_DTOS, actual.toArray());
    }

    @Test
    public void testUpdateCertificateShouldReturnUpdatedCertificateWithNewTagsAndAllUpdatedFields() {
        List<Tag> oldTags = new ArrayList<>(Arrays.asList(TEST_TAGS[0]));
        Tag tagByName = new Tag(null, "beauty");
        List<Tag> newTags = new ArrayList<>(Arrays.asList(TEST_TAGS[0], TEST_TAGS[1], tagByName));
        GiftCertificate oldCertificate = new GiftCertificate(1, "a", "b", BigDecimal.TEN, 1.0, null, null, oldTags);
        GiftCertificate newCertificate = new GiftCertificate(1, "b", "b", BigDecimal.ONE, 2.0, null, null, newTags);
        GiftCertificateDto giftCertificateDto = new GiftCertificateDto(1, "b", "b", BigDecimal.ONE, 2.0, null, null, newTags);

        when(mockCertificateRepository.findById(1)).thenReturn(Optional.of(oldCertificate));
        when(mockTagRepository.findById(1)).thenReturn(Optional.of(TEST_TAGS[0]));
        when(mockTagRepository.findById(2)).thenReturn(Optional.of(TEST_TAGS[1]));
        when(mockTagRepository.findByName(tagByName.getName())).thenReturn(Optional.of(tagByName));
        when(mockTagRepository.save(tagByName)).thenReturn(new Tag(3, "beauty"));
        when(mockDtoMapper.giftCertificateToDto(oldCertificate)).thenReturn(giftCertificateDto);

        GiftCertificateDto actual = service.updateCertificate(giftCertificateDto, 1);

        assertEquals(giftCertificateDto, actual);
    }

}
