package com.numble.team3.comment.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.comment.application.CommentService;
import com.numble.team3.comment.application.request.CreateOrUpdateCommentDto;
import com.numble.team3.comment.application.response.GetCommentListDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
  private final CommentService commentService;

  @PostMapping("/videos/{videoId}/comments")
  public ResponseEntity createComment(
      @LoginUser UserInfo userInfo,
      @Valid @RequestBody CreateOrUpdateCommentDto dto,
      @PathVariable Long videoId) {
    commentService.createComment(userInfo, videoId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/videos/{videoId}/comments/{commentId}")
  public ResponseEntity modifyComment(
      @LoginUser UserInfo userInfo,
      @Valid @RequestBody CreateOrUpdateCommentDto dto,
      @PathVariable Long videoId,
      @PathVariable Long commentId) {
    commentService.modifyComment(userInfo, commentId, dto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("/videos/{videoId}/comments/{commentId}")
  public ResponseEntity deleteComment(
      @LoginUser UserInfo userInfo, @PathVariable Long videoId, @PathVariable Long commentId) {
    commentService.deleteComment(userInfo, commentId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/videos/{videoId}/comments")
  public ResponseEntity<GetCommentListDto> getAllCommentByVideoId(
      @PathVariable Long videoId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size) {
    return ResponseEntity.ok(
        commentService.getAllCommentByVideoId(videoId, PageRequest.of(page, size)));
  }
}
