package kr.bit.animalinc.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.bit.animalinc.entity.user.Users;
import kr.bit.animalinc.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final CertificationGenerator generator;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, String> verificationCodes = new HashMap<>();

    public void sendEmailForCertification(String email) throws NoSuchAlgorithmException, MessagingException {

        String certificationPassword = generator.createCertificationNumber();
        String link = "<a href='http://localhost:3600/login'>로그인 링크로 돌아가기</a>";

        String content = String.format("<br> 임시비밀번호: %s <br><br> %s <br> 로그인 후 마이페이지에서 비밀번호를 수정해주세요.",
                certificationPassword,
                link);

        // 비밀번호 해싱
        String userPw = passwordEncoder.encode(certificationPassword);

        // DB에 비밀번호 저장
        Optional<Users> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setUserPw(userPw); // 비밀번호 설정
            userRepository.save(user); // 변경된 비밀번호를 DB 저장
        }

        // 이메일 전송
        sendEmail(email, content, "kwoong1923@gmail.com");
    }

    private void sendEmail(String email, String content, String fromEmail) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setTo(email);
        helper.setFrom(fromEmail);
        helper.setSubject("애니멀 주식회사");
        helper.setText(content, true);

        mailSender.send(mimeMessage);
    }

    public String sendVerificationEmail(String email) {
        String verificationCode = generateVerificationCode();
        String subject = "애니멀 주식회사에서 당신의 이메일을 인증하려고 합니다.";
        String content = "이메일 인증번호 : "+ verificationCode;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setFrom("kwoong1923@gmail.com");
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            log.info("Verification email sent to {}", email);

            verificationCodes.put(email, verificationCode);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to {}", email, e);
            return null;
        }
        return verificationCode;
    }

    public boolean verifyEmailCode(String email, String code) {
        String savedCode = verificationCodes.get(email);
        return code != null && code.equals(savedCode);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}
