package ltd.highsoft.framework.hstorage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.google.common.collect.Maps;
import org.springframework.core.annotation.AnnotationUtils;
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
        entries.put(mappingEntry.modelClass(), mappingEntry);
        addEntriesForSubClasses(mappingEntry);
    }

    private void addEntriesForSubClasses(MappingEntry mappingEntry) {
        JsonSubTypes annotation = AnnotationUtils.findAnnotation(mappingEntry.mappingClass(), JsonSubTypes.class);
        if (annotation == null) return;
        for (JsonSubTypes.Type type : annotation.value()) {
            MappingEntry existed = entries.get(type.value());
            if (existed != null) {
                throw createDuplicatedMappingException(type.value(), existed.mappingClass(), mappingEntry.mappingClass());
            }
            entries.put(type.value(), mappingEntry);
        }
    }

    private void checkForDuplicatedEntry(MappingEntry entry) {
        MappingEntry existed = entries.get(entry.modelClass());
        if (existed != null) {
            throw createDuplicatedMappingException(entry.modelClass(), existed.mappingClass(), entry.mappingClass());
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

    private void ensureMappingExisted(Class<?> modelClass) {
        if (!entries.containsKey(modelClass)) throw createMissingMappingException(modelClass);
    }

    private MappingException createMissingMappingException(Class<?> mocelClass) {
        return new MappingException("Mapping not found for aggregate class '" + mocelClass.getName() + "'!");
    }

    public Collection<MappingEntry>  entries() {
        return entries.values();
    }

}
