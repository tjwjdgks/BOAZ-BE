package boaz.site.boazback.common.exception;

import boaz.site.boazback.common.util.SlackUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @Autowired
    private SlackUtil slackUtil;

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse>jwtException(JwtException e) {
        log.error("jwtException start");
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        log.error("jwtException end");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> userException(UserException e){
        log.error("userException start");
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        log.error("userException end");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(AuthorityException.class)
    public ResponseEntity<ErrorResponse> authorityException(AuthorityException e){
        log.error("authorityException start");
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        log.error("authorityException end");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }


    @ExceptionHandler(ConferenceException.class)
    public ResponseEntity<ErrorResponse> conferenceException(ConferenceException e){
        log.error("conferenceException start");
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        log.error("conferenceException end");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(CertificationException.class)
    public ResponseEntity<ErrorResponse> certificationException(CertificationException e){
        log.error("certificationException start");
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        log.error("certificationException end");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<Object> imageException(ImageException e){
        log.error("imageException start");
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        log.error("imageException end");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }

    @ExceptionHandler(RecruitmentException.class)
    public ResponseEntity<ErrorResponse> recruitmentException(RecruitmentException e){
        log.error("recruitmentException start");
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        log.error("recruitmentException end");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatusCode()));
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> illegalStateException(IllegalStateException e){
        log.error("illegalStateException start");
        log.error(e.getMessage());
        ErrorCode errorCode = ErrorCode.SEVER_ERROR;
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        log.error("illegalStateException start");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(500));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e){
        log.error("illegalArgumentException start");
        log.error(e.getMessage());
        ErrorCode errorCode = ErrorCode.SEVER_ERROR;
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        slackUtil.sendErrorMessage(e.getMessage());
        log.error("illegalArgumentException start");
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(500));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("MethodArgumentNotValidException start!! url:{}",request.getRequestURI());
        ErrorResponse result = makeErrorResponse(e.getBindingResult());
        slackUtil.sendErrorMessage(e.getMessage());
        log.warn("MethodArgumentNotValidException end");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionError(Exception e, HttpServletRequest request){
        log.warn("Exception start!! url:{}",request.getRequestURI());
        ErrorCode errorCode = ErrorCode.SEVER_ERROR;
        ErrorResponse errorResponse = getErrorResponse(errorCode);
        slackUtil.sendErrorMessage(e.getMessage());
        log.warn("Exception end");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        ErrorResponse response = new ErrorResponse();
        response.status(400);
        response.message("Invalid Request Data");
        response.code("");
        response.errors(errors);
        return response;
    }

    private ErrorResponse getErrorResponse(ErrorCode errorCode) {
        ErrorResponse response = ErrorResponse.create()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatusCode());
        return response;
    }

}
