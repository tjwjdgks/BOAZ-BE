package boaz.site.boazback.common.config;

import boaz.site.boazback.common.interceptor.AdminInterceptor;
import boaz.site.boazback.common.resolver.EmailCheckArgumentResolver;
import boaz.site.boazback.common.resolver.JwtLoginArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final JwtLoginArgumentResolver jwtLoginArgumentResolver;
    private final EmailCheckArgumentResolver emailCheckArgumentResolver;

    private final AdminInterceptor adminInterceptor;

    private final Environment env;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        String [] originPatterns =  new String[1];
        if(env.acceptsProfiles(Profiles.of("dev","local","test"))){
            originPatterns[0] = "*";
        }else{
            originPatterns[0] = "<YOUT WEB URL>";
        }

        registry.addMapping("/**")
                .allowedOriginPatterns(originPatterns)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3000);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtLoginArgumentResolver);
        resolvers.add(emailCheckArgumentResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");
    }
}
