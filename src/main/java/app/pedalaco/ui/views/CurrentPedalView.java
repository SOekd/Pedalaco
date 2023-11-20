package app.pedalaco.ui.views;

import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "current-pedal", layout = ToolbarComponent.class)
@PermitAll
public class CurrentPedalView extends VerticalLayout {



}
