package com.numble.team3.video.application.response;

import com.numble.team3.like.domain.LikeVideo;
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

  @Schema(description = "유저가 좋아요를 누른 동영상들의 ID (리스트)")
  private List<Long> likeVideoIds;

  @Schema(description = "다음 내용 존재 여부")
  private boolean hasNext;

  private GetVideoListDto(List<GetVideoDto> contents, List<Long> likeVideoIds, boolean hasNext) {
    this.contents = contents;
    this.likeVideoIds = likeVideoIds;
    this.hasNext = hasNext;
  }

  public static GetVideoListDto fromEntities(Slice<Video> contents, List<LikeVideo> likeVideoIds) {
    return new GetVideoListDto(
        contents.stream().map(GetVideoDto::fromEntity).collect(Collectors.toList()),
        likeVideoIds.stream()
            .map(likeVideo -> likeVideo.getVideo().getId())
            .collect(Collectors.toList()),
        contents.hasNext());
  }
}
