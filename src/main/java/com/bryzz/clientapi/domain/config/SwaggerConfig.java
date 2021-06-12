package com.bryzz.clientapi.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Arrays;

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean
    public Docket appClientAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .globalOperationParameters(
                        Arrays.asList(new ParameterBuilder()
                                .name("Authorization")
                                .defaultValue("Bearer ")
                                .description("Security token")
                                .modelRef(new ModelRef("String"))
                                .parameterType("header")
                                .required(true)
                                .build()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bryzz.clientapi"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {
        return new ApiInfoBuilder()
                .description("Client Service to manage applications")
                .title("Client API")
                .version("v1.0.0")
                .contact(new Contact("Domou Brice", "github.com/DBryzz", "domoubrice@gmail.com"))
                .build();
    }

}
