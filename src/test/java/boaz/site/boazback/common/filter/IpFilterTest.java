package boaz.site.boazback.common.filter;

import boaz.site.boazback.BaseControllerTest;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class IpFilterTest extends BaseControllerTest {

    List<String> locales = new ArrayList<>();
    Map<String, String> names = new HashMap<>();

    @BeforeEach
    void setUp() {
        setUpToken();
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .addFilters(new FilterChainExceptionHandler(objectMapper))
                .addFilters(new IpAuthenticationFilter(databaseReader,httpReqResUtils))
                .alwaysDo(print())
                .build();
    }

    private static void updateCountry(List<String> locales, Map<String, String> names, String name) {
        locales.add("en");
        names.put("en", name);
    }

    @Test
    void 국내ip체크()throws Exception {
        setupIp("South Korea", "KR");
        given(databaseReader.country(any())).willReturn(countryResponse);
        mockMvc.perform(get("/auth/check")
                        .cookie(accessToken)
                        .cookie(refreshToken)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200));
    }

    @Test
    void 해외ip체크()throws Exception {
        setupIp("United States", "US");
        given(databaseReader.country(any())).willReturn(countryResponse);
        mockMvc.perform(get("/auth/check")
                        .cookie(accessToken)
                        .cookie(refreshToken)
                ).andDo(print())
                .andExpect(status().isForbidden());
    }

    private void setupIp(String country, String countryCode) {
        updateCountry(locales, names, country);
        korea = new Country(locales,null,1835841, countryCode, names);
        countryResponse = new CountryResponse(null,korea,null,null,null,null);
    }


}
