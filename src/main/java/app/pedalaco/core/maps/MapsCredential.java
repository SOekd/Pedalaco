package app.pedalaco.core.maps;

import lombok.Data;

@Data(staticConstructor = "of")
public class MapsCredential {

    private final String apiKey;

    @Override
    public String toString() {
        return apiKey;
    }

}
