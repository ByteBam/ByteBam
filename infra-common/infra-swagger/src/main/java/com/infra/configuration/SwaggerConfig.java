package com.infra.configuration;


import com.infra.modle.SwaggerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Optional;

/**
 * swagger 配置
 */
@Configuration
@ConditionalOnProperty(name = "show.swagger", havingValue = "true", matchIfMissing = false)
public class SwaggerConfig {

    @Autowired
    private SwaggerTemplate swaggerTemplate;

    @Bean
    public Docket createApi() {
        return buildDocket("user");
    }

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage(swaggerTemplate.getBasePackage())).paths(PathSelectors.any()).build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(swaggerTemplate.getTitle()).contact(new Contact(swaggerTemplate.getContactName(), swaggerTemplate.getContactUrl(), swaggerTemplate.getEmail())).version(swaggerTemplate.getVersion()).description(swaggerTemplate.getDescription()).build();
    }

    public Docket buildDocket(String groupName) {

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).groupName(groupName).select().apis(input -> {
            if (input.isAnnotatedWith(ApiVersion.class)) {
                Optional<ApiVersion> versionOptional = input.findAnnotation(ApiVersion.class);
                if (versionOptional.isPresent()) {
                    ApiVersion apiVersion = versionOptional.get();
                    if (apiVersion.value() != null && apiVersion.value().length != 0) {
                        if (Arrays.asList(apiVersion.value()).contains(groupName)) {
                            return true;
                        }
                    }
                }
            }
            Optional<ApiVersion> controllerAnnotation = input.findControllerAnnotation(ApiVersion.class);
            if (controllerAnnotation.isPresent()) {
                ApiVersion clzzApiVersion = controllerAnnotation.get();
                if (clzzApiVersion.value() != null && clzzApiVersion.value().length != 0) {
                    return Arrays.asList(clzzApiVersion.value()).contains(groupName);
                }
            }

            return false;
        }).paths(PathSelectors.any()).build();

    }

}

