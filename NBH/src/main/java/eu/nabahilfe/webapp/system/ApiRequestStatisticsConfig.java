package eu.nabahilfe.webapp.system;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiRequestStatisticsConfig implements WebMvcConfigurer {

    private final ApiRequestStatisticsInterceptor interceptor;

    public ApiRequestStatisticsConfig(ApiRequestStatisticsInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}