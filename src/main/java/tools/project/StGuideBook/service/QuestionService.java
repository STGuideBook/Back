package tools.project.StGuideBook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.domain.Question;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.exception.DataNotFoundException;
import tools.project.StGuideBook.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if(question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser user) {
        Question question = new Question(subject, content, LocalDateTime.now(), user);
        this.questionRepository.save(question);
    }
}
