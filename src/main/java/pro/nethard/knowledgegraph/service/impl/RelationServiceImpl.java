package pro.nethard.knowledgegraph.service.impl;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.nethard.knowledgegraph.dao.Relation;
import pro.nethard.knowledgegraph.dao.RelationDao;
import pro.nethard.knowledgegraph.neo4j.DruidConnPool;
import pro.nethard.knowledgegraph.service.RelationService;

import java.sql.SQLException;
import java.util.List;

@Service
public class RelationServiceImpl implements RelationService {
    @Override
    public List<Relation> getRelationList(String domain, Long originId) throws SQLException {
        DruidPooledConnection conn = DruidConnPool.getInstance().getConnection();

        List<Relation> relList = RelationDao.getInstance().queryRelationList(conn, domain, originId);

        conn.close();
        return relList;
    }

    @Override
    public void addRelation(String domain1, Long originId1, Relation relation) throws SQLException {
        DruidPooledConnection conn = DruidConnPool.getInstance().getConnection();
        RelationDao.getInstance().addRelation(conn, domain1, originId1, relation);
        conn.close();
    }
}