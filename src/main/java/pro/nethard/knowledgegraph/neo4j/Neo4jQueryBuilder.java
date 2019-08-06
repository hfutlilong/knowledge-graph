package pro.nethard.knowledgegraph.neo4j;

import com.alibaba.druid.util.StringUtils;
import pro.nethard.knowledgegraph.dao.Relation;
import pro.nethard.knowledgegraph.entity.Attribute;
import pro.nethard.knowledgegraph.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * neo4j查询语句构建类
 * 
 * @author hzxuxiang
 *
 */
public class Neo4jQueryBuilder {

	/**
	 * 查询实体语句主要部分
	 * 
	 * @return
	 */
	private static String getQueryCypherPart(String domain, String type, String status, Long originId, String operUser,
			String keywords, String attrName, String attrValue) {
		StringBuilder sb = new StringBuilder();
		sb.append(getMainQuery(domain, originId));

		List<String> conds = new ArrayList<String>();
		if (!StringUtils.isEmpty(type)) {
			conds.add(String.format("n.type=\"%s\"", type));
		}
		if (!StringUtils.isEmpty(operUser)) {
			conds.add(String.format("n.updateuser=\"%s\"", operUser));
		}
		if (!StringUtils.isEmpty(keywords)) {
			conds.add(String.format("n.name contains \"%s\"", keywords));
		}
		if (!StringUtils.isEmpty(attrName)) {
			conds.add(String.format("EXISTS(n.%s)", attrName));
			if (attrValue != null) {
				conds.add(String.format("n.%s contains \"%s\"", attrName, attrValue));
			}
		}
		if (!conds.isEmpty()) {
			sb.append(" where ");
			sb.append(String.join(" and ", conds));
		}
		return sb.toString();
	}

	/**
	 * 获取查询实体语句
	 * 
	 * @return
	 */
	public static String getQueryCypher(String domain, String type, String status, Long originId, String operUser,
			String keywords, String attrName, String attrValue, int pageSize, int curPage) {
		String query = getQueryCypherPart(domain, type, status, originId, operUser, keywords, attrName, attrValue);
		query += String.format(" return n SKIP %d LIMIT %d", (curPage - 1) * pageSize, pageSize);
		return query;
	}

	/**
	 * 获取满足条件实体数量的查询语句
	 * 
	 * @return
	 */
	public static String getQueryCypherCnt(String domain, String type, String status, Long originId, String operUser,
			String keywords, String attrName, String attrValue) {
		String query = getQueryCypherPart(domain, type, status, originId, operUser, keywords, attrName, attrValue);
		query += " return count(1) as cnt";
		return query;
	}

	/**
	 * 根据域和系统ID获取主查询语句
	 * 
	 * @param domain
	 * @param originId
	 * @return
	 */
	private static String getMainQuery(String domain, Long originId) {
		String s;
		if (StringUtils.isEmpty(domain)) {
			if (originId == null) {
				s = "match (n)";
			} else {
				s = String.format("match (n {origin_id:%d})", originId);
			}
		} else {
			if (originId == null) {
				s = String.format("match (n:%s)", domain);
			} else {
				s = String.format("match (n:%s {origin_id:%d})", domain, originId);
			}
		}
		return s;
	}

	private static String getMainQuery(String domain, Long originId, String label) {
		String s;
		if (StringUtils.isEmpty(domain)) {
			if (originId == null) {
				s = String.format("match (%s)", label);
			} else {
				s = String.format("match (%s {origin_id:%d})", label, originId);
			}
		} else {
			if (originId == null) {
				s = String.format("match (%s:%s)", label, domain);
			} else {
				s = String.format("match (%s:%s {origin_id:%d})", label, domain, originId);
			}
		}
		return s;
	}

	/**
	 * 删除实体属性DML语句
	 * 
	 * @param domain
	 * @param originId
	 * @param attrNameList
	 * @return
	 */
	public static String removeProperty(String domain, Long originId, List<String> attrNameList) {
		StringBuilder sb = new StringBuilder();
		sb.append(getMainQuery(domain, originId));
		sb.append(" remove ");
		for (String attrName : attrNameList) {
			sb.append(String.format("n.%s,", attrName));
		}

		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 设置实体属性DML语句
	 * 
	 * @param domain
	 * @param originId
	 * @param attrList
	 * @return
	 */
	public static String setProperty(String domain, Long originId, List<Attribute> attrList) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("merge (n:%s {origin_id:%d})", domain, originId));
		sb.append(" on match set");
		for (Attribute attr : attrList) {
			sb.append(String.format(" n.%s=\"%s\",", attr.getAttrKey(), attr.getAttrValue()));
		}
		sb.append("n.updatetime=timestamp(),");
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 根据 origin_id列表批量查询实体
	 * 
	 * @param domain
	 * @param originIdList
	 * @return
	 */
	public static String queryEntityList(String domain, List<Long> originIdList) {
		StringBuilder sb = new StringBuilder();
		sb.append(getMainQuery(domain, null));
		sb.append(" where n.origin_id in [");
		for (Long originId : originIdList) {
			sb.append(originId);
			sb.append(",");
		}
		return sb.substring(0, sb.length() - 1) + "] return n";
	}

