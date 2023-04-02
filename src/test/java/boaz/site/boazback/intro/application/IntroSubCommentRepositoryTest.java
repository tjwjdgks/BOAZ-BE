package boaz.site.boazback.intro.application;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.intro.domain.Intro;
import boaz.site.boazback.intro.domain.IntroSubComment;
import boaz.site.boazback.user.domain.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class IntroSubCommentRepositoryTest extends BaseDataJpaTest {

    @Test
    void findByIntroId() {
        List<IntroSubComment> introList = introSubCommentRepository.findByIntroId(1L);
        assertThat(introList).hasSize(2);
    }

    @Test
    void saveSubComment(){
        Optional<User> user = userRepository.findById(UUID.fromString("df8b21f0-c2d6-11ec-a6d6-0800200c9a66"));
        if (user.isEmpty()) {
            throw new IllegalStateException("user not found");
        }

        Optional<Intro> intro = introRepository.findById(1L);
        if (intro.isEmpty()) {
            throw new IllegalStateException("intro not found");
        }

        IntroSubComment introSubComment = IntroSubComment.builder()
                .contents("daa")
                .user(user.get())
                .intro(intro.get())
                .build();
        introSubCommentRepository.save(introSubComment);
        Optional<Intro> intro2 = introRepository.findById(1L);
        if (intro2.isEmpty()) {
            throw new IllegalStateException("error");
        }

        assertThat(intro2.get().getSubComments()).hasSize(3);
    }




}
