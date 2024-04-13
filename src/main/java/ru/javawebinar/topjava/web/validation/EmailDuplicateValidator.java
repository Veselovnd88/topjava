package ru.javawebinar.topjava.web.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.ErrorType;

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
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo user = (UserTo) target;
        User byEmail = repository.getByEmail(user.getEmail());
        if (byEmail != null) {
            log.warn("User with email already exists, validation failed");
            errors.rejectValue("email", ErrorType.VALIDATION_ERROR.toString(),
                    messageSourceAccessor.getMessage("exception.user.duplicateEmail"));
        }
    }
}
