package boaz.site.boazback.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_PARAMETER(400, "I001", "Invalid Request Data"),
    FILE_NULL(400,"I003","file null"),
    TOKEN_NOT_FOUND(403,"T001","Token not found"),
    TOKEN_EXPIRATION(403, "T002", "Token Was Expired"),
    TOKEN_UNSUPPORTED(401, "T002", "Token Was Expired"),
    TOKEN_MALFORMED(401, "T002", "Token Subject Malformed"),
    TOKEN_SIGNATURE_ERROR(401, "T003", "Token signature fail"),
    PASSWORD_ERROR(401, "U002", "password not correct"),
    USER_ALREADY_REGISTERED(401, "U004", "user already registered"),
    EMAIL_ERROR(401, "U001", "you are not user"),
    USER_ALREADY_CERTIFIED(401, "U005", "user already certificated"),
    ADMIN_ERROR(403,"A001","you are not admin"),
    EDITOR_ERROR(403,"A002","you are not admin or Editor"),
    USER_NOT_FOUND(404, "U003", "user not found"),
    CERTIFICATION_NOT_FOUND(404,"P003","CertificationCode not found"),
    CONFERENCE_NOT_FOUND(404,"C001", "Conference not found"),
    CERTIFICATION_EXPIRATION(410,"P001","CertificationCode is expired"),
    CERTIFICATION_NOT_SAME(410,"P002","CertificationCode is not same"),
    CERTIFICATION_EMAIL_ERROR(410,"P004","Certification Email create error"),
    IMAGE_UPLOAD_ERROR(411,"I002","Image upload error"),

    RECRUITMENT_NOT_FOUND(404, "R001", "recruitment data not found"),

    GEO_IP_FORBIDDEN(403,"G001","you don't have permission to access this service"),
    SEVER_ERROR(500, "S001", "server error");



    private  String code;
    private  String message;
    private  int statusCode;

    ErrorCode( int statusCode,  String code,  String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.code = code;
    }
}
