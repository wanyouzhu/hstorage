package ltd.highsoft.framework.hstorage;

public interface StatePersister {

    void saveState(AggregateState state);

    AggregateState loadState(String id, Class<?> clazz);
    
}
