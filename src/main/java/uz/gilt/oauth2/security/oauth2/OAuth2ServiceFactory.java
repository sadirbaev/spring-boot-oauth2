package uz.gilt.oauth2.security.oauth2;

import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OAuth2ServiceFactory {

    public static OAuth20Service getOAuth2Service(EProvider registrationId){
        if (registrationId.equals(EProvider.KAKAO)){
            return OAuth2Service.kakao;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Sorry! Login with  %s is not supported yet.", registrationId));
        }
    }
}
