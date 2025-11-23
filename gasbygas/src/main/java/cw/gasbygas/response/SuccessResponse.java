package cw.gasbygas.response;

import lombok.Getter;

import java.util.Map;

@Getter
public class SuccessResponse extends Response
{
    private int status = 1;

    public SuccessResponse(Integer status, Object data, String message)
    {
        super(data, message);
        this.status = status;
    }

    public SuccessResponse(Object data, String message)
    {
        super(data, message);
    }

    public SuccessResponse(String message)
    {
        super(null, message);
    }

    public SuccessResponse(Map<String, String> message)
    {
        super(null, message);
    }
}
