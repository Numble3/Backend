package com.numble.team3.video.infra;

import com.numble.team3.video.domain.Video;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVideoRepository
    extends JpaRepository<Video, Long>, JpaVideoSearchRepository, JpaAccountVideoRepository {
  Optional<Video> findByAccountIdAndId(Long accountId, Long videoId);

  @Modifying
  @Query("UPDATE Video v SET v.view = v.view + :viewCount WHERE v.id = :videoId")
  void updateVideoViewCount(@Param("viewCount") Long viewCount, @Param("videoId") Long videoId);

  @Query("SELECT v FROM Video v WHERE v.account.id = :id AND v.deleteYn = false")
  List<Video> findAllByAccountId(@Param("id") Long accountId, Pageable pageable);

  default List<Video> findAllByAccountIdAndLimit(Long accountId, int limit) {
    return findAllByAccountId(accountId, PageRequest.of(0, limit, Sort.by("id").descending()));
  }

  @Query("SELECT v FROM Video v WHERE v.account.id = :accountId")
  Page<Video> findAllByAccountIdWithAdmin(@Param("accountId") Long accountId, Pageable pageable);

  @Query(
      "SELECT v FROM Video v WHERE v.id = :videoId AND v.deleteYn = false AND v.adminDeleteYn = false")
  Optional<Video> findByIdNotDeleted(@Param("videoId") Long videoId);
}
