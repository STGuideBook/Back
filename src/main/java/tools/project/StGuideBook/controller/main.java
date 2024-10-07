package tools.project.StGuideBook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class main {

    @GetMapping("/main")
    public String index() {
        return "main"; // index.html 파일을 반환
    }
}
