package com.numble.team3.video;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static com.numble.team3.video.factory.VideoFactory.*;

import com.numble.team3.Team3ApplicationTests;
import com.numble.team3.account.domain.Account;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.config.LocalRedisConfig;
import com.numble.team3.config.LocalTestRedisClientConfig;
import com.numble.team3.like.infra.JpaLikeVideoRepository;
import com.numble.team3.video.application.VideoService;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.infra.JpaVideoRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ActiveProfiles("test") // 이 클래스는 test profile로 실행시킴
@ImportAutoConfiguration(classes = {LocalRedisConfig.class, LocalTestRedisClientConfig.class})
@DisplayName("Video Api 테스트")
public class VideoApiTest extends Team3ApplicationTests {
  @Autowired MockMvc mvc;
  @Autowired JpaVideoRepository videoRepository;
  @Autowired JpaAccountRepository accountRepository;
  @Autowired JpaLikeVideoRepository likeVideoRepository;

  @Autowired VideoService videoService;
  Account account;
  List<Video> saveResult;

  @BeforeEach
  void beforeEach() {
    account =
        accountRepository.saveAndFlush(
            Account.createSignUpAccount("test@google.com", "test_nickname", "1234"));
    accountRepository.saveAndFlush(
        Account.createSignUpAccount("anonymous@naver.com", "test", "1234"));
    saveResult =
        videoRepository.saveAll(
            List.of(
                createVideoForTest("catcatcat", 10, 10, VideoCategory.CAT, account),
                createVideoForTest("bird", 100, 100, VideoCategory.BIRD, account),
                createVideoForTest("dogdogdog", 1000, 1000, VideoCategory.DOG, account),
                createVideoForTest(
                    "lizard_highest", 100, Integer.MAX_VALUE, VideoCategory.LIZARD, account),
                createVideoForTest("cat123", 1, 1, VideoCategory.CAT, account),
                createVideoForTest("dog123", 1, 1, VideoCategory.DOG, account)));
  }

  @AfterEach
  void afterEach() {
    videoRepository.deleteAllInBatch();
    accountRepository.deleteAllInBatch();
  }

  @DisplayName("동영상 정렬, 검색, 페이징 테스트 (비로그인)")
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

  @DisplayName("동영상 상세 조회 테스트 (비로그인)")
  @Test
  void getVideoDetailTest() throws Exception {
    String url = "/api/videos/" + saveResult.get(3).getId();
    mvc.perform(get(url))
        .andExpect(jsonPath("title").value(saveResult.get(3).getTitle()))
        .andExpect(jsonPath("userLikeVideo").value(false))
        .andExpect(status().is2xxSuccessful())
        .andDo(print());
  }
}
