package com.elk.demo.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <p> Elasticsearch HTTP client. </p>
 * @author wangyunfei 2017-07-07
 */
public class ESHttpClient {

    /** ip和端口 **/
    private String hostAndPort;

    private static final String PROTOCOL = "http";
    private static final String COLON = ":";
    private static final String DOUBLE_SLASH = "//";
    private static final String SLASH = "/";
    private static final String STRING_EMPTY = "";

    // charset
    private static final String CHARSET_UTF_8 = "UTF-8";

    // http code
    private static final int HTTP_CODE_SUCCESS = 200;

    private static interface HttpMethod {

        String GET = "GET";
        String PUT = "PUT";
        String POST = "POST";
        String DEL = "DELETE";
    }

    public ESHttpClient(String hostAndPort) {
        super();
        if (hostAndPort == null || hostAndPort.trim().equals(STRING_EMPTY)) {
            throw new IllegalArgumentException("hostAndPort is invalid");
        }
        this.hostAndPort = hostAndPort;
    }

    /**
     * HTTP 请求操作
     * 
     * @param uri 请求的URI信息
     */
    private String options(String surl, String method, boolean doOutput, String json) {
        OutputStreamWriter writer = null;
        try {
            // 获取连接
            URL url = new URL(surl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setReadTimeout(20000);
            httpConn.setConnectTimeout(20000);
            httpConn.setRequestMethod(method);
            httpConn.setDoOutput(doOutput);
            httpConn.setDoInput(true);

            if (doOutput) {
                httpConn.setRequestProperty("Content-Length", String.valueOf(json.length()));
                httpConn.setRequestProperty("Content-type", "application/json; charset=UTF-8");

                // 发送请求参数
                writer = writeString(httpConn.getOutputStream(), json, CHARSET_UTF_8);
            }

            if (httpConn.getResponseCode() == HTTP_CODE_SUCCESS) {
                return readString(httpConn.getInputStream(), CHARSET_UTF_8);
            } else {
                return httpConn.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(writer);
        }
        return STRING_EMPTY;
    }

    public String get(String uri) {
        return options(getURL(uri), HttpMethod.GET, false, STRING_EMPTY);
    }

    public String put(String uri, String json) {
        return options(getURL(uri), HttpMethod.PUT, true, json);
    }

    public String post(String uri, String json) {
        return options(getURL(uri), HttpMethod.POST, true, json);
    }

    public String delete(String uri) {
        return options(getURL(uri), HttpMethod.DEL, false, STRING_EMPTY);
    }

    /**
     * 获取 {@code URL}
     * 
     * @throws IOException
     */
    private String getURL(String uri) {
        if (uri == null) {
            uri = SLASH;
        }

        // http://127.0.0.1:9200/
        return new StringBuilder().append(PROTOCOL).append(COLON).append(DOUBLE_SLASH)
                .append(hostAndPort).append(uri).toString();
    }

    /**
     * 将信息写入 {@code OutputStream} 流中
     * 
     * @param content 待写入的内容
     * @param charsetName 字符集名称
     */
    private OutputStreamWriter writeString(OutputStream stream, String content, String charsetName)
            throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(stream, charsetName);
        outputStreamWriter.write(content);
        outputStreamWriter.flush();
        return outputStreamWriter;
    }

    /**
     * 从 {@code InputStream} 中读取字符串信息
     * 
     * @param charsetName 字符集名称
     */
    public static String readString(InputStream stream, String charsetName) throws IOException {
        if (stream == null || charsetName == null) {
            return STRING_EMPTY;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charsetName));
        StringBuilder info = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            info.append(line);
        }
        return info.toString();
    }

    /**
     * 关闭流
     */
    private static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            // ignore
        }
    }

    public void setHostAndPort(String hostAndPort) {
        this.hostAndPort = hostAndPort;
    }

}
