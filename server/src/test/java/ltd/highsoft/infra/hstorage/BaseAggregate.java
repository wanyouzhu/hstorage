package ltd.highsoft.infra.hstorage;

@SuppressWarnings("FieldCanBeLocal")
class BaseAggregate {

    private final String id;
    private final String name;

    BaseAggregate(String id, String name) {
        this.id = id;
        this.name = name;
    }

}
