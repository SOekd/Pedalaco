package app.pedalaco.ui.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ToolbarComponent extends AppLayout {


    public ToolbarComponent(){
        createHeader();
        createDrawer();
        createFooter();
    }

    private void createFooter() {
        Icon homeIcon = new Icon(VaadinIcon.PHONE);
        homeIcon.setColor("blue");
        Icon exploreIcon = new Icon(VaadinIcon.SEARCH);
        exploreIcon.setColor("blue");
        Icon postIcon = new Icon(VaadinIcon.PLUS);
        postIcon.setColor("blue");
        Icon chatIcon = new Icon(VaadinIcon.CHAT);
        chatIcon.setColor("blue");
        Icon notificationIcon = new Icon(VaadinIcon.BELL);
        notificationIcon.setColor("blue");

        Button homeButton = new Button(homeIcon);
        Button exploreButton = new Button(exploreIcon);
        Button postButton = new Button(postIcon);
        Button chatButton = new Button(chatIcon);
        Button notificationButton = new Button(notificationIcon);

        var footer = new HorizontalLayout(homeButton,exploreButton,postButton,chatButton,notificationButton);
        footer.addClassName("footer-navbar");
        footer.setSizeFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        addToNavbar(true,footer);

    }


    private void createHeader(){
        H3 appName = new H3("Pedalaço");
        appName.addClassName("app-name");

        var header = new HorizontalLayout(new DrawerToggle(),appName);

        addToNavbar(header);
    }

    private void createDrawer() {
        Icon profileIcon = new Icon(VaadinIcon.USER);
        H3 username = new H3("Unknown");

        Button profileButton = new Button(profileIcon);

        var drawer = new VerticalLayout(profileButton,username);
        drawer.setAlignItems(FlexComponent.Alignment.CENTER);

        // O Script a seguir é apenas para testes!
        // Lembrar de alterar para uma Query por todos os grupos que o usuario pertence.

        for(int i = 0; i < 5;i++){
            Icon groupIcon = new Icon(VaadinIcon.GROUP);
            Button groupButton = new Button(new HorizontalLayout(groupIcon,new H3("Grupo " + i)));
            groupButton.addClassName("group-button");
            drawer.add(groupButton);
        }
        addToDrawer(drawer);
    }

}
