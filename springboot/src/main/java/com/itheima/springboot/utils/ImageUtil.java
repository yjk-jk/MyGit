package com.itheima.springboot.utils;

import org.springframework.http.MediaType;

public class ImageUtil {

    private static String getExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            return ""; // 如果没有找到扩展名或者文件名以点号结束，则返回空字符串
        }
        return filename.substring(dotIndex + 1).toLowerCase(); // 从dotIndex + 1开始，以确保不包含点号，并转换为小写
    }

    public static MediaType getMediaType(String filename) {
        String extension = getExtension(filename);
        switch (extension) {
            case "jpg":
                return MediaType.IMAGE_JPEG;
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            // 添加其他图片格式的处理
            default:
                return MediaType.IMAGE_JPEG; // 默认媒体类型
        }
    }

}
