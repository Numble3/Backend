package com.numble.team3.image.application;

import com.numble.team3.image.application.request.CreateImageDto;
import java.io.IOException;

public interface ImageService {

  String uploadResizeImage(CreateImageDto dto) throws IOException;
}
