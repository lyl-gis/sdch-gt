package edu.zju.gis.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author yanlong_lee@qq.com
 * @version 1.0 2018/11/13
 */
public class CommonSetting {
    private Properties properties;

    private CommonSetting(Properties properties) {
        this.properties = properties;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public static CommonSetting getInstance() {
        return InstanceHolder.setting;
    }

    private static class InstanceHolder {
        static CommonSetting setting;

        static {
            Properties props = new Properties();
            setting = new CommonSetting(props);
            try {
                InputStream is = ClassLoader.getSystemResourceAsStream("config.properties");
                props.load(is);
            } catch (IOException e) {
                throw new RuntimeException("读取配置文件config.properties异常", e);
            }
        }
    }
}
