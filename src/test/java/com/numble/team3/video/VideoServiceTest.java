package com.numble.team3.video;

import static com.numble.team3.factory.entity.VideoEntityFactory.*;
import static com.numble.team3.factory.entity.AccountEntityFactory.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.numble.team3.Team3ApplicationTests;
import com.numble.team3.account.domain.Account;
import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.like.infra.JpaLikeVideoRepository;
import com.numble.team3.video.application.VideoService;
import com.numble.team3.video.controller.VideoController;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoType;
import com.numble.team3.video.infra.JpaVideoRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@AutoConfigureMockMvc
@DisplayName("VideoService 테스트")
public class VideoServiceTest extends Team3ApplicationTests {
  @Autowired MockMvc mvc;
  @Autowired JpaVideoRepository videoRepository;
  @Autowired JpaAccountRepository accountRepository;
  @Autowired JpaLikeVideoRepository likeVideoRepository;
  Account account;

  Video createVideoForTest(String title, long view, long like, VideoCategory category) {
    Video video =
        Video.builder()
            .category(category)
            .videoDuration(10L)
            .videoUrl("https://video-url")
            .thumbnailUrl("https://thumbnail-url")
            .type(VideoType.VIDEO)
            .title(title)
            .account(account)
            .content("content")
            .build();
    setField(video, "like", like);
    setField(video, "view", view);
    return video;
  }

  @BeforeEach
  void beforeEach() {
    account =
        accountRepository.saveAndFlush(
            Account.createSignUpAccount("test@google.com", "test_nickname", "1234"));
    videoRepository.saveAll(
        List.of(
            createVideoForTest("catcatcat", 10, 10, VideoCategory.CAT),
            createVideoForTest("bird", 100, 100, VideoCategory.BIRD),
            createVideoForTest("dogdogdog", 1000, 1000, VideoCategory.DOG),
            createVideoForTest("lizard_highest", 100, Integer.MAX_VALUE, VideoCategory.LIZARD),
            createVideoForTest("cat123", 1, 1, VideoCategory.CAT),
            createVideoForTest("dog123", 1, 1, VideoCategory.DOG)));
  }

  @AfterEach
  void afterEach() {
    videoRepository.deleteAllInBatch();
    accountRepository.deleteAllInBatch();
  }

  @DisplayName("동영상 정렬, 검색, 페이징 테스트")
  @ParameterizedTest
  @CsvSource(
      value = {
        "/api/videos?sort=POPULARITY,lizard_highest,true",
        "/api/videos?title=dog,dogdogdog,false",
        "/api/videos?category=CAT,catcatcat,false"
      },
      delimiter = ',')
  void getAllVideoTest(String url, String title, String hasNext) throws Exception {
    mvc.perform(get(url))
        .andExpect(jsonPath("$.contents[0].title").value(title))
        .andExpect(jsonPath("hasNext").value(Boolean.valueOf(hasNext)))
        .andExpect(jsonPath("likeVideoIds").isEmpty())
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }
}
