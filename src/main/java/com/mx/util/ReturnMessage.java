
package com.mx.util;

import java.io.Serializable;

/**
 * UIF公用返回消息类
 * 
 * @author lindx
 */
public class ReturnMessage implements Serializable {

    public static String      SUCCESS          = "0";

    /**
	 * 
	 */
    private static final long serialVersionUID = 8802911744589920319L;

    private String            code;

    private String            msg;

    private Object            data;

    public ReturnMessage() {

        super();
    }


    public ReturnMessage(String msg) {

        super();
        this.msg = msg;
    }

    public ReturnMessage(String code, String msg) {

        super();
        this.code = code;
        this.msg = msg;
    }

    public ReturnMessage(String code, String msg, Object data) {

        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {

        return code;
    }

    public void setCode(String code) {

        this.code = code;
    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {

        this.msg = msg;
    }

    public Object getData() {

        return data;
    }

    public void setData(Object data) {

        this.data = data;
    }

}
