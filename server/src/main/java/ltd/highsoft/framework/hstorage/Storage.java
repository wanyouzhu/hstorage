package ltd.highsoft.framework.hstorage;

public class Storage {

    private final AggregateMarshaller marshaller;
    private final AggregateMapping mapping;
    private final StatePersister persister;
    private final TimeService timeService;

    public Storage(AggregateMapping mapping, StatePersister persister, TimeService timeService) {
        this.marshaller = new AggregateMarshaller();
        this.mapping = mapping;
        this.persister = persister;
        this.timeService = timeService;
    }

    public void save(Object aggregate) {
        persister.saveState(buildAggregateState(aggregate));
    }

    private AggregateState buildAggregateState(Object aggregate) {
        return new AggregateState(
            "entities", mapping.getIdOf(aggregate), marshaller.marshal(aggregate), timeService.now()
        );
    }

    public <T> T load(String id, Class<T> clazz) {
        return marshaller.unmarshal(persister.loadState("entities", id).content(), clazz);
    }

}
