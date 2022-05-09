package com.numble.team3.video.infra;

import com.numble.team3.video.domain.Video;
import com.numble.team3.video.resolver.SearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface JpaVideoSearchRepository {
  Slice<Video> searchVideoByCondition(SearchCondition filter, Pageable pageable);
}
