package boaz.site.boazback.user.application;


import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.user.domain.User;
import boaz.site.boazback.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class UserRepositoryTest extends BaseDataJpaTest {

    private UserDto fakeUserDto;

    @BeforeEach
    void setUp() {
        fakeUserDto = UserDto.builder()
                .email("example@naver.com")
                .password("hello01")
                .name("bob")
                .section(3)
                .year("16")
                .authenticationCode("boaz")
                .birthDate("0000-00-00")
                .build();
    }


    @Test
    void saveUser() {
        User saveUser = new User(fakeUserDto);
        User savedUser = userRepository.save(saveUser);
        assertThat(savedUser.getEditName()).isEqualTo(savedUser.getEditName());
    }

    @Test
    void findAll(){
        List<User> result = userRepository.findAll();
        System.out.println("result = " + result);
    }



    @Test
    void findById() {
        Optional<User> findUser = userRepository.findById(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        System.out.println("findUser = " + findUser);
        assertThat(findUser).isNotEmpty();
    }


    @Test
    void deleteById() {
        userRepository.deleteById(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        Optional<User> findUser = userRepository.findById(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        assertThat(findUser).isEmpty();
    }



    @Test
    void findByEmail(){
        String email = "gmail@example.com";
        Optional<User> findUser = userRepository.findByEmail(email);
        assertThat(findUser).isNotEmpty();
        assertThat(findUser.get().getEditName()).isEqualTo("16_엔지니어링_bob");
    }

    @Test
    void findAllPage(){
        Pageable pageable = PageRequest.of(0, 5,
                Sort.by(Sort.Order.desc("createdDate") ,Sort.Order.asc("year"))
        );
        System.out.println("pageable = " + pageable);
        Page<User> userPage= userRepository.findAll(pageable);
        System.out.println("userPage = " + userPage.getContent());
    }




}
