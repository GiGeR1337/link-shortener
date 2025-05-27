package com.example.tpo10.Constraints;

import com.example.tpo10.Repositories.LinkRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUrlValidator implements ConstraintValidator<UniqueUrl, String> {
    private final LinkRepository linkRepository;
    public UniqueUrlValidator(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }
    @Override
    public boolean isValid(String targetUrl, ConstraintValidatorContext context) {
        return !linkRepository.existsByTargetUrl(targetUrl);
    }
}
