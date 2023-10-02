package com.linkedin.hsportscatalogejb.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * Constraint validator which checks that the manufacturer is either CompanyA or CompanyB
 */
public class PermittedManufacturerConstraintValidator implements ConstraintValidator<PermittedManufacturer, String> {
    private static String[] permittedManufacturers = new String[]{"CompanyA", "CompanyB"};

    @Override
    public void initialize(PermittedManufacturer constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.asList(permittedManufacturers).contains(value);
    }
}
