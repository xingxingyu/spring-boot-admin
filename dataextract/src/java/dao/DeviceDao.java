package dao;

import java.util.Map;

/**
 * Created by dylan on 2017/7/8.
 */
public interface DeviceDao {
    /**
     * 获取所有电子秤的ip和端口
     *
     * @return
     */
     Map<String, Integer> getServerList();

    /**
     * 更新电子秤的使用状态
     */
     void updateStatById(String stat, Long id);

    /**
     * 获取所有的ip 和序号
     */

     Map<Long, String> getIpList();
}
