package boaz.site.boazback.common.api;

import boaz.site.boazback.common.domain.HealthCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RestControllerEndpoint(id = "boaz")
@RequiredArgsConstructor
public class EndpointController {

    private final DataSource dataSource;

    @GetMapping("/ping")
    public String  greet() {
        return "pong";
    }

    @GetMapping("/health")
    public ResponseEntity<HealthCheck> serverHealthCheck(){
        try(Connection con = dataSource.getConnection()){
            return new ResponseEntity<>(HealthCheck.success(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HealthCheck.success(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
