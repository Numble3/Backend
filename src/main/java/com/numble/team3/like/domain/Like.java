package com.numble.team3.like.domain;

import com.numble.team3.common.entity.BaseTimeEntity;
import com.numble.team3.video.domain.Video;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "like_id")
  private Long id;

  @OneToOne
  @JoinColumn(name = "video_id")
  private Video video;

  @Column(name = "account_id")
  private Long accountId;

  @Builder
  public Like(Video video, Long accountId) {
    this.video = video;
    this.accountId = accountId;
  }
}
