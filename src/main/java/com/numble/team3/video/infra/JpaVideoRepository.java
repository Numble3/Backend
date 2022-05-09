package com.numble.team3.video.infra;

import com.numble.team3.video.domain.Video;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVideoRepository extends JpaRepository<Video, Long>, JpaVideoSearchRepository {
  Optional<Video> findByAccountIdAndId(Long accountId, Long videoId);

  @Modifying
  @Query("UPDATE Video v SET v.view = v.view + :viewCount WHERE v.id = :videoId")
  void updateVideoViewCount(@Param("viewCount") Long viewCount, @Param("videoId") Long videoId);
}
