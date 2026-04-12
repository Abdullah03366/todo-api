package example.demo.todo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI todoApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Demo TODO API")
                        .description("Interactieve documentatie voor TODO endpoints")
                        .version("v1"));
    }
}

