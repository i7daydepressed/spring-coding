package org.testots;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Annotation {
    Class<? extends ArgsValidator> value();
}
