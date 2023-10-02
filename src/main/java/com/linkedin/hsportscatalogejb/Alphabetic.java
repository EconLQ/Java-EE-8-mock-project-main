package com.linkedin.hsportscatalogejb;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
// list of patterns to apply for custom bean validators
@Pattern.List(@Pattern(regexp = "^\\p{L}+(?: \\p{L}+)*$", message = "{invalid.alphabetic}"))
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
public @interface Alphabetic {
    String message() default "{invalid.alphabetic}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
