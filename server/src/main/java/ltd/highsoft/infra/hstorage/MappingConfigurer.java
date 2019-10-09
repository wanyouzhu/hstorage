package ltd.highsoft.infra.hstorage;

import com.google.common.collect.Lists;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MappingConfigurer {

    private final List<MappingEntry> mappingEntries = Lists.newArrayList();

    public void addMappingClass(Class<?> mappingClass) {
        mappingEntries.add(new MappingEntry(mappingClass));
    }

    public void addPackage(String basePackage) {
        mappingEntries.addAll(loadEntriesFromPackage(basePackage));
    }

    private List<MappingEntry> loadEntriesFromPackage(String basePackage) {
        return new ClassPathMappingResolver().resolve(basePackage).stream().map(MappingEntry::new).collect(toList());
    }

    public ModelMapping configure() {
        return new ModelMapping(mappingEntries);
    }

}
