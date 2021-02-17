package phuoc.vd.springloginexample.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import phuoc.vd.springloginexample.controller.request.SignUpRequest;
import phuoc.vd.springloginexample.entity.User;
import phuoc.vd.springloginexample.payload.ApiResponse;
import phuoc.vd.springloginexample.reporsitory.UserRepository;
import phuoc.vd.springloginexample.security.TokenProvider;
import phuoc.vd.springloginexample.service.AuthService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
//    @ApiParam(value = "Basic Base64(user:password)")
    public ResponseEntity<?> authenticateUser(
            @RequestHeader(value = "Authorization") String authCredentials) {
        return ResponseEntity.ok(authService.login(authCredentials));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        User result = authService.registerUser(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }

}
