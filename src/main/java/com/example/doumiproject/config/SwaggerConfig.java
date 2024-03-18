package com.example.doumiproject.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Bend Doumi API Docs",
        description = "Bend Doumi API 문서입니다.",
        version = "1.0.0"
    )
)

@Configuration
public class SwaggerConfig {

}
