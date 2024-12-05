package withbeetravel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://192.168.0.5:3000", "http://54.180.164.254:8080","http://54.180.164.254",
                        "http://localhost:3001", "https://withbee-travel.vercel.app", "https://www.withbee.site", "https://www.withbee.shop",
                        "http://localhost:80", "http://localhost:443", "http://localhost")
                .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);
    }
}