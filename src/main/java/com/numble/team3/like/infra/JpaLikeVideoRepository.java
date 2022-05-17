package com.numble.team3.like.infra;

import com.numble.team3.like.domain.LikeVideo;
import com.numble.team3.video.domain.enums.VideoCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaLikeVideoRepository extends JpaRepository<LikeVideo, Long>,
  CustomJpaLikeVideoRepository {

  @Query("SELECT l FROM LikeVideo l WHERE l.accountId = :accountId AND l.video.id = :videoId")
  Optional<LikeVideo> getLikeByAccountIdAndVideoId(Long accountId, Long videoId);

  @Query("SELECT l FROM LikeVideo l JOIN FETCH l.video WHERE l.accountId = :accountId AND l.category = :category")
  List<LikeVideo> getAllLikesByAccountIdAndCategory(@Param("accountId") Long accountId,
    @Param("category") VideoCategory category, Pageable pageable);

  default List<LikeVideo> getAllLikesWithLimit(Long accountId, VideoCategory category, int limit) {
    return getAllLikesByAccountIdAndCategory(accountId, category,
      PageRequest.of(0, limit, Sort.by("id").descending()));
  }

  default List<LikeVideo> getLikesCategoryWithPaging(Long accountId, VideoCategory category, Pageable pageable) {
    return getAllLikesByAccountIdAndCategory(accountId, category, pageable);
  }

  @Query("SELECT l FROM LikeVideo l JOIN FETCH l.video v WHERE v.id IN :videoIds AND l.accountId = :accountId")
  List<LikeVideo> getLikesByAccountId(@Param("videoIds") List<Long> videoIds, @Param("accountId") Long accountId);

  @Query("SELECT l FROM LikeVideo l JOIN FETCH l.video v WHERE v.id = :videoId AND l.accountId = :accountId")
  Optional<LikeVideo> existsLikeByVideoIdAndAccountId(@Param("videoId") Long videoId, @Param("accountId") Long accountId);
}