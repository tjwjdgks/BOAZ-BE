package boaz.site.boazback.admin.site.application;

import boaz.site.boazback.admin.site.dto.SiteDto;

public interface AdminSiteService {
    boolean updateSiteInformation(SiteDto.Request siteRequest);
}
