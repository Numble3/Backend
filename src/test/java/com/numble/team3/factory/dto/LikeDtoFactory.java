package com.numble.team3.factory.dto;

import com.numble.team3.like.application.response.GetAllLikeListDto;
import com.numble.team3.like.application.response.GetLikeCategoryListLimitDto;
import com.numble.team3.like.application.response.GetLikeDto;
import com.numble.team3.like.application.response.GetLikeListDto;
import com.numble.team3.like.application.response.GetLikeRankVideoDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.video.application.response.GetVideoDto;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeDtoFactory {

  public static GetLikeDto createGetLikeDto() {
    GetVideoDto video = GetVideoDto.builder()
      .videoId(1L)
      .thumbnailPath("https://thumbnail-url")
      .title("title")
      .nickname("nickname")
      .view(1L)
      .like(1L)
      .createdAt(LocalDateTime.now())
      .build();

    return GetLikeDto.builder()
      .id(1L)
      .createdAt(LocalDateTime.now())
      .getVideoDto(video)
      .build();
  }

  public static GetAllLikeListDto createGetAllLikeListDto() {
    GetLikeDto getLikeDto = createGetLikeDto();

    GetLikeCategoryListLimitDto result
      = new GetLikeCategoryListLimitDto(List.of(getLikeDto), getLikeDto.getId());

    Map<String, GetLikeCategoryListLimitDto> map = new HashMap<>();
    map.put("고양이", result);

    return new GetAllLikeListDto(map);
  }

  public static GetLikeListDto createGetLikeListDto() {
    GetLikeDto getLikeDto = createGetLikeDto();

    return new GetLikeListDto(List.of(getLikeDto), 1L);
  }

  public static List<GetVideoRankDto> createGetVideoRankDtoList() {
    GetLikeRankVideoDto dto = GetLikeRankVideoDto.builder()
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
