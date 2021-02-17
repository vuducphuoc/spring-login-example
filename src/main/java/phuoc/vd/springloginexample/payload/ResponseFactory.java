package phuoc.vd.springloginexample.payload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import phuoc.vd.springloginexample.exception.StatusResponse;

public class ResponseFactory {

    public static ResponseEntity success() {
        GenericResponse<Object> responseObject = new GenericResponse<>();
        responseObject.setApiStatus(StatusResponse.SUCCESS);
        return ResponseEntity.ok(responseObject);
    }

    public static ResponseEntity error(HttpStatus httpStatus, StatusResponse code, String message, String description) {
        GenericResponse<Object> responseObject = new GenericResponse<>()
                .setApiStatus(code)
                .setMessage(message)
                .setDescription(description);
        return new ResponseEntity(responseObject, httpStatus);
    }

    public static ResponseEntity generalError() {
        GenericResponse<Object> responseObject = new GenericResponse<>();
        responseObject.setApiStatus(StatusResponse.UNKNOWN);
        return ResponseEntity.ok(responseObject);
    }
}
