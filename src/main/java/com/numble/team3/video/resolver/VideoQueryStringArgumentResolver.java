package com.numble.team3.video.resolver;

import com.numble.team3.video.annotation.SearchFilter;
import com.numble.team3.video.domain.enums.VideoCategory;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class VideoQueryStringArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterAnnotation(SearchFilter.class) != null
        && parameter.getParameterType().equals(SearchCondition.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {
    final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

    List<NameValuePair> queryString =
        URLEncodedUtils.parse(request.getQueryString(), StandardCharsets.UTF_8);
    if (queryString == null) {
      return null;
    }
    String title = null;
    VideoCategory category = null;

    for (NameValuePair query : queryString) {
      if (query.getName().equals("title")) {
        title = query.getValue();
      } else if (query.getName().equals("category")) {
        category = VideoCategory.from(query.getValue());
      }
    }
    return new SearchCondition(title, category);
  }
}
