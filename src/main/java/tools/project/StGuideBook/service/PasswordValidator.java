package tools.project.StGuideBook.service;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidator { // 패스워드 작성 규칙

    public void validate(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 최소한 8글자 이상이어야 합니다.");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("적어도 한 글자 이상의 영문이 들어가야 합니다.");
        }

        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("적어도 하나 이상의 숫자가 들어가야 합니다.");
        }

        if (!password.matches(".*[!@#$%^&*()].*")) {
            throw new IllegalArgumentException("적어도 하나 이상의 특수문자가 들어가야 합니다.");
        }
    }
}
