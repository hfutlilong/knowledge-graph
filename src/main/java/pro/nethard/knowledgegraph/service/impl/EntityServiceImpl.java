package pro.nethard.knowledgegraph.service.impl;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.springframework.stereotype.Service;
import pro.nethard.knowledgegraph.dao.EntityDao;
import pro.nethard.knowledgegraph.dao.connect.DruidConnPool;
import pro.nethard.knowledgegraph.entity.Entity;
import pro.nethard.knowledgegraph.service.EntityService;

import java.sql.SQLException;
import java.util.List;

@Service
public class EntityServiceImpl implements EntityService {
    @Override
    public List<Entity> getEntityList(String domain, List<Long> originIdList) throws SQLException {
        DruidPooledConnection conn = DruidConnPool.getInstance().getConnection();
        List<Entity> entityList = null;
        entityList = EntityDao.getInstance().queryEntityList(conn, domain, originIdList);
        return entityList;
    }
}
