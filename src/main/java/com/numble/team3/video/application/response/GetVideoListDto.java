package com.numble.team3.video.application.response;

import com.numble.team3.video.domain.Video;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class GetVideoListDto {
  private List<GetVideoDto> contents;
  private boolean hasNext;

  private GetVideoListDto(List<GetVideoDto> contents, boolean hasNext) {
    this.contents = contents;
    this.hasNext = hasNext;
  }

  public static GetVideoListDto fromEntities(Slice<Video> contents) {
    return new GetVideoListDto(
        contents.stream().map(GetVideoDto::fromEntity).collect(Collectors.toList()),
        contents.hasNext());
  }
}
