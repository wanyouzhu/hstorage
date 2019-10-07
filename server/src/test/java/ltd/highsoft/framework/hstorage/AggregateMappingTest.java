package ltd.highsoft.framework.hstorage;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Java6Assertions.assertThat;

class AggregateMappingTest {

    private AggregateMapping mapping;

    @BeforeEach
    void setUp() {
        mapping = new AggregateMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
    }

    @Test
    void should_be_able_to_specify_mapping_entries() {
        assertThat(mapping.collectionOf(TestAggregate.class)).isEqualTo("test_aggregates");
        assertThat(mapping.mappingClassOf(TestAggregate.class)).isEqualTo(TestAggregateMapping.class);
    }

    @Test
    void should_be_able_to_retrieve_id_from_aggregate() {
        TestAggregate aggregate = new TestAggregate("one", "van");
        assertThat(mapping.idOf(aggregate)).isEqualTo("one");
    }

    @Test
    void should_reject_aggregates_without_id_field_during_id_retrieving() {
        Object object = new TypeWithoutIdField("Van");
        Throwable thrown = catchThrowable(() -> mapping.idOf(object));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Missing 'id' field in type 'ltd.highsoft.framework.hstorage.TypeWithoutIdField'!");
    }

    @Test
    void should_throw_mapping_exception_if_no_mapping_entry_present_while_resolving_collection() {
        Throwable thrown = catchThrowable(() -> mapping.collectionOf(String.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Mapping not found for aggregate class 'java.lang.String'!");
    }

    @Test
    void should_throw_mapping_exception_if_no_mapping_entry_present_while_resolving_mapping_class() {
        Throwable thrown = catchThrowable(() -> mapping.mappingClassOf(String.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Mapping not found for aggregate class 'java.lang.String'!");
    }

}
