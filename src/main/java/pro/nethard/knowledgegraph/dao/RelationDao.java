package pro.nethard.knowledgegraph.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.nethard.knowledgegraph.neo4j.Neo4jQueryBuilder;
import pro.nethard.knowledgegraph.entity.Attribute;
import pro.nethard.knowledgegraph.entity.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelationDao {

	private static final Logger LOG = LoggerFactory.getLogger(RelationDao.class);

	private static final RelationDao instance = new RelationDao();

	private RelationDao() {
	}

	public static RelationDao getInstance() {
		return instance;
	}

	/**
	 * 查询某个节点的关系
	 * 
	 * @param conn
	 * @param domain
	 * @param originId
	 * @return
	 * @throws SQLException
	 */
	public List<Relation> queryRelationList(Connection conn, String domain, Long originId) throws SQLException {
		String query = Neo4jQueryBuilder.queryRelationList(domain, originId);

		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		List<Relation> relationList = new ArrayList<Relation>();
		while (rs.next()) {
			String rslt = rs.getString("m");
			Map<String, String> userMap = JSON.parseObject(rslt, new TypeReference<Map<String, String>>() {
			});
			Entity entity = Entity.buildFromMap(userMap);

			String relType = rs.getString("rtype");
			String relAttrStr = rs.getString("r");
			Map<String, String> relAttr = JSON.parseObject(relAttrStr, new TypeReference<Map<String, String>>() {
			});
			relationList.add(Relation.buildFromMap(relType, entity, relAttr));
		}
		ps.close();
		return relationList;
	}

	/**
	 * 查询某个实体节点的关系数量
	 * 
	 * @param conn
	 * @param domain
	 * @param originId
	 * @return
	 * @throws SQLException
	 */
	public int queryRelationCnt(Connection conn, String domain, Long originId) throws SQLException {
		String query = Neo4jQueryBuilder.queryRelationCnt(domain, originId);

		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		int cnt = 0;
		while (rs.next()) {
			String a = rs.getString("cnt");
			cnt = Integer.valueOf(a);
		}
		ps.close();
		return cnt;
	}

	/**
	 * 查询两个节点的关系类型
	 * 
	 * @param conn
	 * @param domain1
	 * @param originId1
	 * @param domain2
	 * @param originId2
	 * @return
	 * @throws SQLException
	 */
	public Relation queryRelation(Connection conn, String domain1, Long originId1, String domain2, Long originId2)
			throws SQLException {
		String query = Neo4jQueryBuilder.queryRelation(domain1, originId1, domain2, originId2);

		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		Relation relation = null;
		while (rs.next()) {
			String rslt = rs.getString("m");
			Map<String, String> userMap = JSON.parseObject(rslt, new TypeReference<Map<String, String>>() {
			});
			Entity entity = Entity.buildFromMap(userMap);

			String relType = rs.getString("rtype");
			String relAttrStr = rs.getString("r");
			Map<String, String> relAttr = JSON.parseObject(relAttrStr, new TypeReference<Map<String, String>>() {
			});
			relation = Relation.buildFromMap(relType, entity, relAttr);
		}
		ps.close();
		return relation;
	}

	/**
	 * 更新两个实体间关系的属性
	 * 
	 * @param conn
	 * @param domain1
	 * @param originId1
	 * @param domain2
	 * @param originId2
	 * @param attrList
	 * @return
	 * @throws SQLException
	 */
	public boolean updateRelationAttribute(Connection conn, String domain1, Long originId1, String domain2,
			Long originId2, List<Attribute> attrList) throws SQLException {
		String query = Neo4jQueryBuilder.updateRelationAttribute(domain1, originId1, domain2, originId2, attrList);
		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		boolean rs = ps.execute();
		ps.close();
		return rs;
	}

	/**
	 * 删除两个实体间关系的属性
	 * 
	 * @param conn
	 * @param domain1
	 * @param originId1
	 * @param domain2
	 * @param originId2
	 * @param attrNameList
	 * @return
	 * @throws SQLException
	 */
	public boolean removeRelationAttribute(Connection conn, String domain1, Long originId1, String domain2,
			Long originId2, List<String> attrNameList) throws SQLException {
		String query = Neo4jQueryBuilder.removeRelationAttribute(domain1, originId1, domain2, originId2, attrNameList);
		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		boolean rs = ps.execute();
		ps.close();
		return rs;
	}

	/**
	 * 增加两节点间关系
	 * @param conn
	 * @param domain1
	 * @param originId1
	 * @param relation
	 * @return
	 * @throws SQLException
	 */
	public boolean addRelation(Connection conn, String domain1, Long originId1, Relation relation) throws SQLException {
		String query = Neo4jQueryBuilder.addRelation(domain1, originId1, relation);
		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		boolean rs = ps.execute();
		ps.close();
		return rs;
	}
}
