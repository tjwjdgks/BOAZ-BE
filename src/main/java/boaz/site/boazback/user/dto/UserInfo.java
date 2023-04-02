package boaz.site.boazback.user.dto;

import boaz.site.boazback.user.domain.Role;
import boaz.site.boazback.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

public class UserInfo {


    @Getter
    @Setter
    @NoArgsConstructor
    public static class SimpleLevel{
        private UUID id;

        private String name;
        private String track;
        private String year;
        private Role role;

        @Builder
        public SimpleLevel(UUID id, int track, String year, Role role,String name) {
            this.id = id;
            this.track = changeSectiontoString(track);
            this.year = year;
            this.role = role;
            this.name = name;
            }

            public SimpleLevel(User user){
                this.id = user.getId();
                this.track = changeSectiontoString(user.getSection());
                this.role = user.getRole();
                this.year = user.getYear();
                this.name = user.getName();
            }
        }


    @Getter
    @Setter
    @NoArgsConstructor
   public  static class DetailLevel{

        private UUID id;
        private String track;
        private String email;
        private String name;
        private String editName;
        private Role role;
        private String year;

        @Builder
        public DetailLevel(UUID id, String track, String email, String name, String editName, Role role, String year) {
            this.id = id;
            this.track = track;
            this.email = email;
            this.name = name;
            this.editName = editName;
            this.role = role;
            this.year = year;
        }
    }



    static String changeSectiontoString(int section) {
        switch (section) {
            case 1:
                return "분석";
            case 2:
                return "시각화";
            case 3:
                return "엔지니어링";
            default:
                return "없음";
        }
    }
}
