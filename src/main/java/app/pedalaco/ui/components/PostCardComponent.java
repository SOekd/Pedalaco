package app.pedalaco.ui.components;

import app.pedalaco.core.pedal.Pedal;
import app.pedalaco.core.user.User;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class PostCardComponent extends VerticalLayout {

    private final User user;

    private final Pedal pedal;

    public PostCardComponent(User user, Pedal pedal) {
        this.user = user;
        this.pedal = pedal;

        render();
    }

    private void render() {

        // border
        getStyle().set("border", "1px solid #ccc");
        getStyle().set("border-radius", "5px");
        // shadow
        getStyle().set("box-shadow", "0 2px 4px 0 rgba(0,0,0,.2)");

        add(new H3(pedal.getTitle()));
    }


}
