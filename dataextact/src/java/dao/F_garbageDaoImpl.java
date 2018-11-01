package dao;

import entity.F_garbage;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import util.MDBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Created by dylan on 2017/7/22.
 */
public class F_garbageDaoImpl implements F_garbageDao {
    static Logger logger = Logger.getLogger(F_garbageDaoImpl.class.getName());

    @Override
    public void insertGarbage(List<F_garbage> list) {
        Connection conn = MDBManager.getInstance().getConn();
        String sql = "replace into f_garbage(category_id,category_name,department,ip,net_weight,sn,up_date,operator) values (?,?,?,?,?,?,?,?)";
        PreparedStatement psql;
        try {
            conn.setAutoCommit(false);
            psql = conn.prepareStatement(sql);
            for (F_garbage f_garbage : list) {
                psql.setInt(1, StringUtils.isBlank(f_garbage.getCategoryId()) ? -1 : Integer.parseInt(f_garbage.getCategoryId()));
                psql.setString(2, f_garbage.getCategoryName());
                psql.setString(3, f_garbage.getDepartment());
                psql.setString(4, f_garbage.getIp());
                psql.setDouble(5, f_garbage.getNetWeight());
                psql.setLong(6, f_garbage.getSn());
                psql.setTimestamp(7, f_garbage.getUp_Date());
                psql.setString(8, f_garbage.getOperator());
                psql.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    public void insertGarbage(F_garbage f_garbage) {
        Connection conn = MDBManager.getInstance().getConn();
        String sql = "replace into f_garbage(category_id,category_name,department,ip,net_weight,sn,up_date,operator) values (?,?,?,?,?,?,?,?)";
        PreparedStatement psql;
        try {
            conn.setAutoCommit(false);
            psql = conn.prepareStatement(sql);

                psql.setInt(1, StringUtils.isBlank(f_garbage.getCategoryId()) ? -1 : Integer.parseInt(f_garbage.getCategoryId()));
                psql.setString(2, f_garbage.getCategoryName());
                psql.setString(3, f_garbage.getDepartment());
                psql.setString(4, f_garbage.getIp());
                psql.setDouble(5, f_garbage.getNetWeight());
                psql.setLong(6, f_garbage.getSn());
                psql.setTimestamp(7, f_garbage.getUp_Date());
                psql.setString(8, f_garbage.getOperator());
                psql.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    @Override
    public Long getMaxFid(String ip) {
        Connection conn = MDBManager.getInstance().getConn();
        String sql = "select max(sn) from f_garbage where ip = ?";
        Long fid = null;
        PreparedStatement psql;
        try {
            conn.setAutoCommit(false);
            psql = conn.prepareStatement(sql);
            psql.setString(1, ip);
            ResultSet rs = psql.executeQuery();
            while (rs.next()) {
                fid = rs.getLong(1);
            }
            fid = (fid == null ? 0 : fid);
        } catch (SQLException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                conn.close();
                return fid;
            } catch (Exception e) {
                logger.error(e.getMessage());
                return fid;
            }
        }
    }
}

