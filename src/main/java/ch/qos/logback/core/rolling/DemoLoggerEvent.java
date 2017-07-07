package ch.qos.logback.core.rolling;

import java.io.Serializable;
import java.net.InetAddress;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

/**
 * <p>
 * Demo logger event.
 * </p>
 * 
 * @author wangyunfei 2017-07-07
 */
public class DemoLoggerEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    // 默认本机IP地址
    private static final String DEFAULT_HOST_ADDRESS = "127.0.0.1";
    // 获取本机地址
    private static volatile String localhost;

    private String appName;
    private String host;
    private String level;
    private String message;
    private long timestamp;
    private String threadName;
    private String className;
    private String throwable;

    public DemoLoggerEvent() {
        super();
    }

    public DemoLoggerEvent(String appName, ILoggingEvent event) {
        super();
        this.appName = appName;
        this.host = getLocalHostAddress();
        this.level = event.getLevel().levelStr.toLowerCase();
        this.className = event.getLoggerName();
        this.threadName = event.getThreadName();
        this.timestamp = event.getTimeStamp();
        this.message = event.getMessage();
        this.throwable = getErrorMsg(event);
    }

    /**
     * 获取异常信息
     * */
    private static String getErrorMsg(ILoggingEvent event) {
        IThrowableProxy proxy = event.getThrowableProxy();
        if (proxy == null) {
            return "";
        }

        // 输出异常栈
        StringBuilder err = new StringBuilder(64);
        err.append(proxy.getClassName()).append(":");
        err.append(proxy.getMessage()).append(",");

        StackTraceElementProxy[] arr = proxy.getStackTraceElementProxyArray();
        if (arr != null && arr.length > 0) {
            for (StackTraceElementProxy stackTraceElementProxy : arr) {
                err.append(stackTraceElementProxy.getSTEAsString()).append(",");
            }
        }
        return err.toString();
    }

    /**
     * 获取本机IP
     */
    public static String getLocalHostAddress() {
        if (localhost == null) {
            synchronized (DemoLoggerEvent.class) {
                if (localhost == null) {
                    try {
                        localhost = InetAddress.getLocalHost().getHostAddress();
                    } catch (Exception e) {
                        // 拿不到IP 则赋值为默认地址
                        localhost = DEFAULT_HOST_ADDRESS;
                    }
                }
            }
        }
        return localhost;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DotionsLoggerEvent [appName=");
        builder.append(appName);
        builder.append(", hostAddress=");
        builder.append(host);
        builder.append(", level=");
        builder.append(level);
        builder.append(", message=");
        builder.append(message);
        builder.append(", timestamp=");
        builder.append(timestamp);
        builder.append(", threadName=");
        builder.append(threadName);
        builder.append(", className=");
        builder.append(className);
        builder.append(", throwable=");
        builder.append(throwable);
        builder.append("]");
        return builder.toString();
    }

    public String toJSONString() {
        StringBuilder json = new StringBuilder();
        
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        
        json.append("{")
        .append("\"appName\":").append("\"").append(appName).append("\"").append(",")
        .append("\"host\":").append("\"").append(host).append("\"").append(",")
        .append("\"level\":").append("\"").append(level).append("\"").append(",")
        .append("\"message\":").append("\"").append(message).append("\"").append(",")
        .append("\"threadName\":").append("\"").append(threadName).append("\"").append(",")
        .append("\"className\":").append("\"").append(className).append("\"").append(",")
        .append("\"throwable\":").append("\"").append(throwable).append("\"").append(",")
        .append("\"@timestamp\":").append("\"")
        .append(fmt.print(timestamp)).append("\"")
        .append("}");
        return json.toString();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getThrowable() {
        return throwable;
    }

    public void setThrowable(String throwable) {
        this.throwable = throwable;
    }

}
