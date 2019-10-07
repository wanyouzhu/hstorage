package ltd.highsoft.framework.hstorage;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Java6Assertions.assertThat;

class AggregateMappingTest {

    @Test
    void should_be_able_to_specify_mapping_entries() {
        AggregateMapping mapping = new AggregateMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
        assertThat(mapping.collectionOf(TestAggregate.class)).isEqualTo("test_aggregates");
        assertThat(mapping.mappingClassOf(TestAggregate.class)).isEqualTo(TestAggregateMapping.class);
    }

    @Test
    void should_be_able_to_retrieve_id_from_aggregate() {
        AggregateMapping mapping = new AggregateMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
        TestAggregate aggregate = new TestAggregate("one", "van");
        assertThat(mapping.idOf(aggregate)).isEqualTo("one");
    }

    @Test
    void should_reject_aggregates_without_id_field_during_id_retrieving() {
        AggregateMapping mapping = new AggregateMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
        Object object = new TypeWithoutIdField("Van");
        Throwable thrown = catchThrowable(() -> mapping.idOf(object));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Missing 'id' field in type 'ltd.highsoft.framework.hstorage.TypeWithoutIdField'!");
    }

    @Test
    void should_throw_mapping_exception_if_no_mapping_entry_present_while_resolving_collection() {
        AggregateMapping mapping = new AggregateMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
        Throwable thrown = catchThrowable(() -> mapping.collectionOf(String.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Mapping not found for aggregate class 'java.lang.String'!");
    }

    @Test
    void should_throw_mapping_exception_if_no_mapping_entry_present_while_resolving_mapping_class() {
        AggregateMapping mapping = new AggregateMapping(ImmutableList.of(new MappingEntry(TestAggregateMapping.class)));
        Throwable thrown = catchThrowable(() -> mapping.mappingClassOf(String.class));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Mapping not found for aggregate class 'java.lang.String'!");
    }

    @Test
    void should_reject_duplicated_mapping_entries_while_constructing() {
        MappingEntry entryOne = new MappingEntry(TestAggregateMapping.class);
        MappingEntry entryTwo = new MappingEntry(AnotherTestAggregateMapping.class);
        Throwable thrown = catchThrowable(() -> new AggregateMapping(ImmutableList.of(entryOne, entryTwo)));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessageContaining("Multiple mapping specified for aggregate class 'ltd.highsoft.framework.hstorage.TestAggregate': ");
        assertThat(thrown).hasMessageContaining("ltd.highsoft.framework.hstorage.TestAggregateMapping");
        assertThat(thrown).hasMessageContaining("ltd.highsoft.framework.hstorage.AnotherTestAggregateMapping");
    }

}
