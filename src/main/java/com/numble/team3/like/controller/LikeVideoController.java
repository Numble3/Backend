package com.numble.team3.like.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.like.annotation.AddLikeVideoSwagger;
import com.numble.team3.like.annotation.DeleteLikeVideoSwagger;
import com.numble.team3.like.annotation.GetAllLikeVideosSwagger;
import com.numble.team3.like.annotation.GetLikeVideosByCategorySwagger;
import com.numble.team3.like.annotation.GetVideoRankByDayCategoryLikeSwagger;
import com.numble.team3.like.annotation.GetVideoRankByDayLikeSwagger;
import com.numble.team3.like.application.LikeVideoService;
import com.numble.team3.like.application.response.GetAllLikeVideoListDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import com.numble.team3.video.domain.enums.VideoCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = {"관심영상 조회, 일일 랭킹, 좋아요 추가, 좋아요 삭제"})
public class LikeVideoController {

  private final LikeVideoService likeVideoService;

  @AddLikeVideoSwagger
  @PostMapping(value = "/likes/add", produces = "application/json")
  public ResponseEntity addLike(
    @ApiIgnore @LoginUser UserInfo userInfo,
    @ApiParam(value = "비디오 id", required = true) @RequestParam(name = "id") Long videoId,
    @ApiParam(value = "카테고리", required = true) @RequestParam(name = "category") String categoryName) {
    likeVideoService.addLike(userInfo, videoId, VideoCategory.from(categoryName));
    return new ResponseEntity(HttpStatus.CREATED);
  }

  @DeleteLikeVideoSwagger
  @DeleteMapping(value = "/likes/delete", produces = "application/json")
  public ResponseEntity deleteLike(
    @ApiIgnore @LoginUser UserInfo userInfo,
    @ApiParam(value = "비디오 id", required = true) @RequestParam(name = "id") Long videoId) {
    likeVideoService.deleteLike(userInfo, videoId);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetAllLikeVideosSwagger
  @GetMapping(value = "/likes/all", produces = "application/json")
  public ResponseEntity<GetAllLikeVideoListDto> getLikesHierarchy(@ApiIgnore @LoginUser UserInfo userInfo) {
    return ResponseEntity.ok(likeVideoService.getLikesHierarchy(userInfo));
  }

  @GetLikeVideosByCategorySwagger
  @GetMapping(value = "/likes", produces = "application/json")
  public ResponseEntity getLikesByCategory(
    @ApiIgnore @LoginUser UserInfo userInfo,
    @ApiParam(value = "카테고리 이름", required = true) @RequestParam(name = "category") String categoryName,
    @ApiParam(value = "like id, 2페이지 이후 조회를 위한 값, 입력하지 않거나 0일 때 1페이지 요청", required = false) @RequestParam(required = false, name = "id") Long likeId,
    @ApiParam(value = "페이지 크기", required = true) @RequestParam(name = "size") int size) {
    return ResponseEntity.ok(
      likeVideoService.getLikesByCategory(userInfo, categoryName, likeId, size));
  }

  @GetVideoRankByDayLikeSwagger
  @GetMapping(value = "/likes/rank/day", produces = "application/json")
  public ResponseEntity<Map> getRankByDay() {
    return new ResponseEntity(new HashMap<String, List<GetVideoRankDto>>() {
      {
        put("ranking", likeVideoService.getRank("day"));
      }
    }, HttpStatus.OK);
  }

  @GetVideoRankByDayCategoryLikeSwagger
  @GetMapping(value = "/likes/rank/day/{category}", produces = "application/json")
  public ResponseEntity<Map> getRankByDayAndCategory(
    @ApiParam(value = "카테고리 이름", required = true) @PathVariable(name = "category") String category) {
    VideoCategory videoCategory = VideoCategory.from(category);
    return new ResponseEntity(new HashMap<String, List<GetVideoRankDto>>() {
      {
        put(videoCategory.getName(), likeVideoService.getRank("day", videoCategory));
      }
    }, HttpStatus.OK);
  }
}
