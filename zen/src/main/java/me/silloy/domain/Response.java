package me.silloy.domain;

import java.io.Serializable;

public class Response<T> implements Serializable{
    private static final long serialVersionUID = 3151836054335972087L;


    private Header head;
    private T body;


    public static Response getInstance() {
        Response response = new Response();
        response.setHead(Header.SUCCESS());
        return response;
    }

    public static <T> Response getInstance(T body) {
        Response response = new Response();
        response.setHead(Header.SUCCESS());
        response.setBody(body);
        return response;
    }



    public Boolean hasError() {
        if (head.getError() == 0) return false;
        return true;
    }

    public Header getHead() {
        return head;
    }

    public Response setHead(Header head) {
        this.head = head;
        return this;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Response setErrorCode(int errorCode) {
        this.head.setError(errorCode);
        return this;
    }

    public Response setErrorMessage(String errorMessage) {
        this.head.setMessage(errorMessage);
        return this;
    }

    @Override
    public String toString() {
        return "Response{" +
                "head=" + head +
                ", body=" + body +
                '}';
    }
}
