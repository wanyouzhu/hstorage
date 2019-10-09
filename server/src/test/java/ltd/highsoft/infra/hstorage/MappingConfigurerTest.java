package ltd.highsoft.infra.hstorage;

import com.google.common.collect.ImmutableList;
import ltd.highsoft.infra.hstorage.test.MappingOne;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class MappingConfigurerTest {

    @Test
    void should_be_configured_from_mapping_classes() {
        MappingConfigurer configurer = new MappingConfigurer();
        configurer.addMappingClass(TestAggregateMapping.class);
        ModelMapping mapping = configurer.configure();
        Collection<MappingEntry> expected = ImmutableList.of(new MappingEntry(TestAggregateMapping.class));
        assertThat(mapping.entries()).usingFieldByFieldElementComparator().hasSameElementsAs(expected);
    }

    @Test
    void should_be_configured_from_packages() {
        MappingConfigurer configurer = new MappingConfigurer();
        configurer.addPackage("ltd.highsoft.infra.hstorage.test");
        ModelMapping mapping = configurer.configure();
        assertThat(mapping.entries()).usingFieldByFieldElementComparator().contains(new MappingEntry(MappingOne.class));
    }

}
