package boaz.site.boazback.user.application;

import boaz.site.boazback.user.dto.FindEmailDto;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.UserDto;
import boaz.site.boazback.user.dto.UserLogin;

import java.util.Optional;
import java.util.UUID;


public interface UserService {

    User registerUser(UserDto userDto);

    void deleteUser(UUID uid);

    User loginUser(UserLogin userLogin);

    Optional<User> findUser(UUID uid);

    boolean isExistUser(UUID uid);

    void reissueUserPasswordByEmail(FindEmailDto findEmailDto);

    void certificationSignupConfirm(String certification, String encodedEmail);

    void certificationReissuePasswordConfirm(String certification,String newPassword);

    void resendJoinConfirmMail(String email);
}
