package com.example.tpo10.Constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank())
            return true;

        boolean valid = true;

        context.disableDefaultConstraintViolation();

        if (!password.matches(".*[a-z].*")) {
            context.buildConstraintViolationWithTemplate("{password.lowercase}")
                    .addConstraintViolation();
            valid = false;
        }

        if (password.replaceAll("[^A-Z]", "").length() < 2) {
            context.buildConstraintViolationWithTemplate("{password.uppercase}")
                    .addConstraintViolation();
            valid = false;
        }

        if (password.replaceAll("[^0-9]", "").length() < 3) {
            context.buildConstraintViolationWithTemplate("{password.digits}")
                    .addConstraintViolation();
            valid = false;
        }

        if (password.replaceAll("[\\w]", "").length() < 4) {
            context.buildConstraintViolationWithTemplate("{password.special}")
                    .addConstraintViolation();
            valid = false;
        }

        if (password.length() < 10) {
            context.buildConstraintViolationWithTemplate("{password.length}")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
