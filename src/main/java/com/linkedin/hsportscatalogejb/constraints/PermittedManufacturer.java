package com.linkedin.hsportscatalogejb.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {PermittedManufacturerConstraintValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
public @interface PermittedManufacturer {
    String message() default "{invalid.manufacturer}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
