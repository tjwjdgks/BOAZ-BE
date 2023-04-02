package boaz.site.boazback.common.domain;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Slf4j
public class URLValidator implements ConstraintValidator<URLValid, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean foundMatch = false;
        try {
            Pattern regex = Pattern.compile("\\b(?:(https?|ftp|file)://|www\\.)?[-A-Z0-9+&#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]\\.[-A-Z0-9+&@#/%?=~_|$!:,.;]*[A-Z0-9+&@#/%=~_|$]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher regexMatcher = regex.matcher(value);
            foundMatch = regexMatcher.matches();
        } catch (PatternSyntaxException ex) {
            log.warn(ex.getMessage());
        }
        return foundMatch;
    }
}
