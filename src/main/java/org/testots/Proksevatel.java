package org.testots;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sf.cglib.proxy.Enhancer;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class Proksevatel implements BeanPostProcessor {
    private final Map<String, Map<MethodKey, Annotation>> annotationedMethods = new ConcurrentHashMap<>();
    

    private record MethodKey(String name, Class<?>[] paramTypes) {
        static MethodKey of(Method method) {
            return new MethodKey(method.getName(), method.getParameterTypes());
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Map<MethodKey, Annotation> map = new HashMap<>();
        for (Method method : beanClass.getDeclaredMethods()) {

            Annotation annotation = method.getAnnotation(Annotation.class);

            if (annotation != null && method.getParameterCount() > 0) {
                map.put(MethodKey.of(method), annotation);
            }
            
        }
        if (!map.isEmpty()) {
            annotationedMethods.put(beanName, map);
        }

		return bean;
	}
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Map<MethodKey, Annotation> methods = annotationedMethods.get(beanName);

        if (methods == null || methods.isEmpty()) {
            return bean;
        }
        //проксюем
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback((MethodInterceptor) (Object proxy, Method method, Object[] args, MethodProxy methodProxy) -> {
            Annotation Annotation = methods.get(MethodKey.of(method));

            if (Annotation != null) {
                ArgsValidator validator = Annotation.value().getDeclaredConstructor().newInstance();

                if (!validator.isValid(args)) {
                    throw new IllegalArgumentException(
                        "аргументы метода " + method.getName() +  " не прошли проверку @Annotation"
                    );
                }
            }

            return method.invoke(bean, args);
        });

        return enhancer.create();
	}
}
