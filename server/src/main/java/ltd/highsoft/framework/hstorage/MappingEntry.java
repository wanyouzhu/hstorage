package ltd.highsoft.framework.hstorage;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

public class MappingEntry {

    private final Class<?> mappingClass;
    private final String collection;
    private final Class<?> modelClass;

    private MappingEntry(Class<?> mappingClass, Aggregate annotation) {
        this.mappingClass = mappingClass;
        this.collection = getCollectionFromAnnotation(annotation);
        this.modelClass = annotation.modelClass();
    }

    private MappingEntry(Class<?> mappingClass, NonAggregate annotation) {
        this.mappingClass = mappingClass;
        this.collection = null;
        this.modelClass = annotation.modelClass();
    }

    public static MappingEntry ofAggregate(Class<?> mappingClass) {
        return new MappingEntry(mappingClass, getAggregateAnnotation(mappingClass));
    }

    public static MappingEntry ofNonAggregate(Class<?> mappingClass) {
        return new MappingEntry(mappingClass, getNonAggregateAnnotation(mappingClass));
    }

    private static Aggregate getAggregateAnnotation(Class<?> mappingClass) {
        Aggregate annotation = AnnotationUtils.findAnnotation(mappingClass, Aggregate.class);
        if (annotation == null) throw createMappingException(mappingClass, "@Aggregate");
        return annotation;
    }

    private static MappingException createMappingException(Class<?> mappingClass, String annotation) {
        return new MappingException(
            "Invalid mapping class '" + mappingClass.getName() + "', no '" + annotation + "' annotation present!"
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

    private static NonAggregate getNonAggregateAnnotation(Class<?> mappingClass) {
        NonAggregate annotation = AnnotationUtils.findAnnotation(mappingClass, NonAggregate.class);
        if (annotation == null) throw createMappingException(mappingClass, "@NonAggregate");
        return annotation;
    }

    public Class<?> modelClass() {
        return modelClass;
    }

    public String collection() {
        return collection;
    }

    public Class<?> mappingClass() {
        return mappingClass;
    }

}
