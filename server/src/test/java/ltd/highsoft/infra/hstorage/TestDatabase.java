package ltd.highsoft.infra.hstorage;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

public class TestDatabase {

    private JdbcOperations jdbcTemplate;

    public TestDatabase() {
        this.jdbcTemplate = new JdbcTemplate(createTestDataSource());
    }

    private static DataSource createTestDataSource() {
        return new SingleConnectionDataSource(
            "jdbc:postgresql://localhost:5432/hkeeper",
            "postgres", "postgres", false
        );
    }

    public JdbcOperations jdbcTemplate() {
        return jdbcTemplate;
    }

    public void recreateCollectionTable(String collection) {
        jdbcTemplate.execute("drop table if exists " + collection);
        jdbcTemplate.execute("" +
            "create table " + collection + "\n" +
            "(\n" +
            "    id        varchar(50) not null primary key,\n" +
            "    state     jsonb       not null,\n" +
            "    timestamp timestamp   not null\n" +
            ")"
        );
    }

    public Map<String, Object> getSavedAggregateState(String collection) {
        return jdbcTemplate.queryForMap("select * from " + collection);
    }

    public void recreateCollectionTableWithoutPrimaryKey(String collection) {
        jdbcTemplate.execute("drop table if exists " + collection);
        jdbcTemplate.execute("" +
            "create table " + collection + "\n" +
            "(\n" +
            "    id        varchar(50) not null,\n" +
            "    state     jsonb       not null,\n" +
            "    timestamp timestamp   not null\n" +
            ")"
        );
    }

    public void addTestData(String collection, String id, String state, Instant timestamp) {
        jdbcTemplate.update(
            "insert into " + collection + " values(?, ? ::jsonb, ?)", id, state, Timestamp.from(timestamp)
        );
    }

}