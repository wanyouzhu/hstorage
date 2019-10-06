package ltd.highsoft.framework.hstorage;

public class Storage {

    private final AggregateMapper mapper;
    private final StatePersister persister;

    public Storage(StatePersister persister, TimeService timeService) {
        this.mapper = new AggregateMapper(timeService);
        this.persister = persister;
    }

    public void save(Object aggregate) {
        persister.saveState(mapper.mapToState(aggregate));
    }

    public <T> T load(String id, Class<T> clazz) {
        return mapper.mapToAggregate(persister.loadState("entities", id), clazz);
    }

}
