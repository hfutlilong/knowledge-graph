package pro.nethard.knowledgegraph.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.nethard.knowledgegraph.cql.Neo4jQueryBuilder;
import pro.nethard.knowledgegraph.entity.Attribute;
import pro.nethard.knowledgegraph.entity.Entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * neo4j的数据库操作类
 */
public class EntityDao {

	private static final Logger LOG = LoggerFactory.getLogger(EntityDao.class);

	private static final EntityDao instance = new EntityDao();

	private EntityDao() {
	}

	public static EntityDao getInstance() {
		return instance;
	}

	/**
	 * 查询单个实体节点
	 * 
	 * @param conn
	 * @param domain
	 * @param originId
	 * @return
	 * @throws SQLException
	 */
	public Entity queryEntity(Connection conn, String domain, Long originId) throws SQLException {
		String query = Neo4jQueryBuilder.queryEntity(domain, originId);

		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		Entity entity = null;
		while (rs.next()) {
			String rslt = rs.getString("n");
			Map<String, String> userMap = JSON.parseObject(rslt, new TypeReference<Map<String, String>>() {
			});
			entity = Entity.buildFromMap(userMap);
		}
		ps.close();
		return entity;
	}

	/**
	 * 批量查询实体列表
	 * 
	 * @param conn
	 * @param domain
	 * @param originIdList
	 * @return
	 * @throws SQLException
	 */
	public List<Entity> queryEntityList(Connection conn, String domain, List<Long> originIdList) throws SQLException {
		String query = Neo4jQueryBuilder.queryEntityList(domain, originIdList);

		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		List<Entity> entityList = new ArrayList<Entity>();
		while (rs.next()) {
			String rslt = rs.getString("n");
			Map<String, String> userMap = JSON.parseObject(rslt, new TypeReference<Map<String, String>>() {
			});
			entityList.add(Entity.buildFromMap(userMap));
		}
		ps.close();
		return entityList;
	}

	/**
	 * 查询实体方法
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Entity> queryEntityList(Connection conn, String domain, String type, String status, Long originId,
			String operUser, String keywords, String attrName, String attrValue, int pageSize, int curPage)
			throws SQLException {
		String query = Neo4jQueryBuilder.getQueryCypher(domain, type, status, originId, operUser, keywords, attrName,
				attrValue, pageSize, curPage);

		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		ResultSet rs = ps.executeQuery();

		List<Entity> entityList = new ArrayList<Entity>();
		while (rs.next()) {
			String rslt = rs.getString("n");
			Map<String, String> userMap = JSON.parseObject(rslt, new TypeReference<Map<String, String>>() {
			});
			entityList.add(Entity.buildFromMap(userMap));
		}
		ps.close();
		return entityList;
	}

	/**
	 * 查询实体数量的方法
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int queryCntOfEntity(Connection conn, String domain, String type, String status, Long originId,
			String operUser, String keywords, String attrName, String attrValue) throws SQLException {
		String query = Neo4jQueryBuilder.getQueryCypherCnt(domain, type, status, originId, operUser, keywords, attrName,
				attrValue);

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
	 * 更新实体属性
	 * 
	 * @param domain
	 * @param originId
	 * @param attributes
	 * @return
	 * @throws SQLException
	 */
	public boolean updateEntityAttribute(Connection conn, String domain, Long originId, List<Attribute> attributes)
			throws SQLException {
		String query = Neo4jQueryBuilder.setProperty(domain, originId, attributes);
		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		boolean rs = ps.execute();
		ps.close();
		return rs;
	}

	/**
	 * 删除实体属性
	 * 
	 * @param domain
	 * @param originId
	 * @param attrList
	 * @return
	 * @throws SQLException
	 */
	public boolean removeEntityAttribute(Connection conn, String domain, Long originId, List<String> attrList)
			throws SQLException {
		String query = Neo4jQueryBuilder.removeProperty(domain, originId, attrList);
		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		boolean rs = ps.execute();
		ps.close();
		return rs;
	}

	/**
	 * 新增实体
	 * 
	 * @param conn
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	public boolean addEntity(Connection conn, Entity entity) throws SQLException {
		String query = Neo4jQueryBuilder.addEntity(entity);
		LOG.debug("Query string: {}", query);
		PreparedStatement ps = conn.prepareStatement(query);
		boolean rs = ps.execute();
		ps.close();
		return rs;
	}
}
