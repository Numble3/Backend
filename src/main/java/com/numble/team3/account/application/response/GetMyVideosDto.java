package com.numble.team3.account.application.response;

import com.numble.team3.video.application.response.GetVideoDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyVideosDto {

  private List<GetVideoDto> videos;
  private Long lastVideoId;
}
