package phuoc.vd.springloginexample.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum StatusResponse {

    SUCCESS(0,"SUCCESS","Thành công"),
    FAILED(1,"FAILED","Đã có lỗi xảy ra"),
    FORBIDDEN(2,"FORBIDDEN","Từ chối truy cập"),
    UNAUTHORIZED(2,"UNAUTHORIZED","Từ chối truy cập"),
    EXISTED_RECORD(3,"EXISTED_RECORD","Thông tin đã tồn tại"),
    NOT_FOUND(4,"NOT_FOUND","Không tìm thấy kết quả"),
    CARD_NOT_FOUND(4,"CARD_NOT_FOUND","Không tìm thấy thẻ"),
    ID_NOT_FOUND(4,"ID_NOT_FOUND","Không tìm thấy định danh"),
    MALFORMED(5,"MALFORMED","Dữ liệu không hợp lệ"),
    INVALID_INPUT(5,"INVALID_INPUT","Dữ liệu không hợp lệ"),
    NOT_FOUND_TEST_RESULT_FILE(5,"NOT_FOUND_TEST_RESULT_FILE","Không tìm thấy tệp kiểm tra"),
    OLD_PASSWORD_NOT_MATCH(6,"OLD_PASSWORD_NOT_MATCH","Mật khẩu cũ không đúng"),
    INVALIDATED_EMAIL(7,"INVALIDATED_EMAIL","Email không đúng hoặc không tồn tại"),
    CAPTCHA_VERIFICATION_PARAM_IS_MISSING(8,"CAPTCHA_VERIFICATION_PARAM_IS_MISSING","Thiếu tham số Captcha"),
    CAPTCHA_REFERENCE_ID_NOT_FOUND(9,"CAPTCHA_REFERENCE_ID_NOT_FOUND","Captcha không tồn tại"),
    CAPTCHA_REFERENCE_ID_NOT_ACTIVE(10,"CAPTCHA_REFERENCE_ID_NOT_ACTIVE","Captcha không active"),
    CAPTCHA_REFERENCE_ID_EXPIRED(11,"CAPTCHA_REFERENCE_ID_EXPIRED","Captcha đã hết hạn"),
    EXISTED_EMAIL_ACCOUNT_WHEN_ACTIVE_CARD(12,"EXISTED_EMAIL_ACCOUNT_WHEN_ACTIVE_CARD","Email này đã tồn tại. Vui lòng đăng nhập và nhập mã thẻ theo khóa học tương ứng!"),
    AUTHENTICATE(-2,"WWW-Authenticate","WWW-Authenticate"),
    UNKNOWN(-1,"UNKNOWN","Không xác định");

    private final Integer code;
    private final String message;
    private final String description;

    private StatusResponse(Integer code) {
        this(code, null,null);
    }

    private StatusResponse(Integer code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    @Override
    public String toString() {
        return this.message+"("+this.code+")";
    }

    @JsonCreator
    public static StatusResponse fromValue(String value) throws JsonMappingException {
        String[] ps = value.split("\\(");
        for (StatusResponse format : StatusResponse.values()) {

            if(format.getMessage().equalsIgnoreCase(ps[0]))
                return format;
        }
        return UNKNOWN;
    }

    private static final Map<Integer, String> BASE_MESSAGES;
    static {
        Map<Integer, String> msgs = new HashMap<>();
        for (StatusResponse msg : StatusResponse.values()) {
            if (msgs.containsKey(msg.code)) continue;
            msgs.put(msg.code,msg.message);
        }
        BASE_MESSAGES  =  Collections.unmodifiableMap(msgs);
    }

    public static String messageFromCode(Integer code){
        if (BASE_MESSAGES.containsKey(code)) return BASE_MESSAGES.get(code);
        return null;
    }

    public static Integer codeFromMessage(String code){
        for (Map.Entry<Integer,String> entry : BASE_MESSAGES.entrySet()){
            if (entry.getValue().equalsIgnoreCase(code)) return entry.getKey();
        }
        return null;
    }
}
