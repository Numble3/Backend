package com.numble.team3.like.infra;

import com.numble.team3.like.domain.LikeVideo;
import com.numble.team3.video.domain.enums.VideoCategory;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaLikeVideoRepository extends JpaRepository<LikeVideo, Long>,
  CustomJpaLikeVideoRepository {

  boolean existsById(Long likeId);

  @Query("SELECT l FROM LikeVideo l JOIN FETCH l.video WHERE l.accountId = :accountId AND l.video.category = :category")
  List<LikeVideo> getAllLikesByAccountIdAndCategory(@Param("accountId") Long accountId,
    @Param("category") VideoCategory category, Pageable pageable);

  default List<LikeVideo> getAllLikesWithLimit(Long accountId, VideoCategory category, int limit) {
    return getAllLikesByAccountIdAndCategory(accountId, category,
      PageRequest.of(0, limit, Sort.by("id").descending()));
  }

  default List<LikeVideo> getLikesCategoryWithPaging(Long accountId, VideoCategory category, Pageable pageable) {
    return getAllLikesByAccountIdAndCategory(accountId, category, pageable);
  }

}
