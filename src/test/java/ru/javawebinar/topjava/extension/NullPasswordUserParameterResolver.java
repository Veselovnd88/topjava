package ru.javawebinar.topjava.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import ru.javawebinar.topjava.extension.annotation.NullPasswordUser;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Collections;
import java.util.Date;

public class NullPasswordUserParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(NullPasswordUser.class)
                && parameterContext.getParameter().getType().equals(User.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new User(1, "New", "new@gmail.com", null, 1555, false, new Date(), Collections.singleton(Role.USER));
    }
}
