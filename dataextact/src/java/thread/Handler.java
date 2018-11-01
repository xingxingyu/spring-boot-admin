package thread;

import dao.F_garbageDao;
import dao.F_garbageDaoImpl;
import dao.OperatorDao;
import dao.OperatorImpl;
import entity.F_garbage;
import org.apache.log4j.Logger;


import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


/**
 * Created by dylan on 2017/5/7.
 */
public class Handler implements Runnable {
    static Logger logger = Logger.getLogger(Handler.class.getName());

    private Socket socket;


    public Handler(Socket socket) {
        this.socket = socket;
    }


    public void run() {
        F_garbageDao f_garbageDao = new F_garbageDaoImpl();
        OperatorDao operatorDao = new OperatorImpl();
        F_garbage f = new F_garbage();
        //获取单价和科室的映射关系
        Map<Integer, String> deptmap = operatorDao.getDepartmentMap();
        //获取编号和操作员的对应关系
        Map<Integer, String> opermap = operatorDao.getOperatorMap();
        String line;
        String ip = socket.getInetAddress().getHostAddress();


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
            while ((line = in.readLine()) != null) {
                logger.info("收到数据：" + line);
                resolveToList(line, f, deptmap, opermap, ip, f_garbageDao);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
        }
    }


    private void resolveToList(String s, F_garbage f, Map<Integer, String> deptmap, Map<Integer, String> opermap, String ip, F_garbageDao f_garbageDao) {
        Calendar calendar = Calendar.getInstance();
        String[] data = s.split("\t");
        String head = data[0];
        if (head.indexOf("DWL") != -1) {
            logger.info("销售记录REP 上传开始标识");
        } else if (head.indexOf("REP") != -1) {
            //上传日期
            calendar.set(Integer.parseInt("20" + data[2]), Integer.parseInt(data[3]) - 1,
                    Integer.parseInt(data[4]), Integer.parseInt(data[5]),
                    Integer.parseInt(data[6]), Integer.parseInt(data[7]));
            f.setUp_Date(new Timestamp(calendar.getTime().getTime()));
            //营业员
            f.setOperator(opermap.get(Integer.parseInt(data[10].trim())));
            //FID  -- > ID
            f.setSn(Long.parseLong(data[8].trim()));
        } else if (head.indexOf("RES") != -1) {
            //判断是否是临时称重,目前暂时不知标识位置
            if ("临时称重".equals(data[14]) || "临时计数".equals(data[13])) {
                return;
            }
            //plu编号--垃圾编号
            f.setCategoryId(data[4]);
            //数重量 m,n
            f.setNetWeight(this.convertDouble(Integer.parseInt(data[8].split(",")[0]) * Math.pow(10, Integer.parseInt(data[8].split(",")[1]) * -1)));
            //部门 + 垃圾类型  data[]
            int price = Double.valueOf(Integer.parseInt(data[12].split(",")[0].trim()) * Math.pow(10, Integer.parseInt(data[12].split(",")[1].trim()) * -1)).intValue();
            f.setDepartment(deptmap.get(price));
            f.setCategoryName(data[14]);
        } else if (head.indexOf("REE") != -1) {
            f.setIp(ip);
            f_garbageDao.insertGarbage(f);
            logger.info("入库完成：1条记录");
        }

    }

    public   double convertDouble(double d) {
        return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
