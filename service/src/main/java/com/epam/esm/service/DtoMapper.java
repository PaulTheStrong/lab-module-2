package com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class DtoMapper {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public GiftCertificateDto mapCertificateToDto(GiftCertificate certificate) {

        GiftCertificateDto result = new GiftCertificateDto();
        result.setId(certificate.getId());
        result.setName(certificate.getName());
        result.setDescription(certificate.getDescription());
        result.setPrice(certificate.getPrice());
        result.setDuration(certificate.getDuration());
        result.setCreateDate(certificate.getCreateDate().format(formatter));
        result.setLastUpdateDate(certificate.getLastUpdateDate().format(formatter));

        return result;
    }

}
