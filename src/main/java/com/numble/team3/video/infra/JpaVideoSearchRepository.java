package com.numble.team3.video.infra;

import com.numble.team3.video.domain.Video;
import com.numble.team3.video.resolver.SearchCondition;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface JpaVideoSearchRepository {
  List<Video> searchVideoByCondition (SearchCondition filter, Pageable pageable);
}
