package dao;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import util.MDBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by dylan on 2017/7/8.
 */
public class DeviceDaoImpl implements DeviceDao {
    private static Logger log = LogManager.getLogger(DeviceDaoImpl.class.getName());
    private static PreparedStatement prst;
    private Map<String, Integer> serverMap = new HashMap<String, Integer>();
    private ResultSet rs;
    private Connection conn;

    /**
     * 查询ip和端口
     * @return
     */
    @Override
    public Map<String, Integer> getServerList() {
        String sql = "select distinct ip,port from device";
        try {
            conn = MDBManager.getInstance().getConn();
            prst = conn.prepareStatement(sql);
            rs = prst.executeQuery();
            while (rs.next()) {
                serverMap.put(rs.getString("IP"), rs.getInt("port"));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            try {
                prst.close();
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }

        }
        return serverMap;
    }

    /**
     * 设置设备状态
     * @param stat
     * @param id
     */
    @Override
    public void updateStatById(String stat, Long id) {
        String sql = "update device set stat = ? where id=?";
        try {
            conn = MDBManager.getInstance().getConn();
            prst = conn.prepareStatement(sql);
            prst.setString(1, stat);
            prst.setLong(2, id);
            prst.executeUpdate();

        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            try {
                prst.close();
                conn.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    public Map<Long, String> getIpList() {
        Map<Long, String> map = new HashMap<>();
        String sql = "select id,ip from device";
        try {
            conn = MDBManager.getInstance().getConn();
            prst = conn.prepareStatement(sql);
            ResultSet rs = prst.executeQuery();

            while (rs.next()) {
                map.put(rs.getLong(1), rs.getString(2));
            }

        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            try {
                prst.close();
                conn.close();
                return map;
            } catch (SQLException e) {
                log.error(e.getMessage());
                return null;
            }

        }
    }
}
