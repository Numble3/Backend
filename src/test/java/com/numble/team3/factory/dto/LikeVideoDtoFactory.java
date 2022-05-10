package com.numble.team3.factory.dto;

import com.numble.team3.like.application.response.GetAllLikeVideoListDto;
import com.numble.team3.like.application.response.GetLikeVideoCategoryListLimitDto;
import com.numble.team3.like.application.response.GetLikeVideoDto;
import com.numble.team3.like.application.response.GetLikeListDto;
import com.numble.team3.like.application.response.GetLikeVideoRankDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.video.application.response.GetVideoDto;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeVideoDtoFactory {

  public static GetLikeVideoDto createGetLikeDto() {
    GetVideoDto video = GetVideoDto.builder()
      .videoId(1L)
      .thumbnailPath("https://thumbnail-url")
      .title("title")
      .nickname("nickname")
      .view(1L)
      .like(1L)
      .createdAt(LocalDateTime.now())
      .build();

    return GetLikeVideoDto.builder()
      .id(1L)
      .createdAt(LocalDateTime.now())
      .getVideoDto(video)
      .build();
  }

  public static GetAllLikeVideoListDto createGetAllLikeListDto() {
    GetLikeVideoDto getLikeVideoDto = createGetLikeDto();

    GetLikeVideoCategoryListLimitDto result
      = new GetLikeVideoCategoryListLimitDto(List.of(getLikeVideoDto), getLikeVideoDto.getId());

    Map<String, GetLikeVideoCategoryListLimitDto> map = new HashMap<>();
    map.put("고양이", result);

    return new GetAllLikeVideoListDto(map);
  }

  public static GetLikeListDto createGetLikeListDto() {
    GetLikeVideoDto getLikeVideoDto = createGetLikeDto();

    return new GetLikeListDto(List.of(getLikeVideoDto), 1L);
  }

  public static List<GetVideoRankDto> createGetVideoRankDtoList() {
    GetLikeVideoRankDto dto = GetLikeVideoRankDto.builder()
      .videoId(1L)
      .thumbnailPath("https://thumbnail-url")
      .title("title")
      .nickname("nickname")
      .view(300L)
      .like(150L)
      .createdAt(LocalDateTime.now().toLocalDate())
      .build();

    GetVideoRankDto result = new GetVideoRankDto(dto, 50L);
    return List.of(result);
  }
}
