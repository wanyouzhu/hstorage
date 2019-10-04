package ltd.highsoft.hkeeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcOperations;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostgresJsonbStoreTest {

    private @Resource JdbcOperations jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("drop table if exists entities");
        jdbcTemplate.execute(
            "create table entities\n" +
                "(\n" +
                "    id        varchar(50) primary key,\n" +
                "    state     jsonb       not null,\n" +
                "    timestamp timestamptz not null\n" +
                ")"
        );
    }

    @Test
    void should_save_entity_state_into_database() {
        Store store = new PostgresJsonbStore(jdbcTemplate);
        Entity entity = new Entity("0001", "Van");
        store.save(entity);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from entities");
        assertThat(rows.size()).isEqualTo(1);
    }

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private static class Entity {
        private String id;
        private String name;

        Entity(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
