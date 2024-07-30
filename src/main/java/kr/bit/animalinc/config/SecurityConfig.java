package kr.bit.animalinc.config;

import kr.bit.animalinc.handler.LoginFail;
import kr.bit.animalinc.handler.LoginSuccess;
import kr.bit.animalinc.security.filter.JWTFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
        //모든 도메인에서 요청을 허용

        //허용되는 Http메서드 설정
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","HEAD"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization","Cache-Control", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("security!!!!");

        httpSecurity.csrf(config -> config.disable()); //csrf 비활성화

        httpSecurity.cors(httpSecurityCorsConfigurer -> {  //cors설정 적용
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        httpSecurity.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.formLogin(config ->{
            config.loginPage("/api/user/login");
            config.successHandler(new LoginSuccess());
            config.failureHandler(new LoginFail());
        });

        //jwt검증이 우선이고 그 다음 사용자 정보 기반으로 한 인증토큰 추가해라
        httpSecurity.addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
