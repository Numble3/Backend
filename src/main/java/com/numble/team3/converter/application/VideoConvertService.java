package com.numble.team3.converter.application;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.numble.team3.converter.application.response.GetConvertVideoDto;
import com.numble.team3.converter.domain.ConvertResult;
import com.numble.team3.converter.domain.ConvertVideoUtils;
import com.numble.team3.exception.convert.VideoTypeUnSupportException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoConvertService {
  @Value("${cloud.aws.video.s3.name}")
  private String bucket;

  @Value("${ffmpeg.convertPath}")
  private String convertPath;

  private final ConvertVideoUtils convertVideoUtils;
  private final AmazonS3Client amazonS3Client;
  private final List<String> VIDEO_TYPES = List.of("mp4", "avi", "wmv", "mpg", "mpeg", "webm");

  private String createVideoDirFullPath() {
    return convertPath
        + UUID.randomUUID().toString().substring(0, 10);
  }

  private void validationFileType(String filePath) {
    String ext = convertVideoUtils.getFileExt(filePath);
    if (!VIDEO_TYPES.contains(ext)) {
      throw new VideoTypeUnSupportException();
    }
  }

  private String uploadFilesToS3(String dirFullPath) {
    File dirFile = new File(dirFullPath);
    String indexFileUploadUrl = null;
    String baseKey = UUID.randomUUID().toString() + "/";
    for (File file : dirFile.listFiles()) {
      String s3UploadKey =
          baseKey
              + convertVideoUtils.getFileOriginName(file.getAbsolutePath())
              + "."
              + convertVideoUtils.getFileExt(file.getAbsolutePath());
      log.info("[s3Upload] upload url: {}", s3UploadKey);
      if (convertVideoUtils.getFileExt(file.getAbsolutePath()).equals("m3u8")) {
        amazonS3Client.putObject(
            new PutObjectRequest(bucket, s3UploadKey, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        indexFileUploadUrl = amazonS3Client.getUrl(bucket, s3UploadKey).toString();
        log.info("[s3Upload] index file upload url: {}", indexFileUploadUrl);
      } else {
        amazonS3Client.putObject(
            new PutObjectRequest(bucket, s3UploadKey, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
      }
    }
    return indexFileUploadUrl;
  }

  private void cleanUpDirectory(String dirFullPath){
    File dirFile = new File(dirFullPath);
    for(File file: dirFile.listFiles()){
      file.delete();
    }
    dirFile.delete();
  }

  public GetConvertVideoDto uploadConvertVideo(MultipartFile videoFile) throws IOException {
    validationFileType(videoFile.getOriginalFilename());
    String dirFullPath = createVideoDirFullPath();
    String fileFullPath = convertVideoUtils.saveTempVideoForConvert(dirFullPath, videoFile);
    ConvertResult convertResult = convertVideoUtils.processConvertVideo(dirFullPath, fileFullPath);
    String indexFileUploadUrl = uploadFilesToS3(convertResult.getUploadDir());
    cleanUpDirectory(convertResult.getUploadDir());
    cleanUpDirectory(dirFullPath);
    return new GetConvertVideoDto(indexFileUploadUrl, convertResult.getVideoDuration());
  }
}
