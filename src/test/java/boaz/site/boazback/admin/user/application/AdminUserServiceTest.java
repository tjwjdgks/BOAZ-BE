package boaz.site.boazback.admin.user.application;

import boaz.site.boazback.user.application.UserRepository;
import boaz.site.boazback.user.domain.Role;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    private AdminUserService adminUserService;

    @Mock
    private UserRepository userRepository;
    User user;
    @BeforeEach
    void setUp() {
        adminUserService = new AdminUserServiceImpl(userRepository);
        user = User.builder()
                .id(UUID.randomUUID())
                .year("16")
                .section(3)
                .name("test")
                .build();
    }

    @Test
    void getUserList(){
        Pageable pageable = PageRequest.of(0, 5);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        Page<User> data = new PageImpl<>(userList);
        given(userRepository.findAll((Pageable) any())).willReturn(data);
        List<UserInfo.SimpleLevel> userSimpleInfoList = adminUserService.getUserList(pageable);
        assertThat(userSimpleInfoList).hasSize(1);
        assertThat(userSimpleInfoList.get(0).getRole()).isEqualTo(Role.MEMBER);
    }


}
