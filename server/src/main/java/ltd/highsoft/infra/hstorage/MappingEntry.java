package ltd.highsoft.infra.hstorage;

import org.springframework.core.annotation.AnnotationUtils;

public class MappingEntry {

    private final Class<?> mappingClass;
    private final String collection;
    private final Class<?> modelClass;

    public MappingEntry(Class<?> mappingClass) {
        Mapping mapping = getMappingAnnotation(mappingClass);
        this.mappingClass = mappingClass;
        this.collection = mapping.collection();
        this.modelClass = mapping.modelClass();
    }

    private static Mapping getMappingAnnotation(Class<?> mappingClass) {
        Mapping annotation = AnnotationUtils.findAnnotation(mappingClass, Mapping.class);
        if (annotation == null) throw createMappingException(mappingClass);
        return annotation;
    }

    private static MappingException createMappingException(Class<?> mappingClass) {
        return new MappingException(
            "Invalid mapping class '" + mappingClass.getName() + "', no '@Mapping' annotation present!"
        );
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
