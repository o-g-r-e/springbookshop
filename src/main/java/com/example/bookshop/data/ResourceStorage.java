package com.example.bookshop.data;

import com.example.bookshop.data.entity.BookFile;
import com.example.bookshop.data.repository.BookFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class ResourceStorage {
    @Value("${upload.path}")
    String uploadPath;

    @Value("${download.path}")
    String downloadPath;

    private final BookFileRepository bookFileRepository;

    @Autowired
    public ResourceStorage(BookFileRepository bookFileRepository) {
        this.bookFileRepository = bookFileRepository;
    }
    public String saveNewBookImage(MultipartFile file, String slug) throws IOException {
        String resourceUri = null;
        if(!file.isEmpty()){
            if(!new File(uploadPath).exists()){
                Files.createDirectories(Paths.get(uploadPath));
            }

            String fileName = slug+"."+ FilenameUtils.getExtension(file.getOriginalFilename());
            Path path = Paths.get(uploadPath,fileName);
            resourceUri = "/book-covers/"+fileName;
            file.transferTo(path); // uploading user file here
        }

        return resourceUri;
    }

    public Path getBookFilePath(String hash) {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash);
        return Paths.get(bookFile.getPath());
    }

    public MediaType getBookFileMime(String hash) {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash);
        String mimeType = URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());

        if (mimeType == null) return MediaType.APPLICATION_OCTET_STREAM;

        return MediaType.parseMediaType(mimeType);
    }

    public byte[] getBookFileByteArray(String hash) throws IOException {
        BookFile bookFile = bookFileRepository.findBookFileByHash(hash);
        Path path = Paths.get(downloadPath, bookFile.getPath());
        createIfNotExists(path);
        return Files.readAllBytes(path);
    }

    private void createIfNotExists(Path path) throws IOException {
        File file = path.toFile();
        if(!file.exists()) {
            file.createNewFile();
        }
    }
}
