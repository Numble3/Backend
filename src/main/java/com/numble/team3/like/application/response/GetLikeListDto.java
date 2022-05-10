package com.numble.team3.like.application.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetLikeListDto {

  private List<GetLikeVideoDto> likes;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long lastLikeId;
}

