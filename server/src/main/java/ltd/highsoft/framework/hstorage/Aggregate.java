package ltd.highsoft.framework.hstorage;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Aggregate {

    Class<?> aggregateClass();

    String collection();

}
