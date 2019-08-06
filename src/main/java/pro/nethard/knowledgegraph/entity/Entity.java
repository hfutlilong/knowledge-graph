package pro.nethard.knowledgegraph.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Entity {
	private String domain;
	
	private String type;
	
	private Long originId;
	
	private String name;
	
	private List<Attribute> attributes = new ArrayList<Attribute>();
	
	private String updateUser;
	
	private Long updateTime;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getOriginId() {
		return originId;
	}

	public void setOriginId(Long originId) {
		this.originId = originId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}	
	
	public void putAttribute (String key, String value) {
		if(attributes == null) {
			attributes = new ArrayList<Attribute>();
		}
		attributes.add(new Attribute(key, value));
	}
	public static Entity buildFromMap(Map<String, String> attrs) {
		Entity entity = new Entity();
		Set<Entry<String, String>> entries = attrs.entrySet(); 
		for(Entry <String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			switch(name) {
			case "labels" :
				entity.setDomain(value.replace("\"", "").replace("[", "").replace("]", ""));
				break;
			case "type":
				entity.setType(value);
				break;
			case "origin_id":
				entity.setOriginId(Long.parseLong(value));
				break;
			case "name":
				entity.setName(value);
				break;
			case "updateuser":
				entity.setUpdateUser(value);
				break;
			case "updatetime":
				entity.setUpdateTime(Long.parseLong(value));
				break;
			case "id":
				break;
			case "entry_id":
				break;
			default: 
				entity.putAttribute(name, value);
			}			
		}
		return entity;
	}
	
	public String toString() {
		List<String> attrList = new ArrayList<String>();
		if(domain != null) {
			attrList.add(String.format("domain=%s", domain));
		}
		
		if(type != null) {
			attrList.add(String.format("type=%s", type));
		}
		
		if(originId != null) {
			attrList.add(String.format("originId=%s", originId));
		}
		
		if(name != null) {
			attrList.add(String.format("name=%s", name));
		}
		
		if(attributes != null) {
			for(Attribute attr : attributes) {
				attrList.add(String.format("%s=%s", attr.getAttrKey(), attr.getAttrValue()));
			}
		}
		
		if(updateUser != null) {
			attrList.add(String.format("updateUser=%s", updateUser));
		}
		
		if(updateTime != null) {
			attrList.add(String.format("updateTIme=%s", updateTime));
		}
		
		return "[" + String.join(", ", attrList) + "]";
	}
}
