package com.numble.team3.comment.controller;

import com.numble.team3.account.annotation.LoginUser;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.comment.annotation.CreateCommentSwagger;
import com.numble.team3.comment.annotation.DeleteCommentSwagger;
import com.numble.team3.comment.annotation.GetAllCommentSwagger;
import com.numble.team3.comment.annotation.PutCommentSwagger;
import com.numble.team3.comment.application.CommentService;
import com.numble.team3.comment.application.request.CreateOrUpdateCommentDto;
import com.numble.team3.comment.application.response.GetCommentListDto;
import com.numble.team3.comment.domain.CommentSortCondition;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "댓글 생성, 수정, 삭제, 조회 api")
public class CommentController {
  private final CommentService commentService;

  @CreateCommentSwagger
  @PostMapping("/videos/{videoId}/comments")
  public ResponseEntity createComment(
      @LoginUser UserInfo userInfo,
      @Valid @RequestBody CreateOrUpdateCommentDto dto,
      @ApiParam(name = "영상 ID", required = true) @PathVariable Long videoId) {
    commentService.createComment(userInfo, videoId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutCommentSwagger
  @PutMapping("/videos/{videoId}/comments/{commentId}")
  public ResponseEntity modifyComment(
      @LoginUser UserInfo userInfo,
      @Valid @RequestBody CreateOrUpdateCommentDto dto,
      @ApiParam(name = "영상 ID", required = true) @PathVariable Long videoId,
      @ApiParam(name = "댓글 ID", required = true) @PathVariable Long commentId) {
    commentService.modifyComment(userInfo, commentId, dto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteCommentSwagger
  @DeleteMapping("/videos/{videoId}/comments/{commentId}")
  public ResponseEntity deleteComment(
      @LoginUser UserInfo userInfo,
      @ApiParam(name = "영상 ID", required = true) @PathVariable Long videoId,
      @ApiParam(name = "댓글 ID", required = true) @PathVariable Long commentId) {
    commentService.deleteComment(userInfo, commentId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetAllCommentSwagger
  @GetMapping("/videos/{videoId}/comments")
  public ResponseEntity<GetCommentListDto> getAllCommentByVideoId(
      @LoginUser UserInfo userInfo,
      @ApiParam(name = "영상 ID", required = true) @PathVariable Long videoId,
      @ApiParam(name = "페이지 번호", defaultValue = "0", required = true)
          @RequestParam(defaultValue = "0")
          int page,
      @ApiParam(name = "페이지 크기", defaultValue = "5", required = true)
          @RequestParam(defaultValue = "5")
          int size,
      @ApiParam(name = "댓글 정렬 조건", defaultValue = "인기순", required = true)
          @RequestParam(name = "sort", defaultValue = CommentSortCondition.DEFAULT)
          CommentSortCondition commentSortCondition) {
    return ResponseEntity.ok(
        commentService.getAllCommentByVideoIdWithCondition(
            userInfo, videoId, commentSortCondition, PageRequest.of(page, size)));
  }
}
