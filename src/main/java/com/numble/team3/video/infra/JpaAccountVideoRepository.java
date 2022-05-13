package com.numble.team3.video.infra;

import com.numble.team3.video.application.response.GetVideoDto;
import com.numble.team3.video.domain.enums.VideoSortCondition;
import java.util.List;

public interface JpaAccountVideoRepository {

  List<GetVideoDto> getMyVideoDtos(VideoSortCondition sort, Long accountId, Long videoId, int limit);
}
