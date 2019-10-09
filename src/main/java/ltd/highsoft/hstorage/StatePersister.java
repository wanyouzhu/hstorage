package ltd.highsoft.hstorage;

public interface StatePersister {

    void saveState(AggregateState state);

    AggregateState loadState(String collection, String id);

}
