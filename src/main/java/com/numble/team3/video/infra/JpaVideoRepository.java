package com.numble.team3.video.infra;

import com.numble.team3.video.domain.Video;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaVideoRepository extends JpaRepository<Video, Long> {
  Optional<Video> findByAccountIdAndId(Long accountId, Long videoId);

  @Query("SELECT v FROM Video v JOIN FETCH v.account WHERE v.deleteYn = false")
  List<Video> findAllWithAccount(Pageable pageable);
}
