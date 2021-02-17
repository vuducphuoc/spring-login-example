package phuoc.vd.springloginexample.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Accessors(chain = true)
@MappedSuperclass
public class BasicEntity implements Serializable {

    private static ObjectMapper jsonMapper = new ObjectMapper();

    static {
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    private static final Logger logger = LogManager.getLogger();
    @Id
    @GeneratedValue
    protected Integer id;
    protected Timestamp createdTimestamp;
    protected Timestamp lastUpdatedTimestamp;
    protected Integer creationUserId;
    protected Integer lastUpdatedUserId;
    protected Boolean isDeleted;

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        try {
            return jsonMapper.writeValueAsString(this);
        } catch (Exception e) {
            logger.error("Error parsing entity to json: [{}]", e.getMessage());
        }
        return super.toString();
    }
}
