package com.numble.team3.video.application.response;

import com.numble.team3.video.domain.Video;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class GetVideoListDto {
  @Schema(description = "내용")
  private List<GetVideoDto> contents;

  @Schema(description = "다음 내용 존재 여부")
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
