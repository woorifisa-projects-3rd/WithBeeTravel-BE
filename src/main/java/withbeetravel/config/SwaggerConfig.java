package withbeetravel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 설정 파일을 읽기 위한 annotation
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        // Swagger에서 보여줄 API 정보 설정
        Info info = new Info()
                .version("1.0.0") // 서비스 버전
                .title("WithBee Travel") // 타이틀
                .description("WithBee Travel🐝🛫 Project API"); // 설명

        return new OpenAPI().info(info);
    }
}
