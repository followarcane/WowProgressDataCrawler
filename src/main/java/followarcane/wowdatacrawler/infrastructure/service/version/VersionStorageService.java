package followarcane.wowdatacrawler.infrastructure.service.version;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class VersionStorageService {

    @Value("${properties.app.version-file-path}")
    private String versionFilePath;

    public String getLastVersion() {
        try {
            Path versionFile = Paths.get(versionFilePath);
            if (Files.exists(versionFile)) {
                return Files.readString(versionFile).trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveCurrentVersion(String version) {
        try {
            Path versionFile = Paths.get(versionFilePath);
            Files.createDirectories(versionFile.getParent());
            Files.writeString(versionFile, version, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
