package website.salon.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(MultipartFile file);

    void delete(String url);

    /**
     * Turns a stored path (e.g. "/uploads/foo.jpg") into the URL clients should use to fetch it.
     * Absolute URLs (legacy rows stored before this resolution existed) are returned unchanged.
     */
    default String resolve(String storedPath) {
        return storedPath;
    }
}
