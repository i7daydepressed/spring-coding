package org.example;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.example.time.Timer;

import org.example.annotations.Spawn;
import org.example.exceptions.NoEmptyConstructorException;
// 
// 
// 
public class Collector {
    // коллектить все порожденное у которого есть метод с аннотацией замера времени
    // 
    private static String getMethodName(Method method) {
        String annotationMethodName = method.getDeclaredAnnotation(Spawn.class).value();
        if(!annotationMethodName.isBlank()){
            return annotationMethodName;
        }
        return method.getName();
    }

    private static Object methodInvoke(Method method, Object object) {
        try {
            Object res = method.invoke(object);
            return Timer.proxyTime(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object objectCreator(Class<?> clz) {
        try{
            Constructor<?> constructor = clz.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch(NoSuchMethodException e){
            throw new NoEmptyConstructorException();
        } catch(Exception e){
            throw new RuntimeException();
        }
    }

    public static Map<String, Object> collect(Class<?> clz) { // 2 использования
        Object target = objectCreator(clz);

        return Arrays.stream(clz.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(Spawn.class))
            .filter(method -> method.getParameterCount() == 0)
            .filter(method -> !method.getReturnType().equals(void.class))
            .peek(method -> method.setAccessible(true))
            .collect(Collectors.toMap(
                method -> getMethodName(method),
                method -> methodInvoke(method, target)
            ));
    }
}
