package boaz.site.boazback.question.application;

import boaz.site.boazback.question.domain.Question;
import boaz.site.boazback.question.dto.QuestionDto;

import java.util.List;

public interface QuestionService {
    Question sendInfo(QuestionDto questionDto);

    List<Question> selectAll();

    Question findQuestion(Long id);
}
