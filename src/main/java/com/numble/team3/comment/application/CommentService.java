package com.numble.team3.comment.application;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.comment.application.request.CreateOrUpdateCommentDto;
import com.numble.team3.comment.application.response.GetCommentListDto;
import com.numble.team3.comment.domain.AccountLikeComment;
import com.numble.team3.comment.domain.Comment;
import com.numble.team3.comment.domain.CommentSortCondition;
import com.numble.team3.comment.infra.JpaCommentLikeRepository;
import com.numble.team3.comment.infra.JpaCommentRepository;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.comment.CommentNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.infra.JpaVideoRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final JpaCommentRepository commentRepository;
  private final JpaAccountRepository accountRepository;
  private final JpaVideoRepository videoRepository;
  private final JpaCommentLikeRepository commentLikeRepository;

  private Video findByVideoId(Long videoId) {
    return videoRepository.findById(videoId).orElseThrow(VideoNotFoundException::new);
  }

  private Account findByAccountId(UserInfo userInfo) {
    return accountRepository
        .findById(userInfo.getAccountId())
        .orElseThrow(AccountNotFoundException::new);
  }

  private Comment findCommentByAccountIdWithId(UserInfo userInfo, Long commentId) {
    return commentRepository
        .findCommentByAccountIdWithId(userInfo.getAccountId(), commentId)
        .orElseThrow(CommentNotFoundException::new);
  }

  @Transactional
  public void createComment(UserInfo userInfo, Long videoId, CreateOrUpdateCommentDto dto) {
    Account account = findByAccountId(userInfo);
    Video video = findByVideoId(videoId);
    Comment comment = Comment.createComment(dto.getContent(), video, account);
    commentRepository.save(comment);
  }

  @Transactional
  public void modifyComment(UserInfo userInfo, Long commentId, CreateOrUpdateCommentDto dto) {
    Comment comment = findCommentByAccountIdWithId(userInfo, commentId);
    comment.changeComment(dto.getContent());
  }

  @Transactional
  public void deleteComment(UserInfo userInfo, Long commentId) {
    Comment comment = findCommentByAccountIdWithId(userInfo, commentId);
    comment.commentDelete();
  }

  @Transactional(readOnly = true)
  public GetCommentListDto getAllCommentByVideoIdWithCondition(
      UserInfo userInfo,
      Long videoId,
      CommentSortCondition commentSortCondition,
      PageRequest pageRequest) {

    if (userInfo == null || userInfo.getAccountId() == null) {
      return GetCommentListDto.fromEntities(
          commentRepository.findAllByVideoIdWithCondition(
              videoId, commentSortCondition, pageRequest));
    }
    Slice<Comment> contents =
        commentRepository.findAllByVideoIdWithCondition(videoId, commentSortCondition, pageRequest);
    List<Long> likeCommentIds =
        commentLikeRepository
            .findAllByAccountIdAndVideoId(
                userInfo.getAccountId(),
                videoId,
                contents.getContent().stream().map(Comment::getId).collect(Collectors.toList()))
            .stream()
            .map(AccountLikeComment::getId)
            .collect(Collectors.toList());
    return GetCommentListDto.fromEntities(contents, likeCommentIds);
  }

  @Transactional
  public void createCommentLikeById(UserInfo userInfo, Long videoId, Long commentId) {
    Comment comment =
        commentRepository
            .findCommentFetchJoinByIdAndAccountId(commentId, userInfo.getAccountId())
            .orElseThrow(CommentNotFoundException::new);
    comment.addCommentLike(comment, videoId, userInfo.getAccountId());
  }

  @Transactional
  public void deleteCommentLikeById(UserInfo userInfo, Long videoId, Long commentId) {
    Comment comment =
        commentRepository
            .findCommentFetchJoinByIdAndAccountId(commentId, userInfo.getAccountId())
            .orElseThrow(CommentNotFoundException::new);
    comment.deleteCommentLike(userInfo.getAccountId(), commentId);
  }
}
