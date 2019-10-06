package ltd.highsoft.framework.hstorage;

import org.junit.jupiter.api.*;
import org.springframework.jdbc.core.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcStatePersisterTest {

    private TestDatabase testDatabase;

    @BeforeEach
    void setUp() {
        testDatabase = new TestDatabase();
        testDatabase.recreateCollectionTable(testDatabase.jdbcTemplate);
    }

    @Test
    void should_be_able_save_state_to_database() {
        AggregateState state = new AggregateState("one", "{\"id\":\"one\"}", Instant.EPOCH);
        StatePersister persister = new JdbcStatePersister(testDatabase.jdbcTemplate);
        persister.saveState(state);
        Map<String, Object> loaded = testDatabase.jdbcTemplate.queryForMap("select * from entities");
        assertThat(loaded.get("id")).isEqualTo("one");
        assertThat(loaded.get("state").toString()).isEqualTo("{\"id\": \"one\"}");
        assertThat(loaded.get("timestamp")).isEqualTo(Timestamp.from(Instant.EPOCH));
    }

}