package com.dss.springboot.backend.apirest.auth;

import com.dss.springboot.backend.apirest.models.entity.User;
import com.dss.springboot.backend.apirest.models.services.UserService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AdditionalInfoToken implements TokenEnhancer {

    private final UserService userService;

    public AdditionalInfoToken(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

        User user = userService.findByUsername(oAuth2Authentication.getName());

        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("additional_info", "Hi ".concat(oAuth2Authentication.getName()).concat("!"));
        additionalInfo.put("name", user.getName());
        additionalInfo.put("lastname", user.getLastname());
        additionalInfo.put("email", user.getEmail());

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionalInfo);

        return oAuth2AccessToken;
    }
}
