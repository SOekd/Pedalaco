package app.pedalaco.ui;

import app.pedalaco.core.pedal.Pedal;
import app.pedalaco.core.pedal.PedalService;
import app.pedalaco.core.security.AuthenticatedUser;
import app.pedalaco.ui.components.PostCardComponent;
import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

/**
 * The main view contains a button and a click listener.
 */
@Route(value = "home", layout = ToolbarComponent.class)
@RouteAlias(value = "", layout = ToolbarComponent.class)
@PermitAll
public class MainView extends VirtualList<Pedal> {

    private final AuthenticatedUser authenticatedUser;

    private final PedalService pedalService;

    private ComponentRenderer<PostCardComponent, Pedal> renderer;

    public MainView(AuthenticatedUser authenticatedUser, PedalService pedalService) {
        this.authenticatedUser = authenticatedUser;
        this.pedalService = pedalService;

        this.renderer = new ComponentRenderer<>(pedal -> new PostCardComponent(authenticatedUser.get().orElseThrow(), pedal));


    }




}
