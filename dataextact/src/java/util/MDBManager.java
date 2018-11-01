package util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.System.out;


/**
 * Created by dylan on 2017/9/23.
 */
public class MDBManager {

    private static final MDBManager instance = new MDBManager();
    private static ComboPooledDataSource cpds = new ComboPooledDataSource(true);
    static Logger logger = Logger.getLogger(MDBManager.class.getName());

    /**
     * 此处可以不配置，采用默认也行
     */
    static {
        cpds.setDataSourceName("mydatasource");
        cpds.setJdbcUrl(ConstantUtils.getValue("c3p0.jdbcUrl").toString());
        try {
            cpds.setDriverClass(ConstantUtils.getValue("c3p0.driverClass").toString());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        cpds.setUser(ConstantUtils.getValue("c3p0.user").toString());
        cpds.setPassword(ConstantUtils.getValue("c3p0.password").toString());
        cpds.setMaxPoolSize(Integer.valueOf(ConstantUtils.getValue("c3p0.maxPoolSize").toString()));
        cpds.setMinPoolSize(Integer.valueOf(ConstantUtils.getValue("c3p0.minPoolSize").toString()));
        cpds.setAcquireIncrement(Integer.valueOf(ConstantUtils.getValue("c3p0.acquireIncrement").toString()));
        cpds.setInitialPoolSize(Integer.valueOf(ConstantUtils.getValue("c3p0.initialPoolSize").toString()));
        cpds.setMaxIdleTime(Integer.valueOf(ConstantUtils.getValue("c3p0.maxIdleTime").toString()));
    }

    private MDBManager() {
    }

    public static MDBManager getInstance() {
        return instance;
    }

    public Connection getConn() {
        try {
            return cpds.getConnection();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        out.print(MDBManager.getInstance().getConn().toString());
    }
}