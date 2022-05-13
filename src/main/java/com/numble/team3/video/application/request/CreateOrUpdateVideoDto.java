package com.numble.team3.video.application.request;

import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateOrUpdateVideoDto {
  @Schema(description = "영상 제목")
  @NotBlank(message = "영상 제목은 반드시 있어야 합니다.")
  private String title;

  @Schema(description = "영상 내용")
  @NotBlank(message = "영상 내용은 반드시 있어야 합니다.")
  private String content;

  @Schema(description = "썸네일 경로")
  @NotBlank(message = "썸네일 경로는 반드시 있어야 합니다.")
  @URL(message = "유효하지 않은 URL입니다.")
  private String thumbnailUrl;

  @Schema(description = "영상 경로 (직접 업로드, 임베드 업로드 포함)")
  @URL(message = "유효하지 않은 URL입니다.")
  private String videoUrl;

  @Schema(description = "영상 시간")
  @DecimalMin(value = "0", message = "영상 길이는 최소 0보다 커야 합니다.")
  private Long videoDuration;

  @Schema(description = "영상 카테고리")
  private VideoCategory category;

  @Schema(description = "영상 타입")
  private VideoType type;
}
