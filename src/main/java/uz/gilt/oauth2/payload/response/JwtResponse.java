package uz.gilt.oauth2.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private final String type = "Bearer";
    private long id;
    private String email;
    private List<String> roles;
}
