package com.maestro.lib.junit5.json;

import org.junit.jupiter.params.provider.ArgumentsSource;
import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ArgumentsSource(JsonFileArgumentsProvider.class)
public @interface JsonFileSource {
    String[] resources();
}
