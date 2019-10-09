package ltd.highsoft.infra.hstorage;

import ltd.highsoft.infra.hstorage.test.MappingInSubPackage;
import ltd.highsoft.infra.hstorage.test.MappingOne;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClassPathMappingResolverTest {

    @Test
    void should_be_able_to_resolve_mappings_from_packages_and_all_sub_packages() {
        ClassPathMappingResolver resolver = new ClassPathMappingResolver();
        List<Class<?>> classes = resolver.resolve("ltd.highsoft.infra.hstorage.test");
        assertThat(classes).contains(MappingOne.class, MappingInSubPackage.class);
    }

}
