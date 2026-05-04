package com.tsonline.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;


@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Enter your JWT token to access secured endpoints");
		
		SecurityRequirement bearerRequirement = new SecurityRequirement().addList("Bearer Authentication");
		
		return new OpenAPI()
						.components(new Components()
											.addSecuritySchemes("Bearer Authentication", bearerScheme))
						.addSecurityItem(bearerRequirement)
						.externalDocs(new ExternalDocumentation()
											.description("Project documentation")
											.url("http://allenKo.com"))
						.info(new Info()
									.title("Top Service eCommerce API")
									.version("1.0")
									.description("End ponts for Top Service eCommerce API")
									.license(new License()
													.name("Apache 2.0")
													.url("http://tsOnline.com"))
									.contact(new Contact()
													.name("Allen Ko")
													.email("allenKo99@gmail.com")
													.url("http://github.com/allenKo")));
	}
}
