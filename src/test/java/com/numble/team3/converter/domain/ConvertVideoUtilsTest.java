package com.numble.team3.converter.domain;

import static com.numble.team3.factory.VideoFfmpegUtilsFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("ConvertVideoUtils 테스트")
public class ConvertVideoUtilsTest {
  @Mock FFmpeg ffmpeg;

  @Mock FFprobe ffprobe;

  ConvertVideoUtils convertVideoUtils;

  @BeforeEach
  void beforeEach() {
    convertVideoUtils = createVideoFfmpegUtils(ffmpeg, ffprobe);
  }

  @ParameterizedTest
  @ValueSource(strings = {"/var/lib/test.txt", "D:\\Yun-Workspace\\Numble3-Backend\\test.txt"})
  void getFileExt_파일확장자_얻기_테스트(String path) {
    String ext = convertVideoUtils.getFileExt(path);
    assertEquals(ext, "txt");
  }

  @Test
  void getFileOriginFileName_파일이름_얻기_테스트() {
    String path = null;
    if (File.separator.equals("/")) {
      path = "/var/lib/test.txt";
    } else {
      path = "D:\\Yun-Workspace\\Numble3-Backend\\test.txt";
    }
    String fileName = convertVideoUtils.getFileOriginName(path);
    assertEquals(fileName, "test");
  }

  @ParameterizedTest
  @ValueSource(strings = {"convert", "dir"})
  void saveTempVideoForConvert_파일저장_테스트(String dirName) throws IOException {
    MockMultipartFile file =
        new MockMultipartFile("videoFile", "cat.mp4", "video/mp4", new byte[] {1});
    String convertFileFullPath =
        convertVideoUtils.saveTempVideoForConvert(
            System.getProperty("user.dir") + File.separator + dirName, file);
    File convertFile = new File(convertFileFullPath);
    assertEquals(convertFileFullPath.contains(dirName), true);
    assertEquals(convertFile.exists(), true);
    convertFile.delete();
    new File(System.getProperty("user.dir") + File.separator + dirName).delete();
  }
}
