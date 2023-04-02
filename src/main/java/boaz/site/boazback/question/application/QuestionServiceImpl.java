package boaz.site.boazback.question.application;

import boaz.site.boazback.common.util.SlackUtil;
import boaz.site.boazback.question.domain.Question;
import boaz.site.boazback.question.dto.QuestionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final SlackUtil slackUtil;

    @Override
    @Transactional
    public Question sendInfo(QuestionDto questionDto) {
        log.info("sendinfo service start");
        Question question = questionDto.transForm();
        Question saveQuestion = questionRepository.save(question);
        slackUtil.sendQuestionMessage(saveQuestion);
        log.info("sendinfo service end");
        return saveQuestion;
    }

    @Override
    public List<Question> selectAll() {
        return questionRepository.findAll();
    }

    @Override
    public Question findQuestion(Long id) {
        log.info("findQuestion start");
        Optional<Question> findQuestion = questionRepository.findById(id);
        if (findQuestion.isEmpty()) {
            throw new IllegalStateException("question not exist");
        }
        log.info("findQuestion end");
        return findQuestion.get();
    }
}
