package com.numble.team3.video.domain;

import com.numble.team3.account.domain.Account;
import com.numble.team3.comment.domain.Comment;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert // 객체 생성 시 null값은 쿼리에 넣지 않기 위함
@EntityListeners(AuditingEntityListener.class)
public class Video {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long videoDuration;
  private String title;
  private String content;

  @ColumnDefault(value = "0")
  @Column(name = "view_count")
  private Long view;

  @ColumnDefault(value = "0")
  @Column(name = "like_count")
  private Long like;

  @CreatedDate private LocalDateTime createAt;

  @Column(name = "video_url")
  private String videoUrl;

  @Column(name = "embedded_url")
  private String embeddedUrl;

  @Column(name = "thumbnail_url")
  private String thumbnailUrl;

  @Column(columnDefinition = "tinyint(1)")
  private boolean deleteYn = false;

  @Column(columnDefinition = "tinyint(1)")
  private boolean adminDeleteYn = false;

  @ManyToOne private Account account;

  @OneToMany(mappedBy = "video", fetch = FetchType.LAZY)
  private List<Comment> comments = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  private VideoCategory category;

  @Enumerated(EnumType.STRING)
  private VideoType type;

  private Long showId = Long.valueOf(Integer.MAX_VALUE);

  @Builder
  public Video(
      Long videoDuration,
      String title,
      String content,
      String videoUrl,
      String embeddedUrl,
      String thumbnailUrl,
      Account account,
      VideoCategory category,
      VideoType type) {
    this.videoDuration = videoDuration;
    this.title = title;
    this.content = content;
    this.videoUrl = videoUrl;
    this.thumbnailUrl = thumbnailUrl;
    this.embeddedUrl = embeddedUrl;
    this.account = account;
    this.category = category;
    this.type = type;
  }

  public void deleteVideo() {
    this.deleteYn = true;
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  public String getVideoUrlByVideoType() {
    if (this.type == VideoType.VIDEO) {
      return videoUrl;
    }
    return embeddedUrl;
  }

  public void changeVideo(
      String title, String content, String thumbnailUrl, VideoCategory category, VideoType type) {
    this.title = title;
    this.content = content;
    this.thumbnailUrl = thumbnailUrl;
    this.category = category;
    this.type = type;
  }
}
