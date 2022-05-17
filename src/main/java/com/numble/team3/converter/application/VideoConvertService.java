package com.numble.team3.converter.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.numble.team3.converter.application.request.CreateVideoDto;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
import com.numble.team3.converter.domain.ConvertResult;
import com.numble.team3.converter.domain.ConvertVideoUtils;
import com.numble.team3.exception.convert.VideoTypeUnSupportException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoConvertService {
  @Value("${cloud.aws.video.s3.name}")
  private String BUCKET;

  @Value("${ffmpeg.convert.path}")
  private String BASE_CONVERT_DIR_PATH;

  @Value("${cloud.aws.cloud_front.domain_name}")
  private String CLOUD_FRONT_DOMAIN_NAME;

  private final ConvertVideoUtils convertVideoUtils;
  private final AmazonS3Client amazonS3Client;
  private final List<String> VIDEO_TYPES = List.of("mp4", "avi", "wmv", "mpg", "mpeg", "webm");

  private String createVideoDirFullPath() {
    return BASE_CONVERT_DIR_PATH + File.separator + UUID.randomUUID().toString().substring(0, 10);
  }

  private void validationFileType(String filePath) {
    String ext = convertVideoUtils.getFileExt(filePath);
    if (!VIDEO_TYPES.contains(ext)) {
      throw new VideoTypeUnSupportException();
    }
  }

  public String uploadS3UploadKeyGenerator() {
    return UUID.randomUUID().toString().substring(0, 10)
        + "_"
        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
  }

  private String uploadFilesToS3(String dirFullPath) {
    File dirFile = new File(dirFullPath);
    String indexFileUploadUrl = null;
    String baseKey = uploadS3UploadKeyGenerator();
    for (File file : dirFile.listFiles()) {
      String s3UploadKey =
          baseKey
              + File.separator
              + baseKey
              + "."
              + convertVideoUtils.getFileExt(file.getAbsolutePath());
      log.info("[s3Upload] upload url: {}", s3UploadKey);
      if (convertVideoUtils.getFileExt(file.getAbsolutePath()).equals("m3u8")) {
        amazonS3Client.putObject(
            new PutObjectRequest(BUCKET, s3UploadKey, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        indexFileUploadUrl = CLOUD_FRONT_DOMAIN_NAME + "/" + s3UploadKey;
        log.info("[s3Upload] index file upload url: {}", indexFileUploadUrl);
      } else {
        amazonS3Client.putObject(
            new PutObjectRequest(BUCKET, s3UploadKey, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
      }
    }
    return indexFileUploadUrl;
  }

  private void cleanUpDirectory(String dirFullPath) {
    File dirFile = new File(dirFullPath);
    for (File file : dirFile.listFiles()) {
      file.delete();
    }
    dirFile.delete();
  }

  public GetConvertVideoDto uploadConvertVideo(CreateVideoDto dto) throws IOException {
    validationFileType(dto.getVideoFile().getOriginalFilename());
    String dirFullPath = createVideoDirFullPath();
    String fileFullPath =
        convertVideoUtils.saveTempVideoForConvert(dirFullPath, dto.getVideoFile());
    ConvertResult convertResult = convertVideoUtils.processConvertVideo(dirFullPath, fileFullPath);
    String indexFileUploadUrl = uploadFilesToS3(convertResult.getUploadDir());
    cleanUpDirectory(convertResult.getUploadDir());
    cleanUpDirectory(dirFullPath);
    return new GetConvertVideoDto(indexFileUploadUrl, convertResult.getVideoDuration());
  }
}
