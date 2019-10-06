package ltd.highsoft.framework.hstorage;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
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

    public void recreateCollectionTable() {
        jdbcTemplate.execute("drop table if exists entities");
        jdbcTemplate.execute(
            "create table entities\n" +
                "(\n" +
                "    id        varchar(50) not null primary key,\n" +
                "    state     jsonb       not null,\n" +
                "    timestamp timestamp   not null\n" +
                ")"
        );
    }

    public Map<String, Object> getSavedAggregateState() {
        return jdbcTemplate.queryForMap("select * from entities");
    }

    public JdbcOperations jdbcTemplate() {
        return jdbcTemplate;
    }

}