package test.com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.GiftCertificateRepository;
import com.epam.esm.repository.api.TagCertificateUtil;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.service.DtoMapper;
import com.epam.esm.service.GiftCertificateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import test.com.epam.esm.config.ServiceTestConfig;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
public class GiftCertificateServiceTest {

    private final GiftCertificateRepository mockCertificateRepository;
    private final TagRepository mockTagRepository;
    private final DtoMapper mockDtoMapper;
    private final GiftCertificateService service;
    private final TagCertificateUtil mockTagCertificateUtil;

    @Autowired
    public GiftCertificateServiceTest(GiftCertificateRepository mockCertificateRepository, TagRepository mockTagRepository, DtoMapper mockDtoMapper, GiftCertificateService service, TagCertificateUtil mockTagCertificateUtil) {
        this.mockCertificateRepository = mockCertificateRepository;
        this.mockTagRepository = mockTagRepository;
        this.mockDtoMapper = mockDtoMapper;
        this.service = service;
        this.mockTagCertificateUtil = mockTagCertificateUtil;
    }

    private static final Tag[] TEST_TAGS = {
        new Tag(1, "sport"),
        new Tag(2, "food"),
        new Tag(3, "beauty")
    };
    private static final List<Tag> TAGS_FOR_CERTIFICATE_2 = Arrays.asList(TEST_TAGS[2]);
    private static final List<Tag> TAGS_FOR_CERTIFICATE_1 = Arrays.asList(TEST_TAGS[0], TEST_TAGS[2]);

    private static final GiftCertificate[] TEST_GIFT_CERTIFICATES = {
        new GiftCertificate(0, "Name1", "Descr1", new BigDecimal("10"), 10.0, LocalDateTime.now(), LocalDateTime.now()),
        new GiftCertificate(1, "Name2", "Descr2", new BigDecimal("10"), 10.0, LocalDateTime.now(), LocalDateTime.now()),
    };

    private static final GiftCertificateDto[] TEST_DTOS = {
            new GiftCertificateDto(TEST_GIFT_CERTIFICATES[0].getId(), TEST_GIFT_CERTIFICATES[0].getName(), TEST_GIFT_CERTIFICATES[0].getDescription(), TEST_GIFT_CERTIFICATES[0].getPrice(), TEST_GIFT_CERTIFICATES[0].getDuration(), TEST_GIFT_CERTIFICATES[0].getCreateDate(), TEST_GIFT_CERTIFICATES[0].getLastUpdateDate(), TAGS_FOR_CERTIFICATE_1),
            new GiftCertificateDto(TEST_GIFT_CERTIFICATES[1].getId(), TEST_GIFT_CERTIFICATES[1].getName(), TEST_GIFT_CERTIFICATES[1].getDescription(), TEST_GIFT_CERTIFICATES[1].getPrice(), TEST_GIFT_CERTIFICATES[1].getDuration(), TEST_GIFT_CERTIFICATES[1].getCreateDate(), TEST_GIFT_CERTIFICATES[1].getLastUpdateDate(), TAGS_FOR_CERTIFICATE_2)
    };

    @Test
    public void testGetAllCertificatesWhenRepositoryNotEmptyShouldReturnAllDtosWithTags() {
        Mockito.when(mockTagCertificateUtil.findTagsByCertificateId(0)).thenReturn(TAGS_FOR_CERTIFICATE_1);
        Mockito.when(mockTagCertificateUtil.findTagsByCertificateId(1)).thenReturn(TAGS_FOR_CERTIFICATE_2);

        Mockito.when(mockCertificateRepository.findAll()).thenReturn(Arrays.asList(TEST_GIFT_CERTIFICATES));

        Mockito.when(mockDtoMapper.giftCertificateToDto(TEST_GIFT_CERTIFICATES[0], TAGS_FOR_CERTIFICATE_1)).thenReturn(TEST_DTOS[0]);
        Mockito.when(mockDtoMapper.giftCertificateToDto(TEST_GIFT_CERTIFICATES[1], TAGS_FOR_CERTIFICATE_2)).thenReturn(TEST_DTOS[1]);

        List<GiftCertificateDto> result = service.getCertificates();

        Assertions.assertEquals(TEST_DTOS[0], result.get(0));
        Assertions.assertEquals(TEST_DTOS[1], result.get(1));
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.get(0).getTags().containsAll(TAGS_FOR_CERTIFICATE_1));
        Assertions.assertTrue(result.get(1).getTags().containsAll(TAGS_FOR_CERTIFICATE_2));
    }

    @Test
    public void testGetAllCertificatesWhenRepositoryEmptyShouldReturnEmptyList() {
        Mockito.when(mockCertificateRepository.findAll()).thenReturn(Collections.EMPTY_LIST);

        List<GiftCertificateDto> result = service.getCertificates();

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetCertificateByIdWhenCertificateInDatabaseShouldReturnNonEmptyResult() {
        Mockito.when(mockTagCertificateUtil.findTagsByCertificateId(0)).thenReturn(TAGS_FOR_CERTIFICATE_1);
        Mockito.when(mockCertificateRepository.findById(TEST_GIFT_CERTIFICATES[0].getId())).thenReturn(Optional.of(TEST_GIFT_CERTIFICATES[0]));
        Mockito.when(mockDtoMapper.giftCertificateToDto(TEST_GIFT_CERTIFICATES[0], TAGS_FOR_CERTIFICATE_1)).thenReturn(TEST_DTOS[0]);

        GiftCertificateDto result = service.getById(TEST_GIFT_CERTIFICATES[0].getId());

        Assertions.assertEquals(TEST_DTOS[0], result);
    }

    @Test
    public void testGetGiftCertificatesByIdWhenCertificateNotFoundShouldThrowServiceException() {
        Mockito.when(mockCertificateRepository.findById(0)).thenReturn(Optional.empty());
        Assertions.assertThrows(ServiceException.class, () -> service.getById(0));
    }

    @Test
    public void testDeleteShouldThrowServiceExceptionWhenRequestedRecordNotFound() {
        Mockito.when(mockCertificateRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ServiceException.class, () -> service.deleteCertificate(1));
    }

    @Test
    public void testGiftCertificateShouldReturnCertificateWhenSave() {
        GiftCertificate testGiftCertificate = TEST_GIFT_CERTIFICATES[1];
        GiftCertificateDto testDto = TEST_DTOS[1];

        Mockito.when(mockCertificateRepository.save(testGiftCertificate))
                .thenReturn(Optional.of(testGiftCertificate));
        Integer tagId1 = TAGS_FOR_CERTIFICATE_2.get(0).getId();
        Mockito.when(mockTagRepository.findById(tagId1)).thenReturn(Optional.of(TAGS_FOR_CERTIFICATE_2.get(0)));
        Mockito.when(mockDtoMapper.dtoToGiftCertificate(testDto)).thenReturn(testGiftCertificate);
        GiftCertificateDto giftCertificateDto = service.addCertificate(testDto);

        Assertions.assertEquals(testGiftCertificate.getName(), giftCertificateDto.getName());
        Assertions.assertEquals(TAGS_FOR_CERTIFICATE_2.get(0), giftCertificateDto.getTags().get(0));
        Assertions.assertEquals(testGiftCertificate.getId(), giftCertificateDto.getId());
    }

}
