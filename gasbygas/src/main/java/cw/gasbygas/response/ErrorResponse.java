package cw.gasbygas.response;

import lombok.Getter;

import java.util.Map;

@Getter
public class ErrorResponse extends Response
{
    private int status = 0;

    public ErrorResponse(Integer status, Object data, String message)
    {
        super(data, message);
        this.status = status;
    }

    public ErrorResponse(Object data, String message)
    {
        super(data, message);
    }

    public ErrorResponse(String message)
    {
        super(null, message);
    }

    public ErrorResponse(Map<String, String> message)
    {
        super(null, message);
    }
}
