package app.pedalaco.ui.views;

import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value ="configuration", layout = ToolbarComponent.class)
@AnonymousAllowed
public class ConfigurationView extends VerticalLayout {
    public ConfigurationView() {
        add(new Span("Configuration View"));
    }
}
