package boaz.site.boazback.common.exception;

public class ImageException extends CustomException{

    public static final ImageException FILE_NULL = new ImageException(ErrorCode.FILE_NULL);
    public static final ImageException IMAGE_UPLOAD_ERROR = new ImageException(ErrorCode.IMAGE_UPLOAD_ERROR);


    public ImageException(ErrorCode errorCode) {
        super(errorCode);
    }
}
