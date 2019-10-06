package ltd.highsoft.framework.hstorage;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class AggregateMapping {

    public String getIdOf(Object aggregate) {
        Field field = ReflectionUtils.findField(aggregate.getClass(), "id", String.class);
        if (field == null)
            throw new MappingException("Missing 'id' field in type '" + aggregate.getClass().getName() + "'!");
        ReflectionUtils.makeAccessible(field);
        return (String) ReflectionUtils.getField(field, aggregate);
    }

}
