package withbeetravel.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import withbeetravel.jwt.JwtUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtUtil jwtUtil;

    protected String accessToken;

    @BeforeEach
    void setUp() {
        accessToken = jwtUtil.generateAccessToken(String.valueOf(2L));
    }

    @BeforeEach
    void setPort() {
        // 랜덤으로 배정받은 포트 설정
        RestAssured.port = port;
    }
}

