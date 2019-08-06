package pro.nethard.knowledgegraph.dao;

import pro.nethard.knowledgegraph.entity.Attribute;
import pro.nethard.knowledgegraph.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Relation {
    private String relType;

    private Entity entity;


    private List<Attribute> relAttr = new ArrayList<Attribute>();

    public Relation() {
    }

    public Relation(String relType, Entity entity, List<Attribute> relAttrList) {
        this.relType = relType;
        this.entity = entity;
        this.relAttr = relAttrList;
    }

    public String getRelType() {
        return relType;
    }

    public void setRelType(String relType) {
        this.relType = relType;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<Attribute> getRelAttr() {
        return relAttr;
    }

    public void setRelAttr(List<Attribute> relAttr) {
        this.relAttr = relAttr;
    }

    public void putAttribute(String name, String value) {
        this.relAttr.add(new Attribute(name, value));
    }

    public static Relation buildFromMap(String relType, Entity entity, Map<String, String> attrs) {
        Set<Entry<String, String>> entries = attrs.entrySet();
        Relation relation = new Relation(relType, entity, new ArrayList<Attribute>());
        for (Entry<String, String> entry : entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            switch (name) {
                case "endId":
                    break;
                case "startId":
                    break;
                case "id":
                    break;
                case "type":
                    relType = value;
                    break;
                default:
                    relation.putAttribute(name, value);
            }
        }
        return relation;
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(relType);
        sb.append(": ");
        sb.append(entity.toString());
        sb.append("[");
        if (relAttr != null) {
            for (Attribute attr : relAttr) {
                sb.append(String.format("%s=%s, ", attr.getAttrKey(), attr.getAttrValue()));
            }
        }
        sb.append("]");
        sb.append("}");
        return sb.toString();
    }

}
