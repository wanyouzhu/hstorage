package ltd.highsoft.infra.hstorage;

import com.google.common.collect.Lists;

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
