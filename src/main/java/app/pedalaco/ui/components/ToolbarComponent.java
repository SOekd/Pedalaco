package app.pedalaco.ui.components;

import app.pedalaco.entities.UserEntity;
import app.pedalaco.ui.views.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.router.internal.HasUrlParameterFormat;

import java.util.Map;

public class ToolbarComponent extends AppLayout {

    // Variavel criada apenas para testes
    // lembrar de deletar apos completar a integração com o banco de dados

    Span debugtext = new Span();


    public ToolbarComponent(){
        createHeader();
        createDrawer();
    }



    private void createHeader(){
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), new H1("Pedalaço"));
        addToNavbar(true,header,debugtext);
        Icon profileIcon = VaadinIcon.USER.create();
        Icon homeIcon = VaadinIcon.HOME.create();
        Icon chatIcon = VaadinIcon.CHAT.create();
        Icon notificationIcon = VaadinIcon.BELL_O.create();
        Icon postIcon = VaadinIcon.PLUS.create();

        profileIcon.addClassName("drawer-icon");
        homeIcon.addClassName("drawer-icon");
        chatIcon.addClassName("drawer-icon");
        notificationIcon.addClassName("drawer-icon");
        postIcon.addClassName("drawer-icon");

        Button profileButton = new Button(profileIcon, event -> {
            getUI().ifPresent(ui -> ui.navigate(ProfileView.class,new RouteParameters("username","Carlos")));
        });
        Button homeButton = new Button(homeIcon, event -> {
            getUI().ifPresent(ui -> ui.navigate(ExploreView.class));
        });
        Button chatButton = new Button(chatIcon, event -> {
            getUI().ifPresent(ui -> ui.navigate(ChatsMenuView.class));
        });
        Button notificationButton = new Button(notificationIcon, event -> {
            getUI().ifPresent(ui -> ui.navigate(NotificationView.class));
        });
        Button postButton = new Button(postIcon, event -> {
            getUI().ifPresent(ui -> ui.navigate(PostView.class));
        });

        profileButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        homeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        chatButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        notificationButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        postButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        postButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        profileButton.addClassName("drawer-button");
        homeButton.addClassName("drawer-button");
        chatButton.addClassName("drawer-button");
        notificationButton.addClassName("drawer-button");
        postButton.addClassName("drawer-button");

        HorizontalLayout navbar = new HorizontalLayout(profileButton, homeButton, chatButton, notificationButton, postButton);
        navbar.addClassName("navbar");
        addToNavbar(true,navbar);

    }

    private void createDrawer() {

    }
}
