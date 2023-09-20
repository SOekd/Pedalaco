package app.pedalaco.ui.views;

import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value ="notifications", layout = ToolbarComponent.class)
@AnonymousAllowed
public class NotificationView extends VerticalLayout {

    public NotificationView(){
        add(new H1("NotificationView"));
    }
}
