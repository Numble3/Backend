package com.numble.team3.admin.application.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetAdminDeleteVideoStateDto {
  @Schema(description = "삭제된 동영상 ID")
  private Long videoId;

  @Schema(description = "동영상 상태, (delete: 삭제)")
  private String videoState;
}
