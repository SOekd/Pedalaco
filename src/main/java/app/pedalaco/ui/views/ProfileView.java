package app.pedalaco.ui.views;


import app.pedalaco.core.security.AuthenticatedUser;
import app.pedalaco.core.user.UserService;
import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.val;

@Route(value = "user/:username?", layout = ToolbarComponent.class)
@PageTitle("Perfil")
@PermitAll
public class ProfileView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    private final UserService userService;

    public ProfileView(AuthenticatedUser authenticatedUser, UserService userService) {
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
    }


    public void render(String usernameToRender) {

        val user = authenticatedUser.get().orElseThrow();

        H2 username = new H2(user.getUsername());
        username.addClassName("profile-username");
        Span experiencePoints = new Span(String.valueOf(user.getExperiencePoints()));
        experiencePoints.addClassName("profile-experiencepoints");
        Image profilePicture = new Image();
        FlexLayout banner = new FlexLayout(username, experiencePoints, profilePicture);

        Span biography = new Span(user.getBiography());
        HorizontalLayout biographyLayout = new HorizontalLayout(biography);
        biographyLayout.addClassName("profile-biography");

        VerticalLayout posts = new VerticalLayout(new Span("essa conta ainda não postou nada"));
        posts.addClassName("profile-posts");

        VerticalLayout body = new VerticalLayout(banner, biographyLayout, posts);
        add(body);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {


        event.getRouteParameters().get("username").ifPresentOrElse(
                username -> {

                },
                () -> {

                }
        );

//        render();
    }
}
