package ltd.highsoft.framework.hstorage;

public interface StatePersister {

    void saveState(AggregateState state);

    AggregateState loadState(String collection, String id, Class<?> clazz);
    
}
