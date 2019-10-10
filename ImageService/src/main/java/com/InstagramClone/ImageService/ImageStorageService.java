package com.InstagramClone.ImageService;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.UUID;

public class ImageStorageService {

    private final Path rootLocation = Paths.get("Images");
    
    private HashMap<UUID, Image> imageDatabase = new HashMap<UUID, Image>();

    String store(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new IOException("Failed to store empty file " + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
                UUID uniqueID = UUID.randomUUID();
                Image i = new Image(uniqueID, filename);
                imageDatabase.put(uniqueID, i);
                return uniqueID.toString();
            }
        }
        catch (IOException e) {
            throw new IOException("Failed to store file " + filename, e);
        }
    }

    String load(String id) {
    	return imageDatabase.get(UUID.fromString(id)).getContent();
    }
}