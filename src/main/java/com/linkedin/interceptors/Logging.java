package com.linkedin.interceptors;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@InterceptorBinding
@Inherited
@Target({TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logging {
}
