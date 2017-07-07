package ch.qos.logback.core.rolling;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.joda.time.format.DateTimeFormat;

import com.elk.demo.utils.ESHttpClient;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * <p>
 * Demo FileAppender
 * </p>
 * 
 * @author wangyunfei 2017-07-07
 */
public class DemoRollingFileAppender<E> extends RollingFileAppender<E> {

    private static final String DEFAULT_APP_NAME = "DEFAULT-APP";
    
    private static final String SLASH = "/";

    private String appName;
    private String esHost;

    private static Executor executor = Executors.newFixedThreadPool(5);

    // Elasticsearch http client
    private volatile ESHttpClient client;

    private ESHttpClient getClient() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = new ESHttpClient(esHost);
                }
            }
        }
        return client;
    }

    @Override
    protected void subAppend(E e) {
        super.subAppend(e);

        if (e instanceof ILoggingEvent) {
            executor.execute(() -> {
                DemoLoggerEvent event = new DemoLoggerEvent(getAppName(), (ILoggingEvent) e);
                getClient().post(getURI(event), event.toJSONString());
            });
        }
    }

    /**
     * 获取 Index 
     * */
    private static String getIndex(long timestamp) {
        return DateTimeFormat.forPattern("yyyy-MM-dd").print(timestamp);
    }

    private String getURI(DemoLoggerEvent event) {
        return SLASH + appName + "-" + getIndex(event.getTimestamp()) + SLASH + event.getLevel();
    }

    public void setEsHost(String esHost) {
        this.esHost = esHost;
    }

    public String getAppName() {
        if (appName == null) {
            return DEFAULT_APP_NAME;
        }
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

}
