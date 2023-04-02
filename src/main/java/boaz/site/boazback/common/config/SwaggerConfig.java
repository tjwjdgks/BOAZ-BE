package boaz.site.boazback.common.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Profile(value = "!prd")
@Configuration
public class SwaggerConfig implements SwaggerResourcesProvider{

    @Override
    public List<SwaggerResource> get() {
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = patternResolver.getResources("/static/swagger/swagger.yaml");
            if (resources.length == 0) {
                return Collections.emptyList();
            }
            return Arrays.stream(resources).map(resource -> {
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setSwaggerVersion("3.0");
                swaggerResource.setName(resource.getFilename());
                swaggerResource.setLocation("/swagger/"+resource.getFilename());
                return swaggerResource;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
