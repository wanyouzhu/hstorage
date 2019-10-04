package ltd.highsoft.framework.hstore;

public abstract class Store {

    public abstract void save(Object aggregate);

    public abstract <T> T load(String id, Class<T> clazz);

}
