package ltd.highsoft.framework.hstorage;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class AggregateMapping {

    private final Map<Class<?>, MappingEntry> entries;

    public AggregateMapping(List<MappingEntry> entries) {
        this.entries = entries.stream().collect(Collectors.toMap(MappingEntry::aggregateClass, x -> x));
    }

    public String getIdOf(Object aggregate) {
        Field field = ReflectionUtils.findField(aggregate.getClass(), "id", String.class);
        if (field == null) throw createMissingIdException(aggregate);
        ReflectionUtils.makeAccessible(field);
        return (String) ReflectionUtils.getField(field, aggregate);
    }

    private MappingException createMissingIdException(Object aggregate) {
        return new MappingException("Missing 'id' field in type '" + aggregate.getClass().getName() + "'!");
    }

    public String getCollectionOf(Class<?> aggregateClass) {
        return entries.get(aggregateClass).collection();
    }

    public Class<?> getMappingClassOf(Class<?> aggregateClass) {
        return entries.get(aggregateClass).mappingClass();
    }

}
