package phuoc.vd.springloginexample.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorMessage {

    public static final String DEFAULT_LANG = "default";

    private String description;
    private List<LanguageMessage> language;
    private final Map<String, String> messages = new HashMap<>();

    public void initLanguageMap() {
        for (LanguageMessage messageInLanguage : language) {
            messages.put(messageInLanguage.getCode(), messageInLanguage.getMessage());
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage(String language) {
        return messages.get(language);
    }

    public void setLanguage(List<LanguageMessage> language) {
        this.language = language;
    }
}

