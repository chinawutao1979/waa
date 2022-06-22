package lab01.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@Target(value = {ElementType.TYPE})
public @interface MyBean {

    /**
     * 待存入ioc容器的Bean名称
     *
     * @return Bean名称
     */
    String value() default "";
    
}