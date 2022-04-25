package com.numble.team3.security;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {

  private Map<String, Object> attributes;
  private String attributeKey;
  private String email;
  private String nickname;
  private String profile;

  static OAuth2Attribute of(String provider, String attributeKey, Map<String, Object> attributes) {
    switch (provider) {
      case "naver":
        return ofNaver("id", attributes);
      case "kakao":
        return ofKakao("email", attributes);
      case "google":
        return ofGoogle(attributeKey, attributes);
      default:
        throw new RuntimeException();
    }
  }

  private static OAuth2Attribute ofNaver(String attributeKey, Map<String, Object> attributes) {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    return OAuth2Attribute.builder().nickname((String) response.get("nickname"))
      .email((String) response.get("email")).profile((String) response.get("profile_image"))
      .attributes(response).attributeKey(attributeKey).build();
  }

  private static OAuth2Attribute ofKakao(String attributeKey, Map<String, Object> attributes) {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

    return OAuth2Attribute.builder().nickname((String) kakaoProfile.get("nickname"))
      .email(String.valueOf(attributes.get("id")))
      .profile((String) kakaoProfile.get("thumbnail_image_url")).attributes(kakaoAccount)
      .attributeKey(attributeKey).build();
  }

  private static OAuth2Attribute ofGoogle(String attributeKey, Map<String, Object> attributes) {
    return OAuth2Attribute.builder().nickname((String) attributes.get("name"))
      .email((String) attributes.get("email")).profile((String) attributes.get("picture"))
      .attributes(attributes).attributeKey(attributeKey).build();
  }

  Map<String, Object> convertToMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", attributeKey);
    map.put("key", attributeKey);
    map.put("nickname", nickname);
    map.put("email", email);
    map.put("profile", profile);

    return map;
  }
}

