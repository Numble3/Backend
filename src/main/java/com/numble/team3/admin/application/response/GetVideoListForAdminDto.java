package com.numble.team3.admin.application.response;

import com.numble.team3.video.domain.Video;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class GetVideoListForAdminDto {
  private List<GetVideoSimpleForAdminDto> contents;
  private Long totalSize;
  private int size;
  private int totalPage;
  private int nowPage;

  private GetVideoListForAdminDto(
      List<GetVideoSimpleForAdminDto> contents,
      Long totalSize,
      int size,
      int totalPage,
      int nowPage) {
    this.contents = contents;
    this.totalSize = totalSize;
    this.size = size;
    this.totalPage = totalPage;
    this.nowPage = nowPage;
  }

  public static GetVideoListForAdminDto fromEntities(Page<Video> contents) {
    return GetVideoListForAdminDto.builder()
        .contents(
            contents.getContent().stream()
                .map(GetVideoSimpleForAdminDto::fromEntity)
                .collect(Collectors.toList()))
        .size(contents.getSize())
        .totalSize(contents.getTotalElements())
        .totalPage(contents.getTotalPages())
        .build();
  }
}
