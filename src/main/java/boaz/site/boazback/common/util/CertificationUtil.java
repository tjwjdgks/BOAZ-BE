package boaz.site.boazback.common.util;

import boaz.site.boazback.common.domain.TracingFunction;
import boaz.site.boazback.common.exception.CertificationException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class CertificationUtil {
    private Random rnd =new Random();

    @TracingFunction
    public String createCertificationCode(){
        StringBuilder buf =new StringBuilder();
        for(int i=0;i<5;i++){
            if(rnd.nextBoolean()){
                buf.append((char)((rnd.nextInt(26))+97));
            }else{
                buf.append((rnd.nextInt(10)));
            }
        }
        return buf.toString();
    }
    @TracingFunction
    public void checkExpiredCertificationCode(LocalDateTime expiredTime){
        LocalDateTime today = LocalDateTime.now();
        if(!today.isBefore(expiredTime)){
         throw CertificationException.CERTIFICATION_EXPIRATION;
        }
    }
    @TracingFunction
    public void checkCertificationCode(String inputCode, String certificationCode){
        if (!inputCode.equals(certificationCode)) {
            throw CertificationException.CERTIFICATION_NOT_SAME;
        }
    }

}
