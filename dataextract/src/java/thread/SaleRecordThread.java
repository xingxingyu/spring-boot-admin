package thread;

import dao.F_garbageDao;
import dao.F_garbageDaoImpl;
import dao.OperatorDao;
import dao.OperatorImpl;
import entity.F_garbage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import util.PluUtil;
import util.Tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.out;

/**
 * Created by dylan on 2017/7/13.
 */
public class SaleRecordThread implements Runnable {
    private static Logger logger = LogManager.getLogger(SaleRecordThread.class.getName());
    private String ip;
    private int port;


    public SaleRecordThread(String ip, int port) {
        this.ip = ip;
        this.port = port;

    }

    @Override
    public void run() {
        F_garbageDao f_garbageDao = new F_garbageDaoImpl();
        OperatorDao operatorDao = new OperatorImpl();
        List<F_garbage> list = new ArrayList<>();
        F_garbage f = new F_garbage();
        //获取单价和科室的映射关系
        Map<Integer, String> deptmap = operatorDao.getDepartmentMap();
        //获取编号和操作员的对应关系
        Map<Integer, String> opermap = operatorDao.getOperatorMap();

        Socket socket = null;
        String line;
        Integer sn = f_garbageDao.getMaxFid(ip).intValue();


        try {
            socket = new Socket(ip, port);
            logger.info("成功连上服务器;" + ip + ",端口:" + port);

            sendTag(socket, Tag.REPSTART + sn + "\t\r\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
            socket.shutdownOutput();
            while ((line = in.readLine()) != null) {
                logger.info("收到数据：" + line);
                resolveToList(line, f, list, socket, deptmap, opermap, ip);
            }
            f_garbageDao.insertGarbage(list);
            logger.info("入库完成：" + list.size() + "条记录");
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


    private void resolveToList(String s, F_garbage f, List<F_garbage> list, Socket socket, Map<Integer, String> deptmap, Map<Integer, String> opermap, String ip) {
        Calendar calendar = Calendar.getInstance();
        String[] data = s.split("\t");
        String head = data[0];
        switch (head) {
            //销售记录REP 上传开始标识
            case "DWL":
                logger.info("销售记录REP 上传开始标识");
                break;
            //销售记录REP 起始标识
            case "REP":
                //上传日期
                calendar.set(Integer.parseInt("20" + data[1]), Integer.parseInt(data[2]) - 1,
                        Integer.parseInt(data[3]), Integer.parseInt(data[4]),
                        Integer.parseInt(data[5]), Integer.parseInt(data[6]));


                f.setUp_Date(new Timestamp(calendar.getTime().getTime()));

                //营业员
                f.setOperator(opermap.get(Integer.parseInt(data[9].trim())));
                //FID  -- > ID
                f.setSn(Long.parseLong(data[7].trim()));
                break;
            //销售记录RES 记录起始标识
            case "RES":
                //判断是否是临时称重,目前暂时不知标识位置
                if ("临时称重".equals(data[13]) || "临时计数".equals(data[13])) {
                    break;
                }
                //plu编号--垃圾编号 data[3]
                f.setCategoryId(data[3]);
                //数重量 m,n  data[7]
                f.setNetWeight(Integer.parseInt(data[7].split(",")[0]) * Math.pow(10, Integer.parseInt(data[7].split(",")[1]) * -1));
                //部门 + 垃圾类型  data[]
                int price = Double.valueOf(Integer.parseInt(data[11].split(",")[0].trim()) * Math.pow(10, Integer.parseInt(data[11].split(",")[1].trim()) * -1)).intValue();
                f.setDepartment(deptmap.get(price));
                f.setCategoryName(data[13]);
                break;
            //销售记录REP 结束标识
            case "REE":
                f.setIp(ip);
                list.add(new F_garbage(f.getCategoryId(), f.getCategoryName(),
                        f.getNetWeight(), f.getUp_Date(), f.getSn(), f.getIp(), f.getDepartment(), f.getOperator()));
                break;
            //销售记录REP 上传结束标识
            case "END":
                logger.info("全部解析结束！");
                try {
                    //sendTag(socket, Tag.END);
                    //如果不停止输入流，readline将会在流尾部阻塞
                    socket.shutdownInput();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
                break;
            default:
                break;
        }
    }

    public void sendTag(Socket socket, String tag) {
        try {
            OutputStream out = socket.getOutputStream();
            out.write(tag.getBytes());
            out.flush();
            logger.info("已经发送数据到服务端");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


}
