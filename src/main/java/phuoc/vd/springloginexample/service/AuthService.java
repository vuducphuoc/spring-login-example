package phuoc.vd.springloginexample.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import phuoc.vd.springloginexample.controller.request.SignUpRequest;
import phuoc.vd.springloginexample.controller.response.JwtResponse;
import phuoc.vd.springloginexample.entity.AuthProvider;
import phuoc.vd.springloginexample.entity.User;
import phuoc.vd.springloginexample.exception.BadRequestException;
import phuoc.vd.springloginexample.reporsitory.UserRepository;
import phuoc.vd.springloginexample.security.TokenProvider;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public JwtResponse login(String authCredentials) {
        return tokenProvider.makeAccessToken(authCredentials);
    }

    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);
        return result;
    }
}
