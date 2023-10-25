package app.pedalaco.core.maps;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;

@Component
public class MapsCredentialProvider {

    @SneakyThrows
    @Bean
    public MapsCredential getCredentials() {
        var apiFile = new File("apimaps.txt");
        var apiContent = Files.readString(apiFile.toPath());
        return MapsCredential.of(apiContent);
    }

}
