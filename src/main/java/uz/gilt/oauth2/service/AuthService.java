package uz.gilt.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uz.gilt.oauth2.entity.Role;
import uz.gilt.oauth2.entity.User;
import uz.gilt.oauth2.payload.response.JwtResponse;
import uz.gilt.oauth2.security.jwt.JwtUtils;
import uz.gilt.oauth2.security.oauth2.EProvider;
import uz.gilt.oauth2.security.oauth2.UserInfoAccessService;
import uz.gilt.oauth2.security.oauth2.user.OAuth2UserInfo;
import uz.gilt.oauth2.security.service.impl.UserDetailsImpl;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final RoleService roleService;

    public ResponseEntity<JwtResponse> authenticate(EProvider provider, String code) throws IOException, ExecutionException, InterruptedException {
        OAuth2UserInfo userInfo = UserInfoAccessService.getUserInfo(provider, code);
        if (userInfo.getEmail() == null || userInfo.getEmail().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Email not found from OAuth2 provider");
        }
        Optional<User> optionalUser = userService.findUserByEmail(userInfo.getEmail());
        User user = optionalUser.isPresent()
                ? updateExistingUser(optionalUser.get(), userInfo)
                : registerNewUser(userInfo);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        return getJwtResponse(userDetails);
    }

    public ResponseEntity<JwtResponse> getJwtResponse(UserDetailsImpl userDetails){
        Date expiration = new Date((new Date()).getTime() + JwtUtils.jwtExpirationMs);
        String jwt = jwtUtils.generateJwtToken(userDetails, expiration);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getEmail(),
                        roles
                )
        );
    }


    private User registerNewUser(OAuth2UserInfo oAuth2UserInfo) {
        Role userRole = roleService.getUserRole();
        User user = new User();
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setRoles(new HashSet<>(List.of(userRole)));
        return userService.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setEmail(oAuth2UserInfo.getEmail());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userService.save(existingUser);
    }

}
