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
        assertThat(mapping.getCollectionOf(TestAggregate.class)).isEqualTo("test_aggregates");
        assertThat(mapping.getMappingClassOf(TestAggregate.class)).isEqualTo(TestAggregateMapping.class);
    }

    @Test
    void should_be_able_to_retrieve_id_from_aggregate() {
        TestAggregate aggregate = new TestAggregate("one", "van");
        assertThat(mapping.getIdOf(aggregate)).isEqualTo("one");
    }

    @Test
    void should_reject_aggregates_without_id_field_during_id_retrieving() {
        Object object = new TypeWithoutIdField("Van");
        Throwable thrown = catchThrowable(() -> mapping.getIdOf(object));
        assertThat(thrown).isInstanceOf(MappingException.class);
        assertThat(thrown).hasMessage("Missing 'id' field in type 'ltd.highsoft.framework.hstorage.TypeWithoutIdField'!");
    }

}
