package util;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.util.Properties;


/**
 * Created by dylan on 2017/9/23.
 */
public class ConstantUtils {
    static Logger logger = Logger.getLogger(ConstantUtils.class.getName());

    public static String getValue(String key) {
        Properties prop = new Properties();
        try {
            prop.load(new BufferedInputStream(ConstantUtils.class.getClassLoader().getResourceAsStream("c3p0.properties")));
            return (prop.getProperty(key));
        } catch (Exception e) {
            logger.error(key);
            return null;
        }
    }
}
