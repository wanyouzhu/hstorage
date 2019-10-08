package ltd.highsoft.framework.hstorage;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NonAggregate {

    Class<?> modelClass();

}
