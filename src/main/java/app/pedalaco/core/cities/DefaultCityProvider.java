package app.pedalaco.core.cities;

import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DefaultCityProvider implements CityProvider {

    private static final String CITIES_AND_STATES_URL = "https://gist.githubusercontent.com/letanure/3012978/raw/6938daa8ba69bcafa89a8c719690225641e39586/estados-cidades.json";

    private final Map<String, List<String>> statesByCity = new HashMap<>();

    @Override
    public Map<String, List<String>> getCitiesAndStates() {
        if (!statesByCity.isEmpty())
            return statesByCity;

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(CITIES_AND_STATES_URL, String.class);
        if (jsonResponse == null)
            throw new RuntimeException("Could not get cities and states from " + CITIES_AND_STATES_URL);

        JsonParser.parseString(jsonResponse).getAsJsonObject().get("estados").getAsJsonArray().forEach(state -> {
            String stateName = state.getAsJsonObject().get("nome").getAsString();
            state.getAsJsonObject().get("cidades").getAsJsonArray().forEach(city -> {
                String cityName = city.getAsString();
                statesByCity.computeIfAbsent(stateName, k -> new ArrayList<>());
                statesByCity.computeIfPresent(stateName, (key, cities) -> {
                    cities.add(cityName);
                    return cities;
                });
            });
        });
        return statesByCity;
    }


}
