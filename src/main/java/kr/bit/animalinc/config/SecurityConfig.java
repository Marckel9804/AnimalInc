package kr.bit.animalinc.config;

import kr.bit.animalinc.security.filter.JWTFilter;
import kr.bit.animalinc.util.JWTUtil;
import kr.bit.animalinc.util.RedisTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

    private final JWTUtil jwtUtil;
    private final RedisTokenService redisTokenService;  // RedisTokenService 추가

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
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization")); // 추가
        corsConfiguration.setAllowCredentials(false);

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

        httpSecurity.authorizeHttpRequests(authorize -> {
            authorize
                    .requestMatchers("/api/user/**", "/api/auth/**", "/api/oauth2/**").permitAll()
                    .anyRequest().authenticated();
        });

        //jwt검증이 우선이고 그 다음 사용자 정보 기반으로 한 인증토큰 추가해라
        httpSecurity.addFilterBefore(new JWTFilter(jwtUtil, redisTokenService), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
