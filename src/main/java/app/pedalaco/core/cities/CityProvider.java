package app.pedalaco.core.cities;

import java.util.List;
import java.util.Map;

public interface CityProvider {

    Map<String, List<String>> getCitiesAndStates();

}
