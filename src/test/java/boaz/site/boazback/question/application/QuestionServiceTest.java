package boaz.site.boazback.question.application;

import boaz.site.boazback.common.util.SlackUtil;
import boaz.site.boazback.question.domain.Question;
import boaz.site.boazback.question.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {


    @Mock
    private QuestionRepository questionRepository;
    private QuestionService questionService;

    @Mock
    private SlackUtil slackUtil;


    QuestionDto questionDto;
    Question question;

    @BeforeEach
    void setup() {
        questionService = new QuestionServiceImpl(questionRepository, slackUtil);
        questionDto = new QuestionDto("bob", "example@naver.com", "test", "test");
        question = questionDto.transForm();
    }


    @Test
    @DisplayName("문의를 보냅니다.")
    void 문의하기() {
        given(questionRepository.save(any())).willReturn(question);
        Question sendQuestion = questionService.sendInfo(questionDto);
        assertThat(sendQuestion).isInstanceOf(Question.class);
    }

    @Test
    @DisplayName("제목에 null이 들어간다면?")
    void 제목에null이있어요() {
        given(questionRepository.save(any())).willReturn(question);
        Question sendQuestion = questionService.sendInfo(questionDto);
        assertThat(sendQuestion).isNotNull();
    }

    @Test
    @DisplayName("")
    void 데이터전체조회하기() {
        given(questionRepository.findAll()).willReturn(new ArrayList<Question>());
        List<Question> questions = questionService.selectAll();
        assertThat(questions.size()).isZero();
    }

    @Test
    @DisplayName("세부 내용 조회하기")
    void 세부내용데이터조회하기() {
        given(questionRepository.findById(anyLong())).willReturn(Optional.of(question));
        Question findQuestion = questionService.findQuestion(1L);
        assertThat(findQuestion.getName()).isEqualTo(question.getName());
    }

    @Test
    @DisplayName("세부 내용이 없을 때")
    void 세부내용데이터조회하기2() {
        given(questionRepository.findById(anyLong())).willReturn(Optional.empty());
        Throwable error = assertThrows(IllegalStateException.class, () -> questionService.findQuestion(5L));
        assertThat(error.getMessage()).isEqualTo("question not exist");
    }

}
