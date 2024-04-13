package ru.javawebinar.topjava.extension.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import ru.javawebinar.topjava.extension.NullPasswordUserParameterResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(NullPasswordUserParameterResolver.class)
public @interface NullPasswordUser {
}
