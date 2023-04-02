package boaz.site.boazback.curriculum.dto;

import boaz.site.boazback.curriculum.domain.Skill;
import lombok.Getter;

@Getter
public class SkillDto {

    private String name;
    private String logoUrl;

    public SkillDto(Skill skill) {
        this.name = skill.getName();
        this.logoUrl = skill.getLogoUrl();
    }
}
