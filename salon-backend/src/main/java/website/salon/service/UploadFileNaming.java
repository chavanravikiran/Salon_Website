package website.salon.service;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;

final class UploadFileNaming {

    private static final Set<String> VIDEO_EXTENSIONS = Set.of("mp4", "webm", "ogg");
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "webp", "gif", "ico", "svg", "mp4", "webm", "ogg");

    private UploadFileNaming() {}

    static String generateFilename(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String extension = extractExtension(cleanName(file));
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("Unsupported file type: " + extension);
        }
        return UUID.randomUUID() + "." + extension;
    }

    static boolean isVideo(MultipartFile file) {
        String extension = extractExtension(cleanName(file));
        return VIDEO_EXTENSIONS.contains(extension.toLowerCase());
    }

    private static String cleanName(MultipartFile file) {
        return StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "");
    }

    private static String extractExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            throw new IllegalArgumentException("File has no extension: " + filename);
        }
        return filename.substring(dot + 1);
    }
}
