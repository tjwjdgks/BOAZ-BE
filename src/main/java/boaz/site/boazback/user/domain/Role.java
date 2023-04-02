package boaz.site.boazback.user.domain;

import java.util.StringJoiner;

public enum Role {
    MEMBER, ADMIN;

    public static String getValues(){
        StringJoiner sj = new StringJoiner(",");
        for (Role role : Role.values()) {
            sj.add(role.toString());
        }
        return sj.toString();
    }
}
