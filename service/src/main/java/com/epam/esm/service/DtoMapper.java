package com.epam.esm.service;

import com.epam.esm.data.GiftCertificateDto;
import com.epam.esm.entities.GiftCertificate;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public GiftCertificateDto giftCertificateToDto(GiftCertificate certificate) {

        GiftCertificateDto result = new GiftCertificateDto();
        result.setId(certificate.getId());
        result.setName(certificate.getName());
        result.setDescription(certificate.getDescription());
        result.setPrice(certificate.getPrice());
        result.setDuration(certificate.getDuration());
        result.setCreateDate(certificate.getCreateDate());
        result.setLastUpdateDate(certificate.getLastUpdateDate());
        result.setTags(certificate.getTags());
        return result;
    }

    public GiftCertificate dtoToGiftCertificate(GiftCertificateDto dto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(dto.getId());
        giftCertificate.setName(dto.getName());
        giftCertificate.setDescription(dto.getDescription());
        giftCertificate.setPrice(dto.getPrice());
        giftCertificate.setDuration(dto.getDuration());
        giftCertificate.setCreateDate(dto.getCreateDate());
        giftCertificate.setLastUpdateDate(dto.getLastUpdateDate());
        giftCertificate.setTags(dto.getTags());
        return giftCertificate;
    }
}
