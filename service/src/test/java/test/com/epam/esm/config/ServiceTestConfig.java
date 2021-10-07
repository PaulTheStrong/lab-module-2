package test.com.epam.esm.config;

import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.DtoMapper;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceTestConfig {

    @Bean
    public TagRepository mockTagRepository() {
        return Mockito.mock(TagRepository.class);
    }

    @Bean
    public GiftCertificateRepository mockGiftCertificateRepository() {
        return Mockito.mock(GiftCertificateRepository.class);
    }

    @Bean
    public DtoMapper mockDtoMapper() {
        return Mockito.mock(DtoMapper.class);
    }

    @Bean
    public GiftCertificateService giftCertificateServiceTestBean(
            GiftCertificateRepository giftCertificateRepository,
            TagRepository tagRepository,
            DtoMapper dtoMapper) {
        return new GiftCertificateService(giftCertificateRepository, tagRepository, dtoMapper);
    }

    @Bean
    public TagService tagServiceTestBean(TagRepository tagRepository) {
        return new TagService(tagRepository);
    }

}
