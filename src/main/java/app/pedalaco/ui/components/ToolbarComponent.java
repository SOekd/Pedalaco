package app.pedalaco.ui.components;

import app.pedalaco.ui.views.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteParameters;

public class ToolbarComponent extends AppLayout {

    // Variavel criada apenas para testes
    // lembrar de deletar apos completar a integração com o banco de dados

    private static final String HEADER_ICON_CLASS_NAME = "header-icon";
    private static final String DRAWER_ICON_CLASS_NAME = "drawer-icon";


    private final Span debugText = new Span();


    public ToolbarComponent(){
        createHeader();
        createDrawer();
    }

    private void createHeader(){
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), new H1("Pedalaço"));
        addToNavbar(header,debugText);
        Icon homeIcon = VaadinIcon.HOME.create();
        Icon chatIcon = VaadinIcon.CHAT.create();
        Icon notificationIcon = VaadinIcon.BELL_O.create();
        Icon postIcon = VaadinIcon.PLUS.create();

        homeIcon.addClassName(HEADER_ICON_CLASS_NAME);
        chatIcon.addClassName(HEADER_ICON_CLASS_NAME);
        notificationIcon.addClassName(HEADER_ICON_CLASS_NAME);
        postIcon.addClassName(HEADER_ICON_CLASS_NAME);


        Button homeButton = new Button(homeIcon, event -> getUI().ifPresent(ui -> ui.navigate(ExploreView.class)));

        Button chatButton = new Button(chatIcon, event -> getUI().ifPresent(ui -> ui.navigate(ChatsMenuView.class)));

        Button notificationButton = new Button(notificationIcon, event -> getUI().ifPresent(ui -> ui.navigate(NotificationView.class)));

        Button postButton = new Button(postIcon, event -> getUI().ifPresent(ui -> ui.navigate(PostView.class)));


        homeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        chatButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        notificationButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        postButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        postButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        homeButton.addClassName("drawer-button");
        chatButton.addClassName("drawer-button");
        notificationButton.addClassName("drawer-button");
        postButton.addClassName("drawer-button");

        HorizontalLayout navbar = new HorizontalLayout(homeButton, postButton, chatButton, notificationButton);
        navbar.addClassName("navbar");
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        navbar.setAlignItems(FlexComponent.Alignment.CENTER);
        addToNavbar(true,navbar);
    }

    private void createDrawer() {
        Image profilePicture = new Image();
        profilePicture.setSrc("https://i.imgur.com/xxbEkOM.png");
        profilePicture.setAlt("Imagem de usúario anônima");

        Icon profileIcon = VaadinIcon.USER.create();
        profileIcon.addClassName(HEADER_ICON_CLASS_NAME);

        Button profileButton = new Button(profileIcon, event -> getUI().ifPresent(ui -> ui.navigate(ProfileView.class,new RouteParameters("username","Carlos"))));
        profileButton.addClassName("drawer-button");


        H2 username = new H2("Crimenelson");
        H2 level = new H2("1");

        FlexLayout profileLayout = new FlexLayout(profileButton,username,level);

        Button group1 = new Button("Grupo 1");
        group1.setPrefixComponent(VaadinIcon.GROUP.create());
        Button group2 = new Button("Grupo 2");
        group2.setPrefixComponent(VaadinIcon.GROUP.create());
        Button group3 = new Button("Grupo 3");
        group3.setPrefixComponent(VaadinIcon.GROUP.create());
        Button group4 = new Button("Grupo 4");
        group4.setPrefixComponent(VaadinIcon.GROUP.create());
        Button group5 = new Button("Grupo 5");
        group5.setPrefixComponent(VaadinIcon.GROUP.create());



        HorizontalLayout groupLayout =  new HorizontalLayout(group1,group2,group3,group4,group5);
        groupLayout.addClassName("drawer-group-container");

        profileButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        VerticalLayout drawer = new VerticalLayout(profileLayout,groupLayout);
        addToDrawer(drawer);
    }
}
