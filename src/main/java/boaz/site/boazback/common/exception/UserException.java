package boaz.site.boazback.common.exception;

public class UserException extends CustomException{

    public static final UserException LOGIN_ERROR = new UserException(ErrorCode.EMAIL_ERROR);
    public static final UserException PASSWORD_ERROR = new UserException(ErrorCode.PASSWORD_ERROR);
    public static final UserException USER_NOT_FOUND = new UserException(ErrorCode.USER_NOT_FOUND);
    public static  final UserException USER_ALREADY_REGISTERED = new UserException(ErrorCode.USER_ALREADY_REGISTERED);
    public static final UserException USER_ALREADY_CERTIFIED = new UserException(ErrorCode.USER_ALREADY_CERTIFIED);

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
