package ltd.highsoft.framework.hstorage;

import com.google.common.collect.Maps;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

public class AggregateMapping {

    private final Map<Class<?>, MappingEntry> entries = Maps.newHashMap();

    public AggregateMapping(List<MappingEntry> entries) {
        entries.forEach(this::addEntry);
    }

    private void addEntry(MappingEntry mappingEntry) {
        checkForDuplicatedEntry(mappingEntry);
        entries.put(mappingEntry.aggregateClass(), mappingEntry);
    }

    private void checkForDuplicatedEntry(MappingEntry entry) {
        MappingEntry existed = entries.get(entry.aggregateClass());
        if (existed != null) {
            throw createDuplicatedMappingException(entry.aggregateClass(), existed.mappingClass(), entry.mappingClass());
        }
    }

    private MappingException createDuplicatedMappingException(Class<?> aClass, Class<?> mapping1, Class<?> mapping2) {
        return new MappingException("" +
            "Multiple mapping specified for aggregate class '" + aClass.getName() +
            "': [" + mapping1.getName() + ", " + mapping2.getName() + "]"
        );
    }

    public String idOf(Object aggregate) {
        Field field = ReflectionUtils.findField(aggregate.getClass(), "id", String.class);
        if (field == null) throw createMissingIdException(aggregate);
        ReflectionUtils.makeAccessible(field);
        return (String) ReflectionUtils.getField(field, aggregate);
    }

    private MappingException createMissingIdException(Object aggregate) {
        return new MappingException("Missing 'id' field in type '" + aggregate.getClass().getName() + "'!");
    }

    public String collectionOf(Class<?> aggregateClass) {
        ensureMappingExisted(aggregateClass);
        return entries.get(aggregateClass).collection();
    }

    private void ensureMappingExisted(Class<?> aggregateClass) {
        if (!entries.containsKey(aggregateClass)) throw createMissingMappingException(aggregateClass);
    }

    private MappingException createMissingMappingException(Class<?> aggregateClass) {
        return new MappingException("Mapping not found for aggregate class '" + aggregateClass.getName() + "'!");
    }

    public Collection<MappingEntry>  entries() {
        return entries.values();
    }

}
