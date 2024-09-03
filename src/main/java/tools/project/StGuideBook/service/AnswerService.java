package tools.project.StGuideBook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.project.StGuideBook.domain.Answer;
import tools.project.StGuideBook.domain.Question;
import tools.project.StGuideBook.domain.SiteUser;
import tools.project.StGuideBook.repository.AnswerRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public void create(Question question, String content, SiteUser author) {
        Answer answer = new Answer(content, LocalDateTime.now(), question, author);
        this.answerRepository.save(answer);
    }
}
