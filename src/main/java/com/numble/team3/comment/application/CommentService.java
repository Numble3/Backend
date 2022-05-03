package com.numble.team3.comment.application;

import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.comment.application.request.CreateOrUpdateCommentDto;
import com.numble.team3.comment.application.response.GetCommentListDto;
import com.numble.team3.comment.domain.Comment;
import com.numble.team3.comment.infra.JpaCommentRepository;
import com.numble.team3.exception.account.AccountNotFoundException;
import com.numble.team3.exception.comment.CommentNotFoundException;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.infra.JpaVideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final JpaCommentRepository commentRepository;
  private final JpaAccountRepository accountRepository;
  private final JpaVideoRepository videoRepository;

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
  public GetCommentListDto getAllCommentByVideoId(Long videoId, PageRequest pageRequest) {
    return GetCommentListDto.fromEntities(
        commentRepository.findAllByVideoId(videoId, pageRequest).getContent());
  }
}
