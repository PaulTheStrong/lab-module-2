package com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class DtoMapper {

    public GiftCertificateDto mapCertificateToDto(GiftCertificate certificate) {

        GiftCertificateDto result = new GiftCertificateDto();
        result.setId(certificate.getId());
        result.setName(certificate.getName());
        result.setDescription(certificate.getDescription());
        result.setPrice(certificate.getPrice());
        result.setDuration(certificate.getDuration());
        result.setCreateDate(certificate.getCreateDate());
        result.setLastUpdateDate(certificate.getLastUpdateDate());

        return result;
    }

}
