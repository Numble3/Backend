package com.numble.team3.comment.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_account_like_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountLikeComment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_like_comment_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "comment_id")
  private Comment likedComment;

  private Long videoId;

  private Long accountId;

  public AccountLikeComment(Comment comment, Long videoId, Long accountId) {
    this.likedComment = comment;
    this.videoId = videoId;
    this.accountId = accountId;
  }
}
