package boaz.site.boazback.admin.site.application;

import boaz.site.boazback.admin.site.dto.SiteDto;
import boaz.site.boazback.user.application.SiteRepository;
import boaz.site.boazback.admin.site.domain.Site;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminSiteServiceImpl implements AdminSiteService {

    private final SiteRepository siteRepository;

    @Override
    public boolean updateSiteInformation(SiteDto.Request siteRequest) {
        log.info("updateSiteInformation service start");
        Site site = new Site(siteRequest);
        siteRepository.save(site);
        log.info("updateSiteInformation service end");
        return true;
    }

}
