package boaz.site.boazback.email.domain;

public enum EmailType {
    JOIN("email-template.ftl","[빅데이터 연합 동아리 BOAZ] 회원가입 인증 관련 메일입니다."),FIND("",""), REISSUE("email-reissue-template.ftl","[빅데이터 연합 동아리 BOAZ] 비밀번호 찾기 인증 관련 메일입니다.");
    private String templatePath;
    private String title;

    EmailType(String templatePath,String title) {
        this.templatePath = templatePath;
        this.title = title;
    }
    
    public String getTemplatePath() {
        return templatePath;
    }

    public String getTitle() {
        return title;
    }
}
