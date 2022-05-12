package com.numble.team3.comment.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.comment.application.CommentService;
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
public class CommentLikeController {
  private final CommentService commentService;

  @PostMapping("/videos/{videoId}/comments/{commentId}/likes")
  public ResponseEntity createCommentLikeById(
      @LoginUser UserInfo userInfo, @PathVariable Long videoId, @PathVariable Long commentId) {
    commentService.createCommentLikeById(userInfo, videoId, commentId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/videos/{videoId}/comments/{commentId}/likes")
  public ResponseEntity deleteCommentLikeById(
      @LoginUser UserInfo userInfo, @PathVariable Long videoId, @PathVariable Long commentId) {
    commentService.deleteCommentLikeById(userInfo, videoId, commentId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
