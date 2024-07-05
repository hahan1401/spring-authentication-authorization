package com.first_project.demo.vadidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DobValidator implements ConstraintValidator<DobConstraint, LocalDate> {

  private int min;

  @Override
  public void initialize(DobConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
    min = constraintAnnotation.min();
  }

  @Override
  public boolean isValid(LocalDate value,
      ConstraintValidatorContext constraintValidatorContext) {

    long years = ChronoUnit.YEARS.between(value, LocalDate.now());
    return years >= min;
  }
}
