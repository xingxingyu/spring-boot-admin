package dao;

import org.apache.log4j.Logger;
import util.MDBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by dylan on 2017/9/10.
 */
public class OperatorImpl implements OperatorDao {
    static Logger logger = Logger.getLogger(OperatorImpl.class.getName());

    @Override
    public Map<Integer, String> getDepartmentMap() {
        Connection conn = MDBManager.getInstance().getConn();
        String sql = "SELECT DISTINCT department_id,name FROM  DEPARTMENT";
        Map<Integer, String> deptMap = new HashMap<>();
        PreparedStatement psql;
        try {
            conn.setAutoCommit(false);
            psql = conn.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                Integer deptId = rs.getInt(1);
                String dept = rs.getString(2);
                deptMap.put(deptId, dept);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());

        } finally {
            try {
                conn.close();
                return deptMap;
            } catch (SQLException e) {
                logger.error(e.getMessage());
                return deptMap;
            }
        }
    }

    @Override
    public Map<Integer, String> getOperatorMap() {
        Connection conn = MDBManager.getInstance().getConn();
        String sql = "select distinct operator_id,operator from operator";
        Map<Integer, String> operateMap = new HashMap<>();
        PreparedStatement psql;
        try {
            conn.setAutoCommit(false);
            psql = conn.prepareStatement(sql);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                Integer operId = rs.getInt(1);
                String oper = rs.getString(2);
                operateMap.put(operId, oper);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                conn.close();
                return operateMap;
            } catch (SQLException e) {
                e.printStackTrace();
                return operateMap;
            }
        }
    }
}
