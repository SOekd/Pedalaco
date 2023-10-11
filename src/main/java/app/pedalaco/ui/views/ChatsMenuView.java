package app.pedalaco.ui.views;

import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value ="chats", layout = ToolbarComponent.class)
@AnonymousAllowed
public class ChatsMenuView extends VerticalLayout {
    public ChatsMenuView() {
        add(new Span("chats View"));
    }


}
