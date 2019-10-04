package ltd.highsoft.hkeeper;

public abstract class Store {

    public abstract void save(Object entity);

    public abstract <T> T load(String id, Class<T> clazz);

}
