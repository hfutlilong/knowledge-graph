package pro.nethard.knowledgegraph.neo4j;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

public class DruidConnPool {

    private static final Logger LOG = LoggerFactory.getLogger(DruidConnPool.class);

    private static DruidConnPool instance = new DruidConnPool();
    private static DruidDataSource DATA_SOURCE = null;

    static {
        Properties properties = new Properties();
        try {
            File classPath = new File(DruidConnPool.class.getResource("/").getPath());
            String neo4jConfFilePath = classPath + "/neo4j.properties";
            //String neo4jConfFilePath = "D:\\workspace_ee\\KGManager\\src\\main\\resources\\dev\\neo4j.properties";
            FileInputStream fis = new FileInputStream(neo4jConfFilePath);
            properties.load(fis);
            fis.close();

            DATA_SOURCE = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DruidConnPool() {
    }

    public static DruidConnPool getInstance() {
        return instance;
    }

    public DruidPooledConnection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    public void close() {
        if (DATA_SOURCE != null) {
            DATA_SOURCE.close();
        }
    }
}
