package phuoc.vd.springloginexample.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import phuoc.vd.springloginexample.entity.Role;
import phuoc.vd.springloginexample.entity.RoleUser;
import phuoc.vd.springloginexample.entity.User;
import phuoc.vd.springloginexample.exception.ResourceNotFoundException;
import phuoc.vd.springloginexample.reporsitory.RoleRepository;
import phuoc.vd.springloginexample.reporsitory.RoleUserRepository;
import phuoc.vd.springloginexample.reporsitory.UserRepository;

import java.util.Collections;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
                );
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        RoleUser roleUser = roleUserRepository.findOneByUserId(userPrincipal.getId());
        if (roleUser != null) {
            Role role  = roleRepository.findById(roleUser.getRoleId()).orElse(null);
            if(role != null) {
                List<GrantedAuthority> authorities = Collections.
                        singletonList(new SimpleGrantedAuthority(role.getShortName()));

                userPrincipal.setRoleId(role.getId()).setRoleName(role.getShortName());
                userPrincipal.setAuthorities(authorities);
            }
        }

        return userPrincipal;
    }

    @Transactional
    public UserPrincipal loadUserById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        return UserPrincipal.create(user);
    }
}