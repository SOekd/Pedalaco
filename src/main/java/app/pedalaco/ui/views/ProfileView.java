package app.pedalaco.ui.views;


import app.pedalaco.entities.UserEntity;
import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "user/:username", layout = ToolbarComponent.class)
@AnonymousAllowed
public class ProfileView extends VerticalLayout implements BeforeEnterObserver{
    private String username;

    private UserEntity user;

    public ProfileView(){
        user = UserEntity.builder()
                .id(1)
                .username("Elma Maria")
                .firstName("Elma")
                .lastName("Maria")
                .email("ElmaMaria@gmail.com")
                .password("Jacinto Pinto")
                .biography("Empresa de salgadinho 22")
                .experiencePoints(0)
                .build();

        H2 username = new H2(user.getUsername());
        username.addClassName("profile-username");
        Span experiencePoints = new Span(String.valueOf(user.getExperiencePoints()));
        experiencePoints.addClassName("profile-experiencepoints");
        Image profilePicture = new Image();
        FlexLayout banner = new FlexLayout(username,experiencePoints,profilePicture);

        Span biography = new Span(user.getBiography());
        HorizontalLayout biographyLayout = new HorizontalLayout(biography);
        biographyLayout.addClassName("profile-biography");
        
        VerticalLayout posts = new VerticalLayout(new Span("essa conta ainda não postou nada"));
        posts.addClassName("profile-posts");

        VerticalLayout body = new VerticalLayout(banner,biographyLayout,posts);
        add(body);
    }

    public void render(){

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        event.getRouteParameters().get("username").ifPresentOrElse(username -> this.username = username,
                () -> this.username = "jose");
        Span text = new Span(username);
        add(text);
    }
}
