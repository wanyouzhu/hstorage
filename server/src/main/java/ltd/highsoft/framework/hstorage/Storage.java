package ltd.highsoft.framework.hstorage;

public abstract class Storage {

    public abstract void save(Object aggregate);

    public abstract <T> T load(String id, Class<T> clazz);

}
