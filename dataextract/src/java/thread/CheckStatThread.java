package thread;


import dao.DeviceDao;
import dao.DeviceDaoImpl;
import org.apache.log4j.Logger;


import java.net.InetAddress;

import java.util.Map;


public class CheckStatThread implements Runnable {
    private static final Logger logger = Logger.getLogger(CheckStatThread.class.getName());


    @Override
    public void run() {
        logger.info("线程启动----------");
        DeviceDao deviceDao = new DeviceDaoImpl();
        while (true) {
            try {
                logger.info("------------开始检查设备状态-----------");
                Map<Long, String> ipList = deviceDao.getIpList();
                if (ipList != null) {
                    for (Long id : ipList.keySet()) {
                        if (ping(ipList.get(id))) {
                            deviceDao.updateStatById("正在运行", id);
                        } else {
                            deviceDao.updateStatById("停止运行", id);
                        }
                    }
                }
                Thread.sleep(10000);
            } catch (Exception e) {
             logger.error(e.getMessage());
            }
        }

    }

    public boolean ping(String ipAddress) throws Exception {
        int timeOut = 3000;  //超时应该在3秒以上
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);     // 当返回值是true时，说明host是可用的，false则不可。
        return status;
    }

}
