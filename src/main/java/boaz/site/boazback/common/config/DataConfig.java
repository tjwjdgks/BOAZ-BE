package boaz.site.boazback.common.config;


import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataConfig {
//    @Bean(name="datasource")
//    @Primary
//    @ConfigurationProperties("spring.datasource.hikari")
//    public DataSource dataSource(){
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();
//    }
}
