package boaz.site.boazback.common.domain;


import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER, TYPE_USE }) // 1
@Retention(RetentionPolicy.RUNTIME) // 2
@Constraint(validatedBy = URLValidator.class) // 3
public @interface URLValid {
    String message() default "url is not correct"; // 4
    Class[] groups() default {};
    Class[] payload() default {};
}
