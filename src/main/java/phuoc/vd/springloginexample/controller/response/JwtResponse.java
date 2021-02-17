package phuoc.vd.springloginexample.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class JwtResponse {
    boolean success = true;
    String access_token;
    String refresh_token;
    Integer expires_in;
    String scope;
    String token_type;
    String message;
}