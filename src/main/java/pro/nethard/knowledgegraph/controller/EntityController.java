package pro.nethard.knowledgegraph.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pro.nethard.knowledgegraph.entity.Attribute;
import pro.nethard.knowledgegraph.entity.Entity;
import pro.nethard.knowledgegraph.service.EntityService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("entity")
public class EntityController {
    @Autowired
    private EntityService entityService;

    @RequestMapping("queryAll")
    @ResponseBody
    public String queryAll() {
        try {
            List<Entity> entityList = entityService.getEntityList("商品", Arrays.asList(1L));
            return JSON.toJSONString(entityList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "failed";
    }

    @RequestMapping("addEntity")
    @ResponseBody
    public String addEntity() {
//        Entity entity = new Entity();
//        entity.setDomain("商品");
//        entity.setType("虚拟组合商品");
//        entity.setOriginId(1L);
//        entity.setName("二分东");
//
//        List<Attribute> attributes = new ArrayList<>();
//        attributes.add(new Attribute("颜色","黄色"));
//        attributes.add(new Attribute("重量", "1kg"));
//        entity.setAttributes(attributes);

        Entity entity = new Entity();
        entity.setOriginId(2L);
        entity.setDomain("商品");
        entity.setName("iPhone 6s Plus");
        entity.setType("普通商品");
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("存储","32G"));
        attributes.add(new Attribute("产地", "东莞"));
        entity.setAttributes(attributes);

        try {
            entityService.addEntity(entity);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "failed";
    }
}
