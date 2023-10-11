package app.pedalaco.ui.views;

import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value ="explore", layout = ToolbarComponent.class)
@AnonymousAllowed
public class ExploreView extends VerticalLayout {

    public ExploreView(){
        add(new H1("Explore view"));
    }



}
