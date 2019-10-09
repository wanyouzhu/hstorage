package ltd.highsoft.infra.hstorage;

public interface StatePersister {

    void saveState(AggregateState state);

    AggregateState loadState(String collection, String id);

}
