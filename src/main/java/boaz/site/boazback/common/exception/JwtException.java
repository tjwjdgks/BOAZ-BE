package boaz.site.boazback.common.exception;

public class JwtException  extends CustomException{
    public static final JwtException TOKEN_EXPIRED = new JwtException(ErrorCode.TOKEN_EXPIRATION);
    public static final JwtException TOKEN_NOT_FOUND = new JwtException(ErrorCode.TOKEN_NOT_FOUND);

    public static final JwtException TOKEN_MALFORMED = new JwtException(ErrorCode.TOKEN_MALFORMED);
    public static final JwtException TOKEN_SIGNATURE_ERROR = new JwtException(ErrorCode.TOKEN_SIGNATURE_ERROR);
    public static final JwtException TOKEN_UNSUPPORTED = new JwtException(ErrorCode.TOKEN_UNSUPPORTED);

    public JwtException(ErrorCode errorCode) {
        super(errorCode);
    }

}
