package util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import thread.SaleRecordThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dylan on 2017/7/29.
 */
public class PluUtil {
    private static Logger logger = LogManager.getLogger(PluUtil.class.getName());
    private String ip;
    private int port;

    public PluUtil(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Map<String, String> getDepartmentByPlu() {
        Socket socket = null;
        Map<String, String> map = new HashMap<>();
        String line;
        try {
            socket = new Socket(ip, port);
            sendTag(socket, Tag.PLUSTART);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
            socket.shutdownOutput();
            while ((line = in.readLine()) != null) {
                logger.info((line));
                resolveToMap(line, map, socket);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }


    public void sendTag(Socket socket, String tag) {
        if(tag.equals(Tag.END) && socket.isOutputShutdown()){
            return;
        }
        try {
            OutputStream out = socket.getOutputStream();
            out.write(tag.getBytes());
            out.flush();
            logger.info("已经发送数据到服务端");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resolveToMap(String s, Map<String, String> map, Socket socket) {
        String[] data = s.split("\t");
        String head = data[0];
        switch (head) {
            case "PLU":
                //根据单价拿到部门名称  单价data[5]   部门名称data[16]
                //REP的原始单价对应于PLU的单价
                logger.info("单价：" + data[5] + ",科室：" + data[16]);
                map.put(data[5].split(",")[0], data[16]);
                logger.info("商品信息PLU 起始标识");
                break;
            case "END":
                logger.info("全部解析结束！");
                try {
                    sendTag(socket, Tag.END);
                    //如果不停止输入流，readline将会在流尾部阻塞
                    socket.shutdownInput();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
