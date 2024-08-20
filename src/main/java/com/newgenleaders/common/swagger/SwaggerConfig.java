package com.newgenleaders.common.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("com.newgenleaders.modules")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Newgen Leaders")
                        .description("Este projeto uma api desenvolvida para um blog que permite que os usuários publiquem artigos e acessem diversas funcionalidades adicionais. O projeto foi criado com o objetivo de oferecer uma plataforma robusta e segura para a publicação e leitura de conteúdo.")
                        .version("1.0.0")
                );
    }

}
