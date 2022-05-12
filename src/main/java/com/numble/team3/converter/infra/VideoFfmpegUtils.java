package com.numble.team3.converter.infra;

import com.numble.team3.converter.domain.ConvertResult;
import com.numble.team3.converter.domain.ConvertVideoUtils;
import com.numble.team3.exception.convert.VideoConvertFailureException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
@RequiredArgsConstructor
public class VideoFfmpegUtils implements ConvertVideoUtils {

  @Value("${ffmpeg.ffmpegPath}")
  private String ffmpegPath;

  @Value("${ffmpeg.ffprobePath}")
  private String ffprobePath;

  private FFmpeg ffmpeg;
  private FFprobe ffprobe;

  @Value("${ffmpeg.chunkUnit}")
  private long VIDEO_CHUNK_UNIT;

  private final String CONVERT_DIRECTORY_NAME = File.separator + "convert" + File.separator;

  @PostConstruct
  public void init() {
    try {
      ffmpeg = new FFmpeg(ffmpegPath);
      ffprobe = new FFprobe(ffprobePath);
    } catch (Exception e) {
      log.error("[ffmpeg, ffprobe Error] log: {}", String.valueOf(e));
    }
    log.info("ffmpegPath: {}", ffmpegPath);
    log.info("ffprobePath: {}", ffprobePath);
  }

  @Override
  public String getFileOriginName(String filePath) {
    return filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf("."));
  }

  @Override
  public String getFileExt(String filePath) {
    return filePath.substring(filePath.lastIndexOf(".") + 1);
  }

  private String convertVideo(String dirFullPath, String fileFullPath) {
    String outputFullPath = dirFullPath + getFileOriginName(fileFullPath) + ".m3u8";
    makeEncodingDirectory(dirFullPath);
    log.info("[convert start] output path: {}", outputFullPath);
    FFmpegBuilder builder =
        new FFmpegBuilder()
            .setInput(fileFullPath)
            .overrideOutputFiles(true)
            .addOutput(outputFullPath)
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
    log.info("[convert completed] output path: {}", outputFullPath);
    return outputFullPath;
  }

  // 업로드할 파일의 폴더 경로를 재귀적으로 생성
  private String makeEncodingDirectory(String dirFullPath) {
    log.info("[create directory recursive]: {}", dirFullPath);
    File file = new File(dirFullPath);
    if (!file.exists()) {
      file.mkdirs();
    }
    return dirFullPath;
  }

  // 로컬에 파일 업로드 하기
  private Optional<File> convertMultipartToFile(String dirFullPath, MultipartFile multipartFile)
      throws IOException {
    makeEncodingDirectory(dirFullPath);
    File convertFile = new File(dirFullPath + File.separator + multipartFile.getOriginalFilename());
    if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
      try (FileOutputStream fos =
          new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
        fos.write(multipartFile.getBytes());
      }
      log.info("[file convert complete] convert file name{}", convertFile.getPath());
      return Optional.of(convertFile);
    }
    log.info("[file convert failed] request filename: {}", multipartFile.getOriginalFilename());
    return Optional.empty();
  }

  public long extractVideoDuration(String filePath) throws IOException {
    log.info("[extract start video metadata] path: {}", filePath);
    FFmpegProbeResult probeResult = ffprobe.probe(filePath);
    log.info(
        "[extract start video metadata] duration: {}", probeResult.getStreams().get(0).duration_ts);
    return probeResult.getStreams().get(0).duration_ts;
  }

  @Override
  public String saveTempVideoForConvert(String dirName, MultipartFile videoFile) throws IOException {
    return convertMultipartToFile(dirName, videoFile)
        .orElseThrow(VideoConvertFailureException::new)
        .getAbsolutePath();
  }

  @Override
  public ConvertResult processConvertVideo(String dirPath, String filePath) throws IOException {
    long videoDuration = extractVideoDuration(filePath);
    String uploadDir = dirPath + CONVERT_DIRECTORY_NAME;
    String uploadFilePath = convertVideo(uploadDir, filePath);
    return new ConvertResult(videoDuration, uploadDir, uploadFilePath);
  }
}
