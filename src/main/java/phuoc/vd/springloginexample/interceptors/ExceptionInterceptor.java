package phuoc.vd.springloginexample.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import phuoc.vd.springloginexample.exception.BasicException;
import phuoc.vd.springloginexample.exception.ErrorMessage;
import phuoc.vd.springloginexample.exception.StatusResponse;
import phuoc.vd.springloginexample.payload.GenericResponse;
import phuoc.vd.springloginexample.payload.ResponseFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
public class ExceptionInterceptor {

    private static Logger logger = LogManager.getLogger();
    private static Map<String, ErrorMessage> errorMessages = new ConcurrentHashMap<>();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

//    /**
//     * Refresh cache map after calling refresh endpoint
//     *
//     * @param event
//     */
//    @EventListener(RefreshScopeRefreshedEvent.class)
//    public void onRefresh(RefreshScopeRefreshedEvent event) {
//        logger.info("Context refresh from {}", event.getName());
//        errorMessages.clear();
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(HttpServletRequest req, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), org.springframework.web.bind.annotation.ResponseStatus.class) != null) {
            throw e;
        }
        String language = getErrorMessageLanguage(req);
        try {
            if (e instanceof BasicException) {
                BasicException baseException = (BasicException) e;
                String errorCode = baseException.getCode().getMessage();
                ErrorMessage errorMessageDetail = getErrorMessageDetail(errorCode);
                String message = getErrorMessage(language, baseException, errorMessageDetail);
                String description = errorMessageDetail == null ? baseException.getCode().name().toLowerCase() : errorMessageDetail.getDescription();
                if (!StringUtils.isEmpty(baseException.getLogMessage())) {
                    logger.error(() -> String.format("Exception occurs: [%s] %s", description, baseException.getLogMessage()));
                }
                return ResponseFactory.error(HttpStatus.BAD_REQUEST, baseException.getCode(), message, description);
            } else {
                logger.error(e.getMessage(), e);
                return generalError(language);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            try {
                return generalError(language);
            } catch (Exception unexpectedError) {
                logger.error(unexpectedError.getMessage(), unexpectedError);
                // Error should not happen here
                return ResponseFactory.generalError();
            }
        }
    }

    /**
     * Return error message in selected language
     *
     * @param language
     * @param baseException
     * @param errorMessageDetail
     * @return
     * @throws IOException
     */
    private String getErrorMessage(String language, BasicException baseException, ErrorMessage errorMessageDetail) throws IOException {
        if (errorMessageDetail == null) {
            return baseException.getCode().name().toLowerCase();
        } else {
            if (StringUtils.isEmpty(errorMessageDetail.getMessage(language))) {
                // If message is empty in db we'll try to return "general_error" message in selected language instead
                ErrorMessage generalError = getErrorMessageDetail(StatusResponse.UNKNOWN.getMessage());
                if (generalError == null) {
                    return "";
                } else {
                    return generalError.getMessage(language);
                }
            } else {
                if (baseException.getMessageParams().length == 0) {
                    return errorMessageDetail.getMessage(language);
                } else {
                    return String.format(errorMessageDetail.getMessage(language), (Object[]) baseException.getMessageParams());
                }
            }
        }
    }

    private String getErrorMessageLanguage(HttpServletRequest req) {
        String acceptLanguageHeader = req.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (!StringUtils.isEmpty(acceptLanguageHeader) && !"*".equals(acceptLanguageHeader)) {
            return acceptLanguageHeader;
        } else {
            return ErrorMessage.DEFAULT_LANG;
        }
    }

    private ErrorMessage getErrorMessageDetail(String errorCode) throws IOException {
        if (!errorMessages.containsKey(errorCode)) {
            String errorMessageStr = env.getProperty("error-message." + errorCode);
            if (StringUtils.isEmpty(errorMessageStr)) {
                return null;
            } else {
                ErrorMessage errorMessage = objectMapper.readValue(errorMessageStr, ErrorMessage.class);
                errorMessage.initLanguageMap();
                errorMessages.put(errorCode, errorMessage);
            }
        }
        return errorMessages.get(errorCode);
    }

    private ResponseEntity generalError(String language) throws IOException {
        ErrorMessage errorMessage = getErrorMessageDetail(StatusResponse.FAILED.getMessage());
        GenericResponse<Object> responseObject = new GenericResponse<>();
        responseObject.setApiStatus(StatusResponse.FAILED);
        if (errorMessage == null) {
            responseObject.setMessage(StatusResponse.FAILED.getMessage());
        } else {
            responseObject.setMessage(errorMessage.getMessage(language));
        }

        return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
