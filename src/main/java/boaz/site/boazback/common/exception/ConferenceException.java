package boaz.site.boazback.common.exception;

public class ConferenceException  extends CustomException {

    public static final ConferenceException CONFERENCE_NOT_FOUND = new ConferenceException(ErrorCode.CONFERENCE_NOT_FOUND);
    public ConferenceException(ErrorCode errorCode) {
        super(errorCode);
    }
}
