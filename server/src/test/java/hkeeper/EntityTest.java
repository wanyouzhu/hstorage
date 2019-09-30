package hkeeper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class EntityTest {

    @Test
    void should_to_string_correctly() {
        Entity entity = new Entity("1", "json", Instant.parse("2019-01-01T12:32:48Z"));
        assertThat(entity.toString()).isEqualTo("Entity {id=1, state=json, timestamp=2019-01-01T12:32:48Z}");
    }

}