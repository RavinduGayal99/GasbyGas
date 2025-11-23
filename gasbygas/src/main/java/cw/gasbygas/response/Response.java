package cw.gasbygas.response;

import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Response
{
    private final Date timestamp;
    private final Object data;
    private Map<String, String> message = new HashMap<>();

    public Response(Object data, String message)
    {
        this.data = data;
        this.timestamp = new Date();
        this.message.put("0", message);
    }

    public Response(Object data, Map<String, String> message)
    {
        this.data = data;
        this.timestamp = new Date();
        this.message = message;
    }
}
