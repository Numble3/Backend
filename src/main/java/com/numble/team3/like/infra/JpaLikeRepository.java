package com.numble.team3.like.infra;

import com.numble.team3.like.domain.Like;
import com.numble.team3.video.domain.enums.VideoCategory;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaLikeRepository extends JpaRepository<Like, Long>, CustomJpaLikeRepository {

  boolean existsById(Long likeId);

  @Query("SELECT l FROM Like l JOIN FETCH l.video WHERE l.accountId = :accountId AND l.video.category = :category")
  List<Like> getAllLikesByAccountIdAndCategory(@Param("accountId") Long accountId,
    @Param("category") VideoCategory category, Pageable pageable);

  default List<Like> getAllLikesWithLimit(Long accountId, VideoCategory category, int limit) {
    return getAllLikesByAccountIdAndCategory(accountId, category,
      PageRequest.of(0, limit, Sort.by("id").descending()));
  }

  default List<Like> getLikesCategoryWithPaging(Long accountId, VideoCategory category, Pageable pageable) {
    return getAllLikesByAccountIdAndCategory(accountId, category, pageable);
  }

}
