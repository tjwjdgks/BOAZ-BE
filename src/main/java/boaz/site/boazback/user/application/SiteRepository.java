package boaz.site.boazback.user.application;

import boaz.site.boazback.admin.site.domain.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends JpaRepository<Site,Long> {
}
