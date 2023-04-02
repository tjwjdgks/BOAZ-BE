package boaz.site.boazback.admin.user.controller;

import boaz.site.boazback.admin.user.application.AdminUserService;
import boaz.site.boazback.user.domain.Role;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.UserDto;
import boaz.site.boazback.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Profile("!prd")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/roles")
    public String getRoles(){
        return Role.getValues();
    }

    @GetMapping
    public List<UserInfo.SimpleLevel> getUserList(@SortDefault(value = "createdDate", direction = Sort.Direction.DESC)
                                                                        Pageable pageable){
        log.info("getUserList api start");
        List<UserInfo.SimpleLevel> userList = adminUserService.getUserList(pageable);
        log.info("getUserList api end");
        return userList;
    }

}
