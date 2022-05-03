package com.numble.team3.comment.domain;

import com.numble.team3.account.domain.Account;
import com.numble.team3.video.domain.Video;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(name = "tb_comment")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate private LocalDateTime createdAt;
  private String content;

  @Column(columnDefinition = "tinyint(1)")
  private boolean deleteYn = false;

  @ColumnDefault(value = "0")
  @Column(name = "like_count")
  private Long like;

  @ManyToOne private Account account;
  @ManyToOne private Video video;

  // 도메인 행위 메서드
  public void commentDelete() {
    this.deleteYn = true;
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
