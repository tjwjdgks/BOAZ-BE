package boaz.site.boazback.common.exception;

public class CertificationException extends  CustomException{


    public static final CertificationException CERTIFICATION_EXPIRATION = new CertificationException(ErrorCode.CERTIFICATION_EXPIRATION);
    public static final CertificationException CERTIFICATION_NOT_SAME = new CertificationException(ErrorCode.CERTIFICATION_NOT_SAME);
    public static final CertificationException CERTIFICATION_NOT_FOUND = new CertificationException(ErrorCode.CERTIFICATION_NOT_FOUND);
    public static final CertificationException CERTIFICATION_EMAIL_ERROR = new CertificationException(ErrorCode.CERTIFICATION_EMAIL_ERROR);

    public CertificationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
