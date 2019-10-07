package ltd.highsoft.framework.hstorage;

import org.springframework.core.annotation.AnnotationUtils;

public class MappingEntry {

    private final Class<?> mappingClass;
    private final String collection;
    private final Class<?> aggregateClass;

    public MappingEntry(Class<?> mappingClass) {
        Aggregate annotation = AnnotationUtils.findAnnotation(mappingClass, Aggregate.class);
        this.mappingClass = mappingClass;
        this.collection = annotation.collection();
        this.aggregateClass = annotation.aggregateClass();
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

}
