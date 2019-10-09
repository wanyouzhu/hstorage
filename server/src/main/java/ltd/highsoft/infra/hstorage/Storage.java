package ltd.highsoft.infra.hstorage;

public class Storage {

    private final AggregateMarshaller marshaller;
    private final ModelMapping mapping;
    private final StatePersister persister;
    private final TimeService timeService;

    public Storage(ModelMapping mapping, StatePersister persister, TimeService timeService) {
        this.marshaller = new AggregateMarshaller(mapping);
        this.mapping = mapping;
        this.persister = persister;
        this.timeService = timeService;
    }

    public void save(Object aggregate) {
        persister.saveState(buildAggregateState(aggregate));
    }

    private AggregateState buildAggregateState(Object aggregate) {
        return new AggregateState(
            mapping.collectionOf(aggregate.getClass()), mapping.idOf(aggregate),
            marshaller.marshal(aggregate), timeService.now()
        );
    }

    public <T> T load(String id, Class<T> clazz) {
        return marshaller.unmarshal(persister.loadState(mapping.collectionOf(clazz), id).content(), clazz);
    }

}
