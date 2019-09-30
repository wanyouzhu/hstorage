package hkeeper;

public class DummyJsonMapper implements JsonMapper {

	private final String resultJson;

    public DummyJsonMapper(String resultJson) {
        this.resultJson = resultJson;
	}

    @Override
    public String toJson(Object source) {
        return resultJson;
    }

}
