package boaz.site.boazback.common.exception;

public class RecruitmentException extends CustomException{

    public static final RecruitmentException RECRUITMENT_NOT_FOUND = new RecruitmentException(ErrorCode.RECRUITMENT_NOT_FOUND);

    public RecruitmentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
