
package com.leduo.mall.common;

public class LeDuoMallException extends RuntimeException {

    public LeDuoMallException() {
    }

    public LeDuoMallException(String message) {
        super(message);
    }

    /**
     * 丢出一个异常
     *
     * @param message
     */
    public static void fail(String message) {
        throw new LeDuoMallException(message);
    }

}
