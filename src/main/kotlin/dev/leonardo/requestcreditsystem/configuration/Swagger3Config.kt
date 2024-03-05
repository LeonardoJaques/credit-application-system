package dev.leonardo.requestcreditsystem.configuration

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger3Config {
    @Bean
   fun publicApi(): GroupedOpenApi?{
        return GroupedOpenApi.builder()
            .group("springrequestcreditsystem-public")
            .displayName("Spring Request Credit System Public API")
            .addOpenApiCustomizer(
                org.springdoc.core.customizers.OpenApiCustomizer { openApi ->
                    openApi.info.title = "R.C.System"
                    openApi.info.version = "0.0.1"
                    openApi.info.description = "Request Credit System API"
                       openApi.info.contact(
                            io.swagger.v3.oas.models.info.Contact()
                                 .name("Leonardo")
                                 .email("leonardo@jaquesprojetos.com.br")
                        )

                }
            )
            .pathsToMatch("/api/customers/**", "/api/credits/**")
            .build()
   }
}