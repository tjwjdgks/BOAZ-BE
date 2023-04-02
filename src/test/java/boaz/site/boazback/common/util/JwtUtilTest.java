package boaz.site.boazback.common.util;

import boaz.site.boazback.common.domain.TokenType;
import boaz.site.boazback.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class JwtUtilTest {


    private JwtUtil jwtUtil;

    @BeforeEach
    void setup(){
        jwtUtil = new JwtUtil();
    }

    @Test
    void infiniteToken(){
        User user = User.jwtBuilder()
                .id(UUID.fromString("d66488bb-47f4-43ba-847b-954240e9aaac"))
                .birthDate("2002-05-20")
                .email("test@naver.com")
                .name("테스트계정")
                .section(1)
                .year("17기")
                .createdDate(changeStringToDate("2022-05-21"))
                .modifiedDate(changeStringToDate("2022-05-21"))
                .get();
        String accessToken = jwtUtil.generateToken(user, TokenType.INFINITE_ACCESS);
        System.out.println("accessTokenStr = " + accessToken);
        String secretToken = jwtUtil.generateToken(user, TokenType.INFINITE_REFRESH);
        System.out.println("secretToken = " + secretToken);
    }

    private Date changeStringToDate(String birthDate) {
        try {
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            return transFormat.parse(birthDate);
        } catch (Exception e) {
            return null;
        }
    }
}
