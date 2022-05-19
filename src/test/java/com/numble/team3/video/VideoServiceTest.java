package com.numble.team3.video;

import static com.numble.team3.factory.entity.VideoEntityFactory.*;
import static com.numble.team3.factory.entity.AccountEntityFactory.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.assertj.core.api.Assertions.*;

import com.numble.team3.Team3ApplicationTests;
import com.numble.team3.account.domain.Account;
import com.numble.team3.account.domain.RoleType;
import com.numble.team3.account.infra.JpaAccountRepository;
import com.numble.team3.account.resolver.UserInfo;
import com.numble.team3.exception.video.VideoNotFoundException;
import com.numble.team3.like.infra.JpaLikeVideoRepository;
import com.numble.team3.video.application.VideoService;
import com.numble.team3.video.application.request.CreateOrUpdateVideoDto;
import com.numble.team3.video.application.response.GetVideoListDto;
import com.numble.team3.video.domain.Video;
import com.numble.team3.video.domain.enums.VideoCategory;
import com.numble.team3.video.domain.enums.VideoType;
import com.numble.team3.video.infra.JpaVideoRepository;
import com.numble.team3.video.resolver.SearchCondition;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ActiveProfiles("test") //이 클래스는 test profile로 실행시킴
@ImportAutoConfiguration(classes = {LocalRedisConfig.class, LocalTestRedisClientConfig.class})
@DisplayName("VideoService 테스트")
public class VideoServiceTest extends Team3ApplicationTests {
  @Autowired MockMvc mvc;
  @Autowired JpaVideoRepository videoRepository;
  @Autowired JpaAccountRepository accountRepository;
  @Autowired JpaLikeVideoRepository likeVideoRepository;

  @Autowired VideoService videoService;
  Account account;
  List<Video> saveResult;

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
    accountRepository.saveAndFlush(
        Account.createSignUpAccount("anonymous@naver.com", "test", "1234"));
    saveResult =
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

  @DisplayName("없는 동영상 예외")
  @Test
  void getVideoDetailExceptionTest() {
    assertThatThrownBy(() -> videoService.getVideoById(new UserInfo(null, null), 0L))
        .isInstanceOf(VideoNotFoundException.class);
    assertThatThrownBy(() -> videoService.modifyVideo(new UserInfo(null, null), 0L, null))
        .isInstanceOf(VideoNotFoundException.class);
    assertThatThrownBy(() -> videoService.deleteVideo(new UserInfo(null, null), 0L))
        .isInstanceOf(VideoNotFoundException.class);
  }

  @DisplayName("소유자가 아닌 사용자의 수정, 삭제 예외")
  @Test
  void rejectModifyAndDeleteTest() {
    assertThatThrownBy(
            () ->
                videoService.modifyVideo(
                    new UserInfo(2L, List.of(RoleType.ROLE_USER.toString())), 2L, null))
        .isInstanceOf(VideoNotFoundException.class);
    assertThatThrownBy(
            () ->
                videoService.deleteVideo(
                    new UserInfo(2L, List.of(RoleType.ROLE_USER.toString())), 2L))
        .isInstanceOf(VideoNotFoundException.class);
  }

  @DisplayName("동영상 수정 테스트")
  @ParameterizedTest
  @CsvSource(
      value = {"modify,modify,https://modify.com,https://modify.com,33,OTHERS,EMBEDDED"},
      delimiter = ',')
  void modifyVideoTest(
      String title,
      String content,
      String videoUrl,
      String thumbnailUrl,
      String videoDuration,
      String category,
      String type) {
    CreateOrUpdateVideoDto dto =
        new CreateOrUpdateVideoDto(
            title,
            content,
            videoUrl,
            thumbnailUrl,
            Long.valueOf(videoDuration),
            VideoCategory.from(category),
            VideoType.from(type));
    Long videoId = saveResult.get(3).getId();
    videoService.modifyVideo(new UserInfo(account.getId(), List.of("ROLE_USER")), videoId, dto);
    Video video = videoRepository.findById(videoId).get();
    assertThat(video.getTitle()).isEqualTo(title);
    assertThat(video.getContent()).isEqualTo(content);
    assertThat(video.getVideoDuration()).isEqualTo(Long.valueOf(videoDuration));
    assertThat(video.getVideoUrl()).isEqualTo(videoUrl);
    assertThat(video.getThumbnailUrl()).isEqualTo(thumbnailUrl);
    assertThat(video.getCategory().toString()).isEqualTo(category);
    assertThat(video.getType().toString()).isEqualTo(type);
  }

  @DisplayName("동영상 삭제 테스트")
  @Test
  void videoDeleteTest() {
    Long videoId = saveResult.get(3).getId();
    videoService.deleteVideo(new UserInfo(account.getId(), List.of("ROLE_USER")), videoId);
    GetVideoListDto list =
        videoService.getAllVideoByCondition(
            null, new SearchCondition(null, null, null), PageRequest.of(0, 100));
    //목록 조회에서 삭제됐는지
    assertThat(list.getContents().stream().filter(v -> v.getVideoId() == videoId).findAny())
        .isEmpty();
    //단건 조회가 안되는지
    assertThatThrownBy(() -> videoService.getVideoById(new UserInfo(null, null), videoId))
        .isInstanceOf(VideoNotFoundException.class);
  }
}
