package com.numble.team3.video.resolver;

import com.numble.team3.video.annotation.SearchFilter;
import com.numble.team3.video.domain.enums.VideoCategory;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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

    String queryString = request.getQueryString();
    if (queryString == null) {
      return null;
    }
    String title = null;
    VideoCategory category = null;
    for (String queries : queryString.split("&")) {
      String[] params = queries.split("=");
      if (params.length > 1) {
        if (params[0].equals("title")) {
          title = params[1];
        } else if (params[0].equals("category")) {
          category = VideoCategory.from(params[1]);
        }
      }
    }
    return new SearchCondition(title, category);
  }
}
