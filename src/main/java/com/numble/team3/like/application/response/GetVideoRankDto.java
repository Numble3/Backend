package com.numble.team3.like.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetVideoRankDto {

  private GetLikeVideoRankDto videoDto;
  private long likes;
}
