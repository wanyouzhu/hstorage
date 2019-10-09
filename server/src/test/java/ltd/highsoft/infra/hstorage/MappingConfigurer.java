package ltd.highsoft.infra.hstorage;

import org.assertj.core.util.Lists;

import java.util.List;

public class MappingConfigurer {

    private final List<MappingEntry> mappingEntries = Lists.newArrayList();

    public void addMappingClass(Class<?> mappingClass) {
        mappingEntries.add(new MappingEntry(mappingClass));
    }

    public ModelMapping configure() {
        return new ModelMapping(mappingEntries);
    }

}
