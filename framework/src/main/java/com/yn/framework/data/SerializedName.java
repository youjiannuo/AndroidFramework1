package com.yn.framework.data;

/**
 * Created by youjiannuo on 16/1/11.
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SerializedName {

    /**
     * @return the desired name of the field when it is serialized
     */
    String value();
}
