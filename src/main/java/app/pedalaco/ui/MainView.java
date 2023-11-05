package app.pedalaco.ui;

import app.pedalaco.core.maps.MapsCredential;
import app.pedalaco.core.pedal.Pedal;
import app.pedalaco.core.pedal.PedalService;
import app.pedalaco.core.security.AuthenticatedUser;
import app.pedalaco.ui.components.PostCardComponent;
import app.pedalaco.ui.components.ToolbarComponent;
import app.pedalaco.ui.login.LoginView;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.vaadin.firitin.geolocation.Geolocation;
import org.vaadin.firitin.geolocation.GeolocationOptions;

import java.util.UUID;
import java.util.WeakHashMap;

/**
 * The main view contains a button and a click listener.
 */
@Route(value = "home", layout = ToolbarComponent.class)
@RouteAlias(value = "", layout = ToolbarComponent.class)
@PermitAll
public class MainView extends VirtualList<Pedal> implements BeforeEnterObserver {


    private final AuthenticatedUser authenticatedUser;


    public MainView(AuthenticatedUser authenticatedUser, PedalService pedalService, MapsCredential mapsCredential) {
        this.authenticatedUser = authenticatedUser;

        WeakHashMap<UUID, PostCardComponent> observableMap = new WeakHashMap<>();

        val renderer = new ComponentRenderer<PostCardComponent, Pedal>(pedal -> {
            val postCardComponent = new PostCardComponent(authenticatedUser.get().orElseThrow(), pedalService, pedal, mapsCredential.getApiKey(), () -> {
                System.out.println("Render Callback");
                getDataProvider().refreshAll();
            });
            observableMap.put(pedal.getId(), postCardComponent);
            return postCardComponent;
        });
        setRenderer(renderer);

        Geolocation.watchPosition(
                positionEvent -> {
                    System.out.println("Latitude: " + positionEvent.getCoords().getLatitude());
                    System.out.println("Longitude: " + positionEvent.getCoords().getLongitude());
                    System.out.println("MapSize: " + observableMap.size());
                    observableMap.values().stream().parallel().forEach(it -> it.updateMarker(positionEvent.getCoords().getLatitude(), positionEvent.getCoords().getLongitude()));
                },
                browserError -> {
                    System.out.println("Error: " + browserError);
                },
                new GeolocationOptions(true, 2000, 7000)
        );


        CallbackDataProvider<Pedal, Void> dataProvider = DataProvider.fromCallbacks(
                query -> pedalService.listValid(PageRequest.of(query.getPage(),
                        query.getPageSize(), Sort.by("date"))).stream(),
                query -> Math.toIntExact(pedalService.countValid()));

        setHeightFull();

        this.setDataProvider(dataProvider);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Geolocation.getCurrentPosition(
                positionEvent -> {
                },
                browserError -> getUI().ifPresent(ui -> {
                   if ("Timeout expired".equals(browserError))
                       return;
                    authenticatedUser.logout();
                    ui.navigate(LoginView.class);

                    Notification notification = Notification.show("Não foi possível obter sua localização!\nVerifique se você permitiu o acesso a sua localização!\n\nQuem não permitir a localização não poderá utilizar essa plataforma!", 3000, Notification.Position.MIDDLE);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();

                }),
                new GeolocationOptions(true, 2000, 7000)
        );

    }
}
