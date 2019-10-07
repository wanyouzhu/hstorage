package ltd.highsoft.framework.hstorage;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Objects;

public class MappingEntry {

    private final Class<?> mappingClass;
    private final String collection;
    private final Class<?> aggregateClass;

    public MappingEntry(Class<?> mappingClass) {
        Aggregate annotation = getAggregateAnnotation(mappingClass);
        this.mappingClass = mappingClass;
        this.collection = getCollectionFromAnnotation(annotation);
        this.aggregateClass = annotation.aggregateClass();
    }

    private Aggregate getAggregateAnnotation(Class<?> mappingClass) {
        Aggregate annotation = AnnotationUtils.findAnnotation(mappingClass, Aggregate.class);
        if (annotation == null) throw createMappingException(mappingClass);
        return annotation;
    }

    private MappingException createMappingException(Class<?> mappingClass) {
        return new MappingException(
            "Invalid mapping class '" + mappingClass.getName() + "', no '@Aggregate' annotation present!"
        );
    }

    private String getCollectionFromAnnotation(Aggregate annotation) {
        if (StringUtils.isBlank(annotation.collection())) throw createMappingExceptionForBlankCollection(mappingClass);
        return annotation.collection();
    }

    private MappingException createMappingExceptionForBlankCollection(Class<?> mappingClass) {
        return new MappingException(
            "Invalid mapping class '" + mappingClass.getName() + "', collection of '@Aggregate' can not be empty!"
        );
    }

    public Class<?> aggregateClass() {
        return aggregateClass;
    }

    public String collection() {
        return collection;
    }

    public Class<?> mappingClass() {
        return mappingClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MappingEntry entry = (MappingEntry) o;
        return mappingClass.equals(entry.mappingClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mappingClass);
    }

}
