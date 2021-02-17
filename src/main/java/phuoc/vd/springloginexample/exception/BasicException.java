package phuoc.vd.springloginexample.exception;

import java.util.List;

public class BasicException extends RuntimeException {
    private StatusResponse code; // contains http_code and error_code
    private String[] messageParams = new String[0];  // contains value for placeholders of message
    private String logMessage; // custom log message

    public BasicException(StatusResponse code) {
        super(code.getMessage());
        this.code = code;
    }

    public BasicException(StatusResponse code, List<String> messageParams) {
        super(code.getMessage());
        this.code = code;
        this.messageParams = messageParams.toArray(new String[0]);
    }

    public BasicException(StatusResponse code, String logMessage) {
        super(code.getMessage());
        this.code = code;
        this.logMessage = logMessage;
    }

    public StatusResponse getCode() {
        return code;
    }

    public void setCode(StatusResponse code) {
        this.code = code;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public String[] getMessageParams() {
        return messageParams;
    }
}
