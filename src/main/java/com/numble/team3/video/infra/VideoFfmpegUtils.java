package com.numble.team3.video.infra;

import com.numble.team3.video.domain.VideoUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class VideoFfmpegUtils implements VideoUtils {

  private final String ffmpegPath = System.getProperty("user.dir") + File.separator + "ffmpeg.exe";

  private final String ffprobePath =
      System.getProperty("user.dir") + File.separator + "ffprobe.exe";

  private FFmpeg ffmpeg;
  private FFprobe ffprobe;

  @PostConstruct
  public void init() {
    try {
      ffmpeg = new FFmpeg(ffmpegPath);
      ffprobe = new FFprobe(ffprobePath);
    } catch (Exception e) {
      log.error(String.valueOf(e));
    }
    log.info("ffmpegPath: {}", ffmpegPath);
    log.info("ffprobePath: {}", ffprobePath);
  }

  @Override
  public String convertVideo(MultipartFile videoFile) throws IOException {

    String encodedDir = makeEncodingDirectory(videoFile);
    convertMultipartToFile(encodedDir, videoFile).orElseThrow(IllegalArgumentException::new);

    String fileName =
        videoFile
            .getOriginalFilename()
            .substring(0, videoFile.getOriginalFilename().lastIndexOf("."));

    FFmpegBuilder builder =
        new FFmpegBuilder()
            .setInput(encodedDir + File.separator + videoFile.getOriginalFilename())
            .overrideOutputFiles(true)
            .addOutput(encodedDir + File.separator + fileName + ".m3u8")
            .setVideoCodec("libx264")
            .setAudioCodec("aac")
            .setFormat("hls")
            .addExtraArgs("-start_number", "0") // .st 파일(스트리밍) 시작 번호
            .addExtraArgs("-hls_time", String.valueOf(VIDEO_CHUNK_UNIT)) // .st 파일 분리 단위
            .addExtraArgs("-hls_list_size", "0")
            .addExtraArgs("-force_key_frames", "expr:gte(t,n_forced*1)") // 시간으로 분리 가능하도록 강제
            .disableSubtitle() // No subtiles
            .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
            .done();

    FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
    // Run a one-pass encode
    executor.createJob(builder).run();
    return encodedDir;
  }

  // 업로드할 파일의 폴더 경로를 재귀적으로 생성
  private String makeEncodingDirectory(MultipartFile videoFile) {
    String encodedDir =
        System.getProperty("user.dir") + File.separator + UUID.randomUUID().toString();
    log.info("encode directory: {}", encodedDir);

    File file = new File(encodedDir);
    if (!file.exists()) {
      file.mkdirs();
    }
    return encodedDir;
  }

  // 로컬에 파일 업로드 하기
  private Optional<File> convertMultipartToFile(String path, MultipartFile multipartFile)
      throws IOException {
    File convertFile = new File(path + File.separator + multipartFile.getOriginalFilename());
    if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
      try (FileOutputStream fos =
          new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
        fos.write(multipartFile.getBytes());
      }
      return Optional.of(convertFile);
    }

    return Optional.empty();
  }

  @Override
  public long extractVideoDuration(String videoUrl) throws IOException {
    FFmpegProbeResult probeResult = ffprobe.probe(videoUrl);
    return probeResult.getStreams().get(0).duration_ts;
  }
}
