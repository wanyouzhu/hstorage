package ltd.highsoft.framework.hstorage;

public class Storage {

    private final AggregateMarshaller marshaller;
    private final AggregateMapper mapper;
    private final StatePersister persister;
    private final TimeService timeService;

    public Storage(StatePersister persister, TimeService timeService) {
        this.marshaller = new AggregateMarshaller();
        this.mapper = new AggregateMapper(timeService);
        this.persister = persister;
        this.timeService = timeService;
    }

    public void save(Object aggregate) {
        persister.saveState(mapper.mapToState(aggregate));
    }

    public <T> T load(String id, Class<T> clazz) {
        return mapper.mapToAggregate(persister.loadState("entities", id), clazz);
    }

}
