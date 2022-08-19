package com.practice.springbootrestapimarket.aop;

import com.practice.springbootrestapimarket.config.security.guard.AuthHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AssignMemberIdAspect {
    private final AuthHelper authHelper;

    @Before("@annotation(com.practice.springbootrestapimarket.aop.AssignMemberId)")
    public void assignMember(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .forEach(args -> getMethod(args.getClass(), "setMemberId")
                        .ifPresent(setMemberId -> invokeMethod(args, setMemberId, authHelper.extractMemberId())));
    }

    private Optional<Method> getMethod(Class<?> _class, String methodName) {
        try {
            return Optional.of(_class.getMethod(methodName, Long.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private void invokeMethod(Object obj, Method method, Object... args) {
        try {
            method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
