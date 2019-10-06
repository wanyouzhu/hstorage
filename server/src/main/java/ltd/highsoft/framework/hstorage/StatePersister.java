package ltd.highsoft.framework.hstorage;

public interface StatePersister {

    void saveState(String collection, AggregateState state);

    AggregateState loadState(String collection, String id, Class<?> clazz);
    
}
