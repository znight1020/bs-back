package com.bob.global.utils.image;

import com.bob.global.exception.exceptions.ApplicationException;
import com.bob.global.exception.response.ApplicationError;
import java.util.UUID;

public class ImageUtils {

  public static String generateImageFileName(String contentType, ImageDirectory directory) {
    String extension = extractExtension(contentType);
    String uuid = UUID.randomUUID().toString();
    return directory.getPrefix() + uuid + "." + extension;
  }

  public static String extractExtension(String contentType) {
    return switch (contentType) {
      case "image/jpeg" -> "jpg";
      case "image/png" -> "png";
      case "image/gif" -> "gif";
      default -> throw new ApplicationException(ApplicationError.UN_SUPPORTED_TYPE);
    };
  }
}
