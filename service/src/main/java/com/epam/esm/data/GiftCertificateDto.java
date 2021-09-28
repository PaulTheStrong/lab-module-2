package com.epam.esm.data;

import com.epam.esm.entities.Tag;
import com.epam.esm.validator.DtoTag;
import com.epam.esm.validator.PatchDto;
import com.epam.esm.validator.SaveDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.epam.esm.exception.ExceptionCodes.CERTIFICATE_DESCRIPTION_LENGTH_CONSTRAINT;
import static com.epam.esm.exception.ExceptionCodes.CERTIFICATE_DURATION_MUST_BE_POSITIVE;
import static com.epam.esm.exception.ExceptionCodes.CERTIFICATE_DURATION_MUST_BE_SPECIFIED;
import static com.epam.esm.exception.ExceptionCodes.CERTIFICATE_NAME_LENGTH_CONSTRAINT;
import static com.epam.esm.exception.ExceptionCodes.CERTIFICATE_NAME_MUST_BE_SPECIFIED;
import static com.epam.esm.exception.ExceptionCodes.CERTIFICATE_PRICE_MUST_BE_POSITIVE;
import static com.epam.esm.exception.ExceptionCodes.CERTIFICATE_PRICE_MUST_BE_SPECIFIED;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateDto {

    private int id;

    @NotNull(groups = SaveDto.class, message = CERTIFICATE_NAME_MUST_BE_SPECIFIED)
    @Size(min = 1, max = 20, groups = {SaveDto.class, PatchDto.class},
            message = CERTIFICATE_NAME_LENGTH_CONSTRAINT)
    private String name;

    @Size(max = 255, groups = {SaveDto.class, PatchDto.class},
    message = CERTIFICATE_DESCRIPTION_LENGTH_CONSTRAINT)
    private String description;

    @Min(value = 0, groups = {SaveDto.class, PatchDto.class}, message = CERTIFICATE_PRICE_MUST_BE_POSITIVE)
    @NotNull(groups = SaveDto.class, message = CERTIFICATE_PRICE_MUST_BE_SPECIFIED)
    private BigDecimal price;

    @NotNull(groups = SaveDto.class, message = CERTIFICATE_DURATION_MUST_BE_SPECIFIED)
    @Min(value = 0, groups = {SaveDto.class, PatchDto.class}, message = CERTIFICATE_DURATION_MUST_BE_POSITIVE)
    private Double duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastUpdateDate;

    private List<@DtoTag(groups = {SaveDto.class, PatchDto.class}) Tag> tags;
}
