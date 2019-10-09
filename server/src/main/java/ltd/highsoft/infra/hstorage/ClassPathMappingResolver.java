package ltd.highsoft.infra.hstorage;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class ClassPathMappingResolver {

    private final ClassLoader classLoader;

    public ClassPathMappingResolver() {
        this.classLoader = getClass().getClassLoader();
    }

    public ClassPathMappingResolver(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public List<Class<?>> resolve(String basePackage) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Mapping.class));
        Set<BeanDefinition> definitions = provider.findCandidateComponents(basePackage);
        return definitions.stream().map(this::loadMappingClass).collect(toList());
    }

    private Class<?> loadMappingClass(BeanDefinition x) {
        try {
            return classLoader.loadClass(x.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new MappingException("Failed to load mapping class '" + x.getBeanClassName() + "'!", e);
        }
    }

}
