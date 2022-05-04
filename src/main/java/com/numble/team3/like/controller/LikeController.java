package com.numble.team3.like.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.like.application.LikeService;
import com.numble.team3.like.application.response.GetAllLikeListDto;
import com.numble.team3.like.application.response.GetVideoRankDto;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

  private final LikeService likeService;

  @PostMapping(value = "/likes/add", produces = "application/json")
  public ResponseEntity addLike(
    @LoginUser UserInfo userInfo,
    @RequestParam(name = "id") Long videoId) {
      likeService.addLike(userInfo, videoId);
      return new ResponseEntity(HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/likes/delete", produces = "application/json")
  public ResponseEntity deleteLike(@RequestParam(name = "id") Long likeId) {
    likeService.deleteLike(likeId);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GetMapping(value = "/likes/all", produces = "application/json")
  public ResponseEntity<GetAllLikeListDto> getLikesHierarchy(
    @LoginUser UserInfo userInfo) {
    return ResponseEntity.ok(likeService.getLikesHierarchy(userInfo));
  }

  @GetMapping(value = "/likes", produces = "application/json")
  public ResponseEntity getLikesByCategory(
    @LoginUser UserInfo userInfo,
    @RequestParam(name = "category") String categoryName,
    @RequestParam(required = false, name = "id") Long likeId,
    @RequestParam(name = "size") int size) {
      return ResponseEntity.ok(likeService.getLikesByCategory(userInfo, categoryName, likeId, size));
  }

  @GetMapping(value = "/likes/rank/day", produces = "application/json")
  public ResponseEntity<Map> getRankByDay() {
    return new ResponseEntity(new HashMap<String, List<GetVideoRankDto>>() {
      {
        put("ranking", likeService.getRank("day"));
      }
    }, HttpStatus.OK);
  }
}
