package org.example.time;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.example.annotations.TimeMeasure;

public class HandlerTime implements InvocationHandler {
    private Object original;

    HandlerTime(Object original){
        this.original = original;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> originalClass = original.getClass();
        method = originalClass.getMethod(method.getName(), method.getParameterTypes());
        if(!method.isAnnotationPresent(TimeMeasure.class)){
            return method.invoke(original,args);
        }

        long start = System.currentTimeMillis();
        Object res = method.invoke(original,args);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println(timeElapsed);
        return res;
    }
}
