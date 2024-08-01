package kr.bit.animalinc.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class LoginFail implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.info("Login Fail!");

        Gson gson = new Gson();

        String str=gson.toJson(Map.of("error","LoginFail"));
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter= response.getWriter();
        printWriter.println(str);
        printWriter.close();
    }
}
