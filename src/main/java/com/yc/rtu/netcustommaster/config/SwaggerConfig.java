package com.yc.rtu.netcustommaster.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI WonJeongFoodOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("netCustomMaster API")
                        .description("netCustomMaster API 명세서"));
    }

    @Bean
    public GroupedOpenApi deviceGroup() {
        return GroupedOpenApi.builder()
                .group("device")
                .pathsToMatch("/api/v1/device/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/api/v1/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi settingGroup() {
        return GroupedOpenApi.builder()
                .group("setting")
                .pathsToMatch("/api/v1/setting/**")
                .build();
    }

    @Bean
    public GroupedOpenApi aiGroup() {
        return GroupedOpenApi.builder()
                .group("ai")
                .pathsToMatch("/api/v1/solve/**")
                .build();
    }

    @Bean
    public GroupedOpenApi stateGroup() {
        return GroupedOpenApi.builder()
                .group("state")
                .pathsToMatch("/api/v1/state/**")
                .build();
    }
}