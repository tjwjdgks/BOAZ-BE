package boaz.site.boazback.common.util;

import boaz.site.boazback.common.exception.CertificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


class CertificationUtilTest {

    CertificationUtil certificationUtil;

    @BeforeEach
    void setUp() {
        certificationUtil = new CertificationUtil();
    }

    @Test
    void createCertificationCode() {
        String result = certificationUtil.createCertificationCode();
        assertThat(result).hasSize(5);
    }

    @Test
    void checkExpiredCertificationCode() {
        certificationUtil.checkExpiredCertificationCode(LocalDateTime.now().plusHours(1));
    }

    @Test
    void checkExpiredCertificationCode_expired(){
        Throwable result = assertThrows(CertificationException.class, () -> certificationUtil.checkExpiredCertificationCode(LocalDateTime.now().minusHours(1)));
        assertThat(result.getMessage()).isEqualTo("CertificationCode is expired");
    }

    @Test
    void checkCertificationCode() {
        certificationUtil.checkCertificationCode("test","test");
    }

    @Test
    void checkCertificationCode_not_same(){
        Throwable result = assertThrows(CertificationException.class, () -> certificationUtil.checkCertificationCode("test","test2"));
        assertThat(result.getMessage()).isEqualTo("CertificationCode is not same");
    }

}
