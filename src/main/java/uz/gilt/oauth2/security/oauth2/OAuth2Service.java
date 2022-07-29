package uz.gilt.oauth2.security.oauth2;

import com.github.scribejava.apis.KakaoApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

public class OAuth2Service {
    public final static OAuth20Service kakao = new ServiceBuilder(Constant.KAKAO_CLIENT_ID)
            .apiSecret(Constant.KAKAO_API_SECRET)
            .build(KakaoApi.instance());
}
