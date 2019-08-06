package pro.nethard.knowledgegraph.service;

import pro.nethard.knowledgegraph.entity.Entity;

import java.sql.SQLException;
import java.util.List;

public interface EntityService {
    List<Entity> getEntityList(String domain, List<Long> originIdList) throws SQLException;

    void addEntity(Entity entity) throws SQLException;
}
