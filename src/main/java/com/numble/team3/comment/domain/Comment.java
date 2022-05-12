package com.numble.team3.comment.domain;

import com.numble.team3.account.domain.Account;
import com.numble.team3.common.entity.BaseTimeEntity;
import com.numble.team3.video.domain.Video;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Entity
@Table(name = "tb_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Comment extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String content;

  @Column(columnDefinition = "tinyint(1)")
  private boolean deleteYn = false;

  @ColumnDefault(value = "0")
  @Column(name = "like_count")
  private Long like;

  @ManyToOne private Account account;

  @ManyToOne private Video video;

  @OneToMany(
      mappedBy = "likedComment",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<AccountLikeComment> accountLikeCommentList = new ArrayList<>();

  // 도메인 행위 메서드
  public void commentDelete() {
    this.deleteYn = true;
  }

  public void deleteCommentLike(Long accountId, Long commentId) {
    if (!accountLikeCommentList.stream()
        .filter(entity -> entity.getAccountId() == accountId)
        .findAny()
        .isPresent()) {
      return;
    }
    this.like -= 1L;
    this.accountLikeCommentList.removeIf(
        entity ->
            (entity.getLikedComment().getId() == commentId && entity.getAccountId() == accountId));
  }

  public void addCommentLike(Comment comment, Long videoId, Long accountId) {
    if (accountLikeCommentList.stream()
        .filter(entity -> entity.getAccountId() == accountId)
        .findAny()
        .isPresent()) {
      return;
    }
    this.like += 1L;
    this.accountLikeCommentList.add(new AccountLikeComment(comment, videoId, accountId));
  }

  private Comment(String content, Video video, Account account) {
    this.content = content;
    if (video != null) {
      video.addComment(this);
      this.video = video;
    }
    this.account = account;
  }

  public static Comment createComment(String content, Video video, Account account) {
    return new Comment(content, video, account);
  }

  public void changeComment(String content) {
    this.content = content;
  }
}
