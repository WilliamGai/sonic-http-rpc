package com.sonic.http.rpc.exception;

public class RpcException extends Throwable
{
	private static final long serialVersionUID = 5574987226847736276L;

	private String code;

    private Object data;

    public RpcException(String message, Throwable cause, String code, Object data) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public RpcException(String code, Object data) {
        super();
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}