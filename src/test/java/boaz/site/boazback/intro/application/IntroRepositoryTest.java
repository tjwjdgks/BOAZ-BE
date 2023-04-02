package boaz.site.boazback.intro.application;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.intro.domain.Intro;
import boaz.site.boazback.intro.dto.IntroInfo;
import boaz.site.boazback.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class IntroRepositoryTest extends BaseDataJpaTest {

    @Test
    void introSave() {
        Optional<User> user = userRepository.findById(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        Intro intro = Intro.builder()
                .contents("12")
                .erase(false)
                .introSubComments(new ArrayList<>())
                .user(user.get())
                .build();
        Intro savedIntro= introRepository.save(intro);
        assertThat(savedIntro.isErase()).isFalse();
    }

    @Test
    void findIntro(){
        Optional<Intro> findIntro = introRepository.findById(1L);
        if (findIntro.isEmpty()) {
            throw new IllegalStateException("intro not exist");
        }
        System.out.println("findIntro = " + findIntro.get());
        IntroInfo introInfo = new IntroInfo(findIntro.get());
        System.out.println("introInfo = " + introInfo);
        assertThat(findIntro.get().getSubComments()).hasSize(2);
    }

    @Test
    void pagination(){
        Pageable pageable = PageRequest.of(0, 1);
        Page<Intro> result = introRepository.findAll(pageable);
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void count(){
        Long size = introRepository.getCount();
        assertThat(size).isEqualTo(1);
    }

}
