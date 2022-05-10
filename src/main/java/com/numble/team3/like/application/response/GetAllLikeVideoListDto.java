package com.numble.team3.like.application.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetAllLikeVideoListDto {

  private Map<String, GetLikeVideoCategoryListLimitDto> likes;
}
