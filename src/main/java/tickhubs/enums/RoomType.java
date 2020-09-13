package tickhubs.enums;

public enum RoomType {
	PUBLIC("public"), PRIVATE("private");

	private String name;

	private RoomType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
