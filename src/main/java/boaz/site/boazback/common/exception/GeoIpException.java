package boaz.site.boazback.common.exception;

public class GeoIpException extends CustomException{
    public static final GeoIpException GEO_IP_FORBIDDEN = new GeoIpException(ErrorCode.GEO_IP_FORBIDDEN);

    public GeoIpException(ErrorCode errorCode) {
        super(errorCode);
    }
}
