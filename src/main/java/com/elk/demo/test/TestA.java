package com.elk.demo.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * </p>
 * 
 * @author wangyunfei 2017-07-07
 */
public class TestA {

    private static final Logger logger = LoggerFactory.getLogger(TestA.class);

    private TestA() {}

    public static void main(String[] args) {
        printError(20);
        printWarn(40);
        printInfo(80);
        printDebug(100);
        logger.debug("End, timestamp=" + System.currentTimeMillis() / 1000);
    }

    private static void printDebug(int n) {
        for (int i = 0; i < n; i++) {
            logger.info("这个是 DEBUG 信息，这里没错");
        }
    }
    
    private static void printInfo(int n) {
        for (int i = 0; i < n; i++) {
            logger.info("这个是 INFO 信息，这里没错");
        }
    }
    
    private static void printWarn(int n) {
        for (int i = 0; i < n; i++) {
            logger.warn("这个是 WARN 信息，这里没错");
        }
    }
    
    private static void printError(int n) {
        IndexOutOfBoundsException e = new IndexOutOfBoundsException("test excepiton");
        for (int i = 0; i < n; i++) {
            logger.error("这个是 ERROR 信息，这里出错了。", e);
        }
    }

}
