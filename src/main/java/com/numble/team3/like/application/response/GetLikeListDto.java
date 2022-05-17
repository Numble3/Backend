package com.numble.team3.like.application.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetLikeListDto {

  private List<GetLikeVideoDto> likes;

  private Long lastLikeId;
}

