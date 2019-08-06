package pro.nethard.knowledgegraph.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pro.nethard.knowledgegraph.entity.Entity;
import pro.nethard.knowledgegraph.service.EntityService;

import java.sql.SQLException;
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
}
