package app.pedalaco.ui.components;

import app.pedalaco.core.security.AuthenticatedUser;
import app.pedalaco.core.user.User;
import app.pedalaco.ui.MainView;
import app.pedalaco.ui.views.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteParameters;

@PageTitle("layout")
public class ToolbarComponent extends AppLayout {
    private static final String NAVBAR_ICON_CLASS_NAME = "header-icon";
    private static final String NAVBAR_BUTTON_CLASS_NAME = "navbar-button";
    private final Span debugText = new Span();
    private final AuthenticatedUser authenticatedUser;


    public ToolbarComponent(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        createHeader();
        createDrawer();

    }

    private void createHeader() {
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), new H1("Pedalaço"));
        addToNavbar(header, debugText);

        Button homeButton = createNavbarButton(VaadinIcon.HOME.create(), ButtonVariant.LUMO_TERTIARY, MainView.class);
        Button currentPedal = createNavbarButton(VaadinIcon.GROUP.create(), ButtonVariant.LUMO_TERTIARY, CurrentPedalView.class);
        Button chatButton = createNavbarButton(VaadinIcon.CHAT.create(), ButtonVariant.LUMO_TERTIARY, ChatsMenuView.class);
        Button notificationButton = createNavbarButton(VaadinIcon.BELL_O.create(), ButtonVariant.LUMO_TERTIARY, NotificationView.class);
        Button postButton = createNavbarButton(VaadinIcon.PLUS.create(), ButtonVariant.LUMO_PRIMARY, PostView.class);

        HorizontalLayout navbar = new HorizontalLayout(homeButton, currentPedal, postButton, chatButton, notificationButton);
        addToNavbar(true, navbar);
        navbar.addClassName("navbar");
    }

    private void createDrawer() {


        var user = authenticatedUser.get().orElse(null);
        if (user == null)
            return;


        Avatar profilePicture = new Avatar(user.getName());


        profilePicture.addClassName("drawer-profile-userphoto-image");
        Button profileButton = new Button(profilePicture, event -> getUI().ifPresent(ui -> ui.navigate(ProfileView.class, new RouteParameters("username", user.getUsername()))));
        profileButton.addClassName("drawer-profile-userphoto");

        H2 username = new H2(user.getUsername());
        username.addClassName("drawer-profile-username");

        H2 userLevel = new H2(String.valueOf(user.getLevel()));
        userLevel.addClassName("drawer-profile-userlevel");

        Image banner = new Image();
        banner.addClassName("drawer-profile-banner");
        FlexLayout profileLayout = new FlexLayout(banner, profileButton, username, userLevel);
        profileLayout.addClassName("drawer-profile");

        H3 groupLabel = new H3("Groups:");
        groupLabel.addClassName("drawer-group-label");

        Icon configurationIcon = VaadinIcon.COG.create();
        Icon logoutIcon = VaadinIcon.EXIT.create();

        Button configurationButton = new Button("Configurations", event -> getUI().ifPresent(ui -> ui.navigate(ConfigurationView.class)));
        configurationButton.setPrefixComponent(configurationIcon);

        Button logoutButton = new Button("Log out");
        logoutButton.setPrefixComponent(logoutIcon);

        configurationButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        logoutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        configurationButton.addClassName("drawer-configuration-button");
        logoutButton.addClassName("drawer-logout-button");

        logoutButton.addClickListener(event -> {
//            o correto é usar o auth user aqui, mas ainda é preciso pensar em como instanciar
//            getUI().ifPresent(ui -> ui.navigate(LoginForm.class));
        });

        VerticalLayout topLayout = new VerticalLayout(profileLayout);
        topLayout.addClassName("drawer-top-layout");
        VerticalLayout bottomLayout = new VerticalLayout(configurationButton, logoutButton);
        bottomLayout.addClassName("drawer-bottom-layout");
        VerticalLayout drawer = new VerticalLayout(topLayout, bottomLayout);
        drawer.addClassName("drawer");
        addToDrawer(drawer);
    }

    private Button createGroupButton(String name) {
        Icon groupIcon = VaadinIcon.GROUP.create();
        groupIcon.addClassName("drawer-icon");
        Button groupButton = new Button(name);
        groupButton.setPrefixComponent(groupIcon);
        groupButton.addClassName("drawer-group-button");
        return groupButton;
    }

    private Button createNavbarButton(Icon icon, ButtonVariant buttonVariant, Class<? extends Component> destination) {
        icon.addClassName(NAVBAR_ICON_CLASS_NAME);
        Button navbarButton = new Button(icon);
        navbarButton.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate(destination)));
        navbarButton.addThemeVariants(buttonVariant);
        navbarButton.addClassName(NAVBAR_BUTTON_CLASS_NAME);
        return navbarButton;
    }

}
