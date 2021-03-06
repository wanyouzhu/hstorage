package ltd.highsoft.hstorage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.google.common.collect.Maps;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelMapping {

    private final Map<Class<?>, MappingEntry> entries = Maps.newHashMap();

    public ModelMapping(List<MappingEntry> entries) {
        entries.forEach(this::addEntry);
    }

    private void addEntry(MappingEntry mappingEntry) {
        addEntry(mappingEntry.modelClass(), mappingEntry);
        addEntriesForSubClasses(mappingEntry);
    }

    private void addEntriesForSubClasses(MappingEntry mappingEntry) {
        JsonSubTypes annotation = AnnotationUtils.findAnnotation(mappingEntry.mappingClass(), JsonSubTypes.class);
        if (annotation == null) return;
        for (JsonSubTypes.Type type : annotation.value()) {
            addEntry(type.value(), mappingEntry);
        }
    }

    private void addEntry(Class<?> modelClass, MappingEntry mappingEntry) {
        checkForDuplicatedEntry(modelClass, mappingEntry.mappingClass());
        entries.put(modelClass, mappingEntry);
    }

    private void checkForDuplicatedEntry(Class<?> modelClass, Class<?> mappingClass) {
        MappingEntry existed = entries.get(modelClass);
        if (existed != null) {
            throw createDuplicatedMappingException(modelClass, existed.mappingClass(), mappingClass);
        }
    }

    private MappingException createDuplicatedMappingException(Class<?> aClass, Class<?>... mappings) {
        return new MappingException("" +
            "Multiple mapping specified for aggregate class '" + aClass.getName() +
            "': [" + Arrays.stream(mappings).map(Class::getName).collect(Collectors.joining(",")) + "]"
        );
    }

    public String idOf(final Object aggregate) {
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
        String collection = entries.get(aggregateClass).collection();
        if (StringUtils.isEmpty(collection)) throw createWrongMappingException(aggregateClass);
        return collection;
    }

    private MappingException createWrongMappingException(Class<?> modelClass) {
        return new MappingException("Class '" + modelClass.getName() + "' is mapped as a non-aggregate!");
    }

    private void ensureMappingExisted(Class<?> modelClass) {
        if (!entries.containsKey(modelClass)) throw createMissingMappingException(modelClass);
    }

    private MappingException createMissingMappingException(Class<?> modelClass) {
        return new MappingException("Mapping not found for aggregate class '" + modelClass.getName() + "'!");
    }

    public Collection<MappingEntry> entries() {
        return entries.values();
    }

}
