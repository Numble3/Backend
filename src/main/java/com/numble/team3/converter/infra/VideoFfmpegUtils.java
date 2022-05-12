package com.numble.team3.converter.infra;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
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

  private String getFileOriginName(String filePath){
    return filePath.substring(0, filePath.lastIndexOf("."));
  }

  private void convertVideo(String dirName, String filePath) {
    String outputPath = dirName + File.separator + getFileOriginName(filePath) + File.separator + ".m3u8";
    log.info("[convert start] output path: {}", outputPath);
    FFmpegBuilder builder =
      new FFmpegBuilder()
        .setInput(filePath)
        .overrideOutputFiles(true)
        .addOutput(outputPath)
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
    log.info("[convert completed] output path: {}", outputPath);
  }

  // 업로드할 파일의 폴더 경로를 재귀적으로 생성
  private String makeEncodingDirectory(String encodedDir) {
    log.info("[create directory recursive]: {}", encodedDir);
    File file = new File(encodedDir);
    if (!file.exists()) {
      file.mkdirs();
    }
    return encodedDir;
  }

  // 로컬에 파일 업로드 하기
  private Optional<File> convertMultipartToFile(String path, MultipartFile multipartFile)
    throws IOException {
    makeEncodingDirectory(path);
    File convertFile = new File(path + File.separator + multipartFile.getOriginalFilename());
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
    log.info("[extract start video metadata] duration: {}", probeResult.getStreams().get(0).duration_ts);
    return probeResult.getStreams().get(0).duration_ts;
  }

  @Override
  public String saveTempVideoForConvert(String filename, CreateVideoDto dto) throws IOException {
    return convertMultipartToFile(filename, dto.getFile()).orElseThrow(
      VideoConvertFailureException::new).getAbsolutePath();
  }

  @Override
  public long processConvertVideo(String dirName, String filePath) throws IOException {
    long videoDuration = extractVideoDuration(filePath);
    convertVideo(dirName, filePath);
    return videoDuration;
  }
}
