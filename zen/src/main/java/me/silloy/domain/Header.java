package me.silloy.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Header  {
    private int error;
    private String message;
    private String orderId;
    private String sessionId;
    private String sn;
    private String token;
    private Map<String, Object> headMap;

    public static Header SUCCESS(){
        Header header=new Header();
        header.setError(0);
        return header;
    }

    public Header sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

}
