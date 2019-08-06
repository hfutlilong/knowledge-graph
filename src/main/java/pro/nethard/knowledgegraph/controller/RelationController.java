package pro.nethard.knowledgegraph.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pro.nethard.knowledgegraph.dao.Relation;
import pro.nethard.knowledgegraph.entity.Entity;
import pro.nethard.knowledgegraph.service.RelationService;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("relation")
public class RelationController {
    @Autowired
    private RelationService relationService;

    @RequestMapping("queryAll")
    @ResponseBody
    public String queryAll() {
        try {
            List<Relation> relationList = relationService.getRelationList("商品", 2L);
            return JSON.toJSONString(relationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "failed";
    }

    @RequestMapping("addRelation")
    @ResponseBody
    public String addRelation() {
        try {
            Entity entity = new Entity();
            entity.setOriginId(2L);
            entity.setDomain("商品");
            entity.setName("iPhone 6s Plus");
            entity.setType("普通商品");

            Relation relation = new Relation();
            relation.setEntity(entity);
            relation.setRelType("成分");

            relationService.addRelation("商品", 1L, relation);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "false";
    }
}
