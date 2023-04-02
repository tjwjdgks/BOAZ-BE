package boaz.site.boazback.admin.user.application;


import boaz.site.boazback.user.dto.UserInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminUserService {

    List<UserInfo.SimpleLevel> getUserList(Pageable pageable);
}
