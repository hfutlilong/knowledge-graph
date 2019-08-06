package pro.nethard.knowledgegraph.entity;

public class Attribute {
	
	private String attrKey;
	
	private String attrValue;
	
	public Attribute() {
		
	}

	public Attribute(String attrKey, String attrValue) {
		this.attrKey = attrKey;
		this.attrValue = attrValue;
	}
	
	public String getAttrKey() {
		return attrKey;
	}

	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
}
