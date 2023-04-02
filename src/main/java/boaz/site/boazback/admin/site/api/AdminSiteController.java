package boaz.site.boazback.admin.site.api;

import boaz.site.boazback.admin.site.application.AdminSiteService;
import boaz.site.boazback.admin.site.dto.SiteDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/site")
@Slf4j
@RequiredArgsConstructor
public class AdminSiteController {

    private final AdminSiteService adminSiteService;

    @PostMapping
    public boolean updateSiteInformation(@RequestBody SiteDto.Request siteRequest){
        log.info("updateSiteInformation api start");
        boolean result = adminSiteService.updateSiteInformation(siteRequest);
        log.info("updateSiteInformation api end");
        return result;
    }

}
