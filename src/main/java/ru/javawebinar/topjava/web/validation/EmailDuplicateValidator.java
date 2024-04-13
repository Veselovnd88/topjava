package ru.javawebinar.topjava.web.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.web.ExceptionInfoHandler;

@Component
public class EmailDuplicateValidator implements Validator {
    private static final Logger log = LoggerFactory.getLogger(EmailDuplicateValidator.class);

    private final UserRepository repository;

    private final MessageSourceAccessor messageSourceAccessor;


    public EmailDuplicateValidator(UserRepository repository, MessageSourceAccessor messageSourceAccessor) {
        this.repository = repository;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UserTo user = (UserTo) target;
        if (!StringUtils.hasText(user.getEmail())) {
            return;
        }
        User byEmail = repository.getByEmail(user.getEmail().toLowerCase());
        if (byEmail != null && !byEmail.getId().equals(user.getId())) {
            log.warn("User with email already exists, validation failed");
            errors.rejectValue("email", ErrorType.VALIDATION_ERROR.toString(),
                    messageSourceAccessor.getMessage(ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL));
        }
    }
}
