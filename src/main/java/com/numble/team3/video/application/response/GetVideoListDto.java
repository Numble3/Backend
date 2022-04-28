package com.numble.team3.video.application.response;

import com.numble.team3.video.domain.Video;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GetVideoListDto {
  private List<GetVideoDto> contents;
  private boolean hasNext;

  @Getter
  static class GetVideoDto {
    private String thumbnailPath;
    private String title;
    private String nickname;
    private long view;
    private long like;
    private LocalDateTime createdAt;

    @Builder
    public GetVideoDto(
        String thumbnailPath,
        String title,
        String nickname,
        long view,
        long like,
        LocalDateTime createdAt) {
      this.thumbnailPath = thumbnailPath;
      this.title = title;
      this.nickname = nickname;
      this.view = view;
      this.like = like;
      this.createdAt = createdAt;
    }

    static GetVideoDto fromEntity(Video video) {
      return GetVideoDto.builder()
          .thumbnailPath(video.getThumbnailUrl())
          .title(video.getTitle())
          .nickname(video.getAccount().getNickname())
          .view(video.getView())
          .like(video.getLike())
          .createdAt(video.getCreateAt())
          .build();
    }
  }

  private GetVideoListDto(List<GetVideoDto> contents, boolean hasNext) {
    this.contents = contents;
    this.hasNext = hasNext;
  }

  public static GetVideoListDto fromEntities(List<Video> contents) {
    return new GetVideoListDto(
        contents.stream().map(GetVideoDto::fromEntity).collect(Collectors.toList()),
        contents.size() != 0);
  }
}
