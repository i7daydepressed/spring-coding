package org.testots;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class dsda implements BeanPostProcessor {

    // Для каждого бина запоминаем методы, которые надо проверять
    private final Map<String, Map<MethodKey, Check>> checkedMethods = new ConcurrentHashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        Map<MethodKey, Check> map = new HashMap<>();

        for (Method method : beanClass.getDeclaredMethods()) {
            Check check = method.getAnnotation(Check.class);

            if (check != null && method.getParameterCount() > 0) {
                map.put(MethodKey.of(method), check);
            }
        }

        if (!map.isEmpty()) {
            checkedMethods.put(beanName, map);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Map<MethodKey, Check> methods = checkedMethods.get(beanName);

        if (methods == null || methods.isEmpty()) {
            return bean;
        }

        // Один прокси на бин
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback((MethodInterceptor) (proxy, method, args, methodProxy) -> {
            Check check = methods.get(MethodKey.of(method));

            if (check != null) {
                ArgsValidator validator = check.value().getDeclaredConstructor().newInstance();

                if (!validator.isValid(args)) {
                    throw new IllegalArgumentException(
                        "Аргументы метода " + method.getName() + " не прошли проверку @Check"
                    );
                }
            }

            // Вызов оригинального бина
            return method.invoke(bean, args);
        });

        return enhancer.create();
    }

    private record MethodKey(String name, Class<?>[] paramTypes) {
        static MethodKey of(Method method) {
            return new MethodKey(method.getName(), method.getParameterTypes());
        }
    }
}