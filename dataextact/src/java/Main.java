
import org.apache.log4j.Logger;
import service.MultiThreadServer;
import service.QuartzManager;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Created by dylan on 2017/7/9.
 */
public class Main {
    private static Logger logger = Logger.getLogger(QuartzManager.class.getName());

    public static void main(String[] args) {
/*        logger.info("开始调度");
        Properties prop = new Properties();
        try {
            prop.load(new BufferedInputStream(Main.class.getClassLoader().getResourceAsStream("time.properties")));
            logger.info("crond表达式为" + prop.get("time").toString());
            QuartzManager.addJob("ExtractJob", prop.get("time").toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }*/

        try {
            new MultiThreadServer().service();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }
}
