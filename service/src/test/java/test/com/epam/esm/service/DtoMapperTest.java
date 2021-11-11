package test.com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.service.DtoMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoMapperTest {

    private static final LocalDateTime DATE_TIME = LocalDateTime.of(LocalDate.of(2005, 10, 13), LocalTime.NOON);
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final int ID = 1;
    private static final double DURATION = 12.5;
    private static final BigDecimal PRICE = BigDecimal.TEN;
    private static final List<Tag> TAGS = new ArrayList<>(Arrays.asList(new Tag(1, "asd"), new Tag(2, "sas")));
    private static final GiftCertificate TEST_CERTIFICATE = new GiftCertificate(ID, NAME, DESCRIPTION, PRICE, DURATION, DATE_TIME, DATE_TIME, TAGS);
    private static final GiftCertificateDto TEST_DTO = new GiftCertificateDto(ID, NAME, DESCRIPTION, PRICE, DURATION, DATE_TIME, DATE_TIME, TAGS);
    private final DtoMapper dtoMapper = new DtoMapper();

    @Test
    public void testGiftCertificateToDto() {
        GiftCertificateDto actual = dtoMapper.giftCertificateToDto(TEST_CERTIFICATE);
        assertEquals(ID, actual.getId());
        assertEquals(NAME, actual.getName());
        assertEquals(PRICE, actual.getPrice());
        assertEquals(DURATION, actual.getDuration());
        assertEquals(DESCRIPTION, actual.getDescription());
        assertEquals(TAGS, actual.getTags());
        assertEquals(DATE_TIME, actual.getCreateDate());
        assertEquals(DATE_TIME, actual.getLastUpdateDate());
    }

    @Test
    public void testDtoToGiftCertificate() {
        GiftCertificate actual = dtoMapper.dtoToGiftCertificate(TEST_DTO);
        assertEquals(ID, actual.getId());
        assertEquals(NAME, actual.getName());
        assertEquals(PRICE, actual.getPrice());
        assertEquals(DURATION, actual.getDuration());
        assertEquals(DESCRIPTION, actual.getDescription());
        assertEquals(TAGS, actual.getTags());
        assertEquals(DATE_TIME, actual.getCreateDate());
        assertEquals(DATE_TIME, actual.getLastUpdateDate());
    }

}
