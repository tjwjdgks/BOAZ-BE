package boaz.site.boazback.common.exception;

public class AuthorityException extends  CustomException{

    public static final AuthorityException ADMIN_ERROR = new AuthorityException(ErrorCode.ADMIN_ERROR);
    public static final AuthorityException EDITOR_ERROR = new AuthorityException(ErrorCode.EDITOR_ERROR);

    public AuthorityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
