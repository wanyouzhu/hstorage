package ltd.highsoft.hstorage;

import ltd.highsoft.hstorage.test.MappingInSubPackage;
import ltd.highsoft.hstorage.test.MappingOne;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClassPathMappingResolverTest {

    @Test
    void should_be_able_to_resolve_mappings_from_packages_and_all_sub_packages() {
        ClassPathMappingResolver resolver = new ClassPathMappingResolver();
        List<Class<?>> classes = resolver.resolve("ltd.highsoft.hstorage.test");
        assertThat(classes).contains(MappingOne.class, MappingInSubPackage.class);
    }

    @Test
    void should_throw_mapping_exception_if_class_not_found() throws ClassNotFoundException {
        ClassLoader classLoader = Mockito.mock(ClassLoader.class);
        when(classLoader.loadClass(any())).thenThrow(new ClassNotFoundException("test"));
        ClassPathMappingResolver resolver = new ClassPathMappingResolver(classLoader);
        Throwable thrown = catchThrowable(() -> resolver.resolve("ltd.highsoft.hstorage.test"));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessageContaining("Failed to load mapping class");
        assertThat(thrown).hasCauseInstanceOf(ClassNotFoundException.class);
    }

}
