package com.numble.team3.comment.infra;

import com.numble.team3.comment.domain.AccountLikeComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaCommentLikeRepository extends JpaRepository<AccountLikeComment, Long> {
  @Query(
      "SELECT a FROM AccountLikeComment a WHERE a.videoId = :videoId AND a.accountId = :accountId AND a.likedComment.id IN :commentIds")
  List<AccountLikeComment> findAllByAccountIdAndVideoId(
      @Param("accountId") Long accountId,
      @Param("videoId") Long videoId,
      @Param("commentIds") List<Long> commentIds);
}
