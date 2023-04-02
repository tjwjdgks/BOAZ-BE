package boaz.site.boazback.user.domain;

import boaz.site.boazback.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@NoArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;
    private int section;

    private String name;
    private String email;

    @JsonIgnore
    private String password;

    private String year; //기수
    private String editName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_date")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;


    public String getEditName(int section) {
        String sectionName = changeSectiontoString(section);
        return this.year + "_" + sectionName + "_" + name;
    }

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private String changeSectiontoString(int section) {
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

    @ColumnDefault("false")
    private boolean emailCheck;

    @Builder(builderMethodName = "jwtBuilder",buildMethodName = "get")
    public User(UUID id, int section, String name, String email, String password, String year, String birthDate,Date createdDate,Date modifiedDate) {
        this.id = id;
        this.section = section;
        this.name = name;
        this.email = email;
        this.password = password;
        this.year = year;
        this.editName = this.getEditName(section);
        this.role = Role.MEMBER;
        if (birthDate != null) {
            this.birthDate = changeStringToDate(birthDate);
        }
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    @Builder
    public User(UUID id, int section, String name, String email, String password, String year, String birthDate) {
        this.id = id;
        this.section = section;
        this.name = name;
        this.email = email;
        this.password = password;
        this.year = year;
        this.editName = this.getEditName(section);
        this.role = Role.MEMBER;
        if (birthDate != null) {
            this.birthDate = changeStringToDate(birthDate);
        }
    }

    public User(UserDto userDto) {
        this.section = userDto.getSection();
        this.name = userDto.getName();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
        this.year = userDto.getYear();
        this.editName = this.getEditName(section);
        this.role = Role.MEMBER;
        if (birthDate != null) {
            this.birthDate = changeStringToDate(userDto.getBirthDate());
        }
    }

    private Date changeStringToDate(String birthDate) {
        try {
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
            return transFormat.parse(birthDate);
        } catch (Exception e) {
            return null;
        }
    }

    public User certificateEmail() {
        this.emailCheck = true;
        return this;
    }

    public User changePassword(String newPassword) {
        this.password = newPassword;
        return this;
    }

    public void registRole(Role role){
        this.role = role;
    }


}


