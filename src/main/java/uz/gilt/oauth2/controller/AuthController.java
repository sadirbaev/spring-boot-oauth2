package uz.gilt.oauth2.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.gilt.oauth2.payload.response.JwtResponse;
import uz.gilt.oauth2.security.oauth2.EProvider;
import uz.gilt.oauth2.service.AuthService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/oauth2/{provider}/")
    public ResponseEntity<JwtResponse> authenticateUser(
            @Parameter(
                    name =  "provider",
                    schema = @Schema(
                            implementation = EProvider.class,
                            enumAsRef = true,
                            defaultValue = "KAKAO",
                            allowableValues = {"KAKAO"}),
                    required = true)
            @PathVariable("provider") EProvider provider, @RequestParam("code") String code) throws IOException, ExecutionException, InterruptedException {
        return authService.authenticate(provider, code);
    }

}
