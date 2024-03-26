package com.sky.aspect;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * intercept by condition
     * ---> interface found in mapper package
     * ---> AutoFill annotation tag MUST exist
     */
    @Pointcut("execution(* com.sky.mapper.*.* (..)) && @annotation(com.sky.annotation.AutoFill)")
    public void anyMapperMethods(){};

    /**
     * Before advice: complete entity public fields(update/create time/user) b4 execute query
     * @param joinPoint
     */
    @Before("anyMapperMethods()")
    public void beforeAdvice(JoinPoint joinPoint){
        log.info("Starting enhance method... : {}", joinPoint);

        // Avoid raw pointer
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        // Business promise that first index MUST be entity
        Object entity = args[0];

        // Get annotation value
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = annotation.value();

        // Business promise that first index MUST be entity
        //Class[] parameterTypes = signature.getParameterTypes();
        //Class entity = parameterTypes[0];

        // Public fields
        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();


        // Base on different type insert public fields
        if(operationType.equals(OperationType.INSERT)){
            try {
                //TODO understand how reflection work, what is different method and declared method in class
                Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);

                setCreateUser.invoke(entity, id);
                setUpdateUser.invoke(entity, id);
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);


            } catch (Exception e) {
                //TODO not sure did this need change to log error
                throw new RuntimeException(e);
            }
        } else if (operationType.equals(OperationType.UPDATE)) {

            try {
                Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);

                setUpdateUser.invoke(entity, id);
                setUpdateTime.invoke(entity, now);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    };

}
