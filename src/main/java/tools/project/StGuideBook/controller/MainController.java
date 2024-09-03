package tools.project.StGuideBook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/main")
    public String Main() {
        return "main";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/main";
    }

}
