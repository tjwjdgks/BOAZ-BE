package boaz.site.boazback.admin.user.application;

import boaz.site.boazback.user.application.UserRepository;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    private  final UserRepository userRepository;


    @Override
    public List<UserInfo.SimpleLevel> getUserList(Pageable pageable) {
        log.info("getUserList service start");
        Page<User> userInfoPage = userRepository.findAll(pageable);
        List<UserInfo.SimpleLevel> userList= userInfoPage.getContent().stream().map(UserInfo.SimpleLevel::new).collect(Collectors.toList());
        log.info("getUserList service end");
        return userList;
    }
}
