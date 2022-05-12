package com.numble.team3.comment.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.comment.annotation.CreateCommentLikeSwagger;
import com.numble.team3.comment.annotation.DeleteCommentLikeSwagger;
import com.numble.team3.comment.application.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "댓글 좋아요 생성, 삭제 api")
public class CommentLikeController {
  private final CommentService commentService;

  @CreateCommentLikeSwagger
  @PostMapping("/videos/{videoId}/comments/{commentId}/likes")
  public ResponseEntity createCommentLikeById(
      @LoginUser UserInfo userInfo,
      @ApiParam(name = "영상 ID", required = true) @PathVariable Long videoId,
      @ApiParam(name = "댓글 ID", required = true) @PathVariable Long commentId) {
    commentService.createCommentLikeById(userInfo, videoId, commentId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteCommentLikeSwagger
  @DeleteMapping("/videos/{videoId}/comments/{commentId}/likes")
  public ResponseEntity deleteCommentLikeById(
      @LoginUser UserInfo userInfo,
      @ApiParam(name = "영상 ID", required = true) @PathVariable Long videoId,
      @ApiParam(name = "댓글 ID", required = true) @PathVariable Long commentId) {
    commentService.deleteCommentLikeById(userInfo, videoId, commentId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
