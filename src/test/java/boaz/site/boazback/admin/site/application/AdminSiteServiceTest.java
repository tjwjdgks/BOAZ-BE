package boaz.site.boazback.admin.site.application;

import boaz.site.boazback.admin.site.dto.SiteDto;
import boaz.site.boazback.user.application.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminSiteServiceTest {

    private AdminSiteService adminSiteService;

    @Mock
    private SiteRepository siteRepository;


    @BeforeEach
    void setUp() {
        adminSiteService = new AdminSiteServiceImpl(siteRepository);
    }

    @Test
    void updateSiteData(){
        SiteDto.Request siteRequest = new SiteDto.Request("www.youtube.com", "asdfasdf");
        boolean result = adminSiteService.updateSiteInformation(siteRequest);
        verify(siteRepository).save(any());
        assertThat(result).isTrue();
    }


}
