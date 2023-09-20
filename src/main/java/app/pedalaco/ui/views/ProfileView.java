package app.pedalaco.ui.views;


import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "user", layout = ToolbarComponent.class)
public class ProfileView extends VerticalLayout implements HasUrlParameter {

    H1 username = new H1("");
    public ProfileView(){
        add(username);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Object parameter) {
        if (parameter == null) {
            username.setText("Hello, Unknown");
        } else {
            username.setText("Hello, " + parameter);
        }
    }
}
