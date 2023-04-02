package boaz.site.boazback.common.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.Errors;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class Result {


    private int statusCode;
    @JsonProperty(value="payload")
    private Object data;
    private FormEnum formEnum;

    public Result(int statusCode, Object data, FormEnum formEnum) {
        this.statusCode = statusCode;
        this.data = data;
        this.formEnum = formEnum;
    }

    public static Result customResult(Object data, int statusCode, FormEnum formEnum) {
	    return new Result(statusCode,data,formEnum);
    }
    public Result resultSuccess() {
        statusCode = 200;
        data = true;
        formEnum = FormEnum.PASS;
        return this;
    }
    public Result resultFault() {
        statusCode = 510;
        data = false;
        formEnum = FormEnum.FAULT;
        return this;
    }
    public Result resultError() {
        statusCode = 500;
        data = false;
        formEnum = FormEnum.ERROR;
        return this;
    }
    public Result resultBadRequest(Errors error){
        statusCode = 400;
        data = error;
        formEnum = FormEnum.FAULT;
        return this;
    }

    public Result resultExpire(){
        statusCode = 500;
        data = false;
        formEnum = FormEnum.EXPIRE;
        return this;
    }


}
