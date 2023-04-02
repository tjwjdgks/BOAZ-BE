package boaz.site.boazback.common.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthCheck {
    private String server;
    private String db;
    private String error;

    @Builder
    public HealthCheck(String server, String db, String error) {
        this.server = server;
        this.db = db;
        this.error = error;
    }

    public static HealthCheck fail(String error){
        return new HealthCheck("DOWN","DOWN",error);
    }

    public static HealthCheck success(){
        return new HealthCheck("UP", "UP", "");
    }
}
