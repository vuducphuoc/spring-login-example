package phuoc.vd.springloginexample.entity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "role_user", uniqueConstraints = {
})
@IdClass(RoleUser.PrimaryKeys.class)
public class RoleUser implements Serializable {
    @Data
    public static class PrimaryKeys implements Serializable {
        private Integer roleId;
        private Integer userId;
    }

    @Id
    @Column(name = "role_id", nullable = false)
    private Integer roleId;
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "created_timestamp", nullable = true)
    private Timestamp createdTimestamp;
}
