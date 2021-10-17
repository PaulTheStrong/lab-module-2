package test.com.epam.esm.service;

import com.epam.esm.data.OrderDto;
import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.api.GiftCertificateRepository;
import com.epam.esm.repository.api.OrderRepository;
import com.epam.esm.repository.api.UserRepository;
import com.epam.esm.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PurchaseServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    private static final GiftCertificate AFFORDABLE_CERTIFICATE = new GiftCertificate(1, "certificate", "descr", new BigDecimal(100), 10.0, null, null);
    private static final User TEST_USER = new User(1, "user", new BigDecimal(200), null);
    private static final Order TEST_ORDER = new Order(1, AFFORDABLE_CERTIFICATE.getPrice(), null, TEST_USER, AFFORDABLE_CERTIFICATE);
    private static final GiftCertificate NOT_AFFORDABLE_CERTIFICATE = new GiftCertificate(1, "certificate", "descr", new BigDecimal(2000), 10.0, null, null);

    @Test
    public void testPurchaseCertificateShouldThrowExceptionWhenBalanceIsLessThanPrice() {
        when(userRepository.findById(1)).thenReturn(Optional.of(TEST_USER));
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.of(AFFORDABLE_CERTIFICATE));
        when(orderRepository.save(any())).thenReturn(Optional.of(TEST_ORDER));

        OrderDto expected = new OrderDto(TEST_ORDER);
        OrderDto actual = purchaseService.purchaseCertificate(1, 1);

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getCertificateId(), actual.getCertificateId());
        assertEquals(expected.getUserId(), actual.getUserId());
    }

    @Test
    public void testPurchaseCertificateShouldThrowExceptionWhenBalanceNotEnough() {
        when(userRepository.findById(1)).thenReturn(Optional.of(TEST_USER));
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.of(NOT_AFFORDABLE_CERTIFICATE));

        assertThrows(ServiceException.class, () -> purchaseService.purchaseCertificate(1, 1));
    }

    @Test
    public void testPurchaseCertificateShouldThrowExceptionWhenUserNotFound() {
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.of(NOT_AFFORDABLE_CERTIFICATE));
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> purchaseService.purchaseCertificate(1, 1));
    }

    @Test
    public void testPurchaseCertificateShouldThrowExceptionWhenCertificateNotFound() {
        when(giftCertificateRepository.findById(1)).thenReturn(Optional.empty());
        when(userRepository.findById(1)).thenReturn(Optional.of(TEST_USER));

        assertThrows(ServiceException.class, () -> purchaseService.purchaseCertificate(1, 1));
    }

}