	/**
	 * 查询单个实体节点
	 * 
	 * @param domain
	 * @param originId
	 * @return
	 */
	public static String queryEntity(String domain, Long originId) {
		return getMainQuery(domain, originId) + " return n";
	}

	/**
	 * 查询某个节点的关系
	 * 
	 * @param domain
	 * @param originId
	 * @return
	 */
	public static String queryRelationList(String domain, Long originId) {
		StringBuilder sb = new StringBuilder();
		sb.append(getMainQuery(domain, originId));
		sb.append("-[r]-(m) return m, r, type(r) as rtype");
		return sb.toString();
	}

	/**
	 * 查询某个节点关系的数量
	 * 
	 * @param domain
	 * @param originId
	 * @return
	 */
	public static String queryRelationCnt(String domain, Long originId) {
		StringBuilder sb = new StringBuilder();
		sb.append(getMainQuery(domain, originId));
		sb.append("-[r]-(m) return COUNT(1) as cnt");
		return sb.toString();
	}

	/**
	 * 查询两个节点间的关系
	 * 
	 * @param domain1
	 * @param originId1
	 * @param domain2
	 * @param originId2
	 * @return
	 */
	public static String queryRelation(String domain1, Long originId1, String domain2, Long originId2) {
		StringBuilder sb = new StringBuilder();
		sb.append(getMainQuery(domain1, originId1, "n"));
		sb.append(" ");
		sb.append(getMainQuery(domain2, originId2, "m"));
		sb.append(" match (n)-[r]-(m) return r, m, type(r) as rtype");

		return sb.toString();
	}

	/**
	 * 更新两个节点之间关系的属性
	 * 
	 * @param domain1
	 * @param originId1
	 * @param domain2
	 * @param originId2
	 * @param attrList
	 * @return
	 */
	public static String updateRelationAttribute(String domain1, Long originId1, String domain2, Long originId2,
			List<Attribute> attrList) {
		StringBuilder sb = new StringBuilder();
		sb.append(getMainQuery(domain1, originId1, "n"));
		sb.append(" ");
		sb.append(getMainQuery(domain2, originId2, "m"));
		sb.append(" match (m)-[r]-(n) set");
		for (Attribute attr : attrList) {
			sb.append(String.format(" r.%s=\"%s\"", attr.getAttrKey(), attr.getAttrValue()));
			sb.append(",");
		}

		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 删除两个节点之间关系的属性
	 * 
	 * @param domain1
	 * @param originId1
	 * @param domain2
	 * @param originId2
	 * @param attrNameList
	 * @return
	 */
	public static String removeRelationAttribute(String domain1, Long originId1, String domain2, Long originId2,
			List<String> attrNameList) {
		StringBuilder sb = new StringBuilder();
		sb.append(getMainQuery(domain1, originId1, "n"));
		sb.append(" ");
		sb.append(getMainQuery(domain2, originId2, "m"));
		sb.append(" match (m)-[r]-(n) remove ");
		for (String attrName : attrNameList) {
			sb.append(String.format("r.%s,", attrName));
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 新增实体的SQL语句
	 * 
	 * @param entity
	 * @return
	 */
	public static String addEntity(Entity entity) {
		List<String> attrList = new ArrayList<String>();
		if (entity.getAttributes().size() > 0) {
			for (Attribute attr : entity.getAttributes()) {
				attrList.add(String.format("%s:\"%s\"", attr.getAttrKey(), attr.getAttrValue()));
			}
			return String.format("create (n:%s {origin_id:%s, name:\"%s\", type:\"%s\", %s}) ", entity.getDomain(),
					entity.getOriginId(), entity.getName(), entity.getType(), String.join(", ", attrList));
		} else {
			return String.format("create (n:%s {origin_id:%s, name:\"%s\", type:\"%s\"}) ", entity.getDomain(),
					entity.getOriginId(), entity.getName(), entity.getType());
		}
	}

	/**
	 * 增加两实体间关系的SQL语句
	 * 
	 * @return
	 */
	public static String addRelation(String domain1, Long originId1, Relation relation) {
		List<String> attrList = new ArrayList<String>();
		if (relation.getRelAttr().size() > 0) {
			for (Attribute attr : relation.getRelAttr()) {
				attrList.add(String.format("r.%s=\"%s\"", attr.getAttrKey(), attr.getAttrValue()));
			}
			return String.format("%s %s merge (n)-[r:%s]-(m) set %s",
					getMainQuery(relation.getEntity().getDomain(), relation.getEntity().getOriginId(), "m"),
					relation.getRelType(), String.join(", ", attrList));
		} else {
			return String.format("%s %s merge (n)-[r:%s]-(m)", getMainQuery(domain1, originId1),
					getMainQuery(relation.getEntity().getDomain(), relation.getEntity().getOriginId(), "m"),
					relation.getRelType());
		}
	}
}
