package boaz.site.boazback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
//test
public class BoazbackApplication {
    public static void main(String[] args) {
        SpringApplication.run(BoazbackApplication.class, args);
    }
}
