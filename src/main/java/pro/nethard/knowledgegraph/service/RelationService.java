package pro.nethard.knowledgegraph.service;

import pro.nethard.knowledgegraph.dao.Relation;

import java.sql.SQLException;
import java.util.List;

public interface RelationService {
    List<Relation> getRelationList(String domain, Long originId) throws SQLException;

    void addRelation(String domain1, Long originId1, Relation relation) throws SQLException;
}
