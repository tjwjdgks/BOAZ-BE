package boaz.site.boazback.common.domain;

public enum TokenType {
    /** 토큰 지속 시간 mill * sec * min * hour * day */
    REFRESH(1000 * 60 * 60 * 24 * 3L),
    ACCESS(1000 * 60 * 60 * 24 * 2L),
    INFINITE_ACCESS(1000 * 60 * 60 * 24 * 356L),
    INFINITE_REFRESH(1000 * 60 * 60 * 24 * 365L),
    CERTIFICATION(1000 * 60 * 60 * 24 * 100L);


    private Long time;

    TokenType(Long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }
}
