package boaz.site.boazback.question.application;

import boaz.site.boazback.BaseDataJpaTest;
import boaz.site.boazback.question.domain.Question;
import boaz.site.boazback.question.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


class QuestionRepositoryTest extends BaseDataJpaTest {

    QuestionDto questionDto;
    Question question;
    @BeforeEach
    void setup(){
        questionDto = new QuestionDto("bob","example@naver.com","test","test");
        question = questionDto.transForm();
    }

    @Test
    @DisplayName("저장하기")
    void save(){
        Question saveQuestion = questionRepository.save(question);
        assertThat(saveQuestion.getName()).isEqualTo(question.getName());
    }


    @Test
    @DisplayName("전체불러오기")
    void selectAll(){
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList.size()).isEqualTo(3);
        assertThat(questionList.get(0).getName()).isEqualTo("bob");
    }


    @Test
    @DisplayName("데이터 불러오기")
    void findDataById(){
        Optional<Question> findQuestion = questionRepository.findById(1L);
        assertThat(findQuestion).isNotEmpty();
        assertThat(findQuestion.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("데이터를 못불러온다면")
    void findDataByIdNull(){
        Optional<Question> findQuestion = questionRepository.findById(5L);
        assertThat(findQuestion).isEmpty();
    }
}

