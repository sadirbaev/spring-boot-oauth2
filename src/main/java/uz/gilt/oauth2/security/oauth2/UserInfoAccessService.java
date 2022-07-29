package uz.gilt.oauth2.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import uz.gilt.oauth2.security.oauth2.user.OAuth2UserInfo;
import uz.gilt.oauth2.security.oauth2.user.OAuth2UserInfoFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UserInfoAccessService {

    public static OAuth2UserInfo getUserInfo(EProvider provider, String code) throws IOException, ExecutionException, InterruptedException {
        OAuth20Service oAuth20Service = OAuth2ServiceFactory.getOAuth2Service(provider);
        OAuth2AccessToken accessToken = oAuth20Service.getAccessToken(code);

        if (provider.equals(EProvider.KAKAO)){
            OAuthRequest request = new OAuthRequest(Verb.GET, Constant.KAKAO_USER_INFO_URI);
            oAuth20Service.signRequest(accessToken, request);
            try (Response response = oAuth20Service.execute(request)){
                Map<String, Object> map = new ObjectMapper().readValue(response.getBody(), HashMap.class);
                return OAuth2UserInfoFactory.getOAuth2UserInfo(provider, map);
            }
        }  else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("Sorry! Login with %s is not supported yet.", provider));
        }

    }

}
