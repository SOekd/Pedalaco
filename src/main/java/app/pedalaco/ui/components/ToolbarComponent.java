package app.pedalaco.ui.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class ToolbarComponent extends AppLayout {


    public ToolbarComponent(){
        createHeader();
        createDrawer();
        createFooter();
    }

    private void createFooter() {
        Icon homeIcon = new Icon(VaadinIcon.PHONE);
        Icon exploreIcon = new Icon(VaadinIcon.PHONE);
        Icon postIcon = new Icon(VaadinIcon.PHONE);
        Icon chatIcon = new Icon(VaadinIcon.PHONE);
        Icon notificationIcon = new Icon(VaadinIcon.PHONE);

        Button homeButton = new Button(homeIcon);
        Button exploreButton = new Button(exploreIcon);
        Button postButton = new Button(postIcon);
        Button chatButton = new Button(chatIcon);
        Button notificationButton = new Button(notificationIcon);

        var footer = new HorizontalLayout(homeButton,exploreButton,postButton,chatButton,notificationButton);
        footer.addClassName("Footer-Navbar");
        footer.setSizeFull();
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        addToNavbar(true,footer);

    }


    private void createHeader(){
        H1 appName = new H1("Pedalaço");
        appName.addClassName("App-name");

        var header = new HorizontalLayout(new DrawerToggle(),appName);

        addToNavbar(header);
    }

    private void createDrawer() {
        Icon profileIcon = new Icon(VaadinIcon.USER);
        Icon groupIcon = new Icon(VaadinIcon.GROUP);
        H1 username = new H1("Unknown");

        Button profileButton = new Button(profileIcon);

        var drawer = new VerticalLayout(profileButton,username);
        drawer.setAlignItems(FlexComponent.Alignment.CENTER);

        // O Script a seguir é apenas para testes!
        // Lembrar de alterar para uma Query por todos os grupos que o usuario pertence.

        for(int i = 0; i < 5;i++){
            Button groupButton = new Button(new HorizontalLayout(groupIcon,new TextField("Grupo " + i)));
            groupButton.addClassName("group-button");
            drawer.add(groupButton);
        }
        addToDrawer(drawer);
    }

}
