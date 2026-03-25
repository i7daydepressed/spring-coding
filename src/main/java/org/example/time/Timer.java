package org.example.time;

import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;

public class Timer {
    public static <T> T proxyTime(T object) {
        Class<?> classObject = object.getClass();
        ClassLoader classLoader = classObject.getClassLoader();
        Class<?>[] interfaces = classObject.getInterfaces();
        if (interfaces.length == 0) throw new RuntimeException("Dont have interface");
        InvocationHandler handler = new HandlerTime(object);
        return (T) Proxy.newProxyInstance(
                classLoader,
                interfaces,
                handler
        );
    }
}