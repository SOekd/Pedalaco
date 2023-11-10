package app.pedalaco.ui.views;

import app.pedalaco.core.privatemessage.PrivateChat;
import app.pedalaco.core.security.AuthenticatedUser;
import app.pedalaco.core.user.User;
import app.pedalaco.core.user.UserService;
import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import lombok.val;

import java.io.ByteArrayInputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = "chats", layout = ToolbarComponent.class)
@PermitAll
public class ChatsMenuView extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;

    private final UserService userService;


    public ChatsMenuView(AuthenticatedUser authenticatedUser, UserService userService) {
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
        add(getPossibleChats());

        renderOpenChats();
    }

    private void renderOpenChats() {
        val user = authenticatedUser.get().orElseThrow();

//        List<PrivateChat> chats = Stream.concat(user.getReceivedChats().stream(), user.getAuthoredChats().stream())
//                .sorted(Comparator.comparing(cht -> cht.getLastMessage().getSentAt()))
//                .collect(Collectors.toList());
//
//        VirtualList<PrivateChat> virtualList = new VirtualList<>();
//
//        val renderer = new ComponentRenderer<Component, PrivateChat>(chat -> {
//
//            val chatLayout = new HorizontalLayout();
//
//            val avatar = new Avatar();
//
//            User userOnAvatar;
//            if (chat.getAuthor().equals(user)) {
//                userOnAvatar = chat.getReceiver();
//            } else {
//                userOnAvatar = chat.getAuthor();
//            }
//
//            avatar.setName(userOnAvatar.getUsername());
//            if (userOnAvatar.getProfilePicture() != null) {
//                avatar.setImageResource(new StreamResource(userOnAvatar.getName() + ".png", () -> new ByteArrayInputStream(userOnAvatar.getProfilePicture())));
//            }
//
//            return chatLayout;
//        });
//
//        virtualList.setItems(chats);
//        virtualList.setRenderer(renderer);

    }

    private ComboBox<User> getPossibleChats() {

        val currentUser = authenticatedUser.get().orElseThrow();

        ComboBox.ItemFilter<User> filter = (filteringUser, filterString) -> filteringUser
                .getName().toLowerCase().startsWith(filterString.toLowerCase()) && !filteringUser.equals(currentUser);

        ComboBox<User> possibleChats = new ComboBox<>();

        possibleChats.setWidth("100%");

        possibleChats.setItems(filter, userService.listAll());

        val renderer = new ComponentRenderer<Component, User>(user -> {

            val userLayout = new HorizontalLayout();

            val avatar = new Avatar();
            avatar.setName(user.getUsername());

            if (user.getProfilePicture() != null) {
                avatar.setImageResource(new StreamResource(user.getName() + ".png", () -> new ByteArrayInputStream(user.getProfilePicture())));
            }

            val span = new Span(user.getUsername() + " (" + user.getName() + ")");

            userLayout.setAlignItems(Alignment.CENTER);
            userLayout.setJustifyContentMode(JustifyContentMode.START);

            userLayout.add(avatar, span);
            return userLayout;
        });

        possibleChats.setItemLabelGenerator(User::getUsername);
        possibleChats.setRenderer(renderer);

        possibleChats.addValueChangeListener(listener -> {
            if (listener.getValue() == null)
                return;

            val selectedUser = listener.getValue();
            if (selectedUser.equals(currentUser))
                return;

            getUI().ifPresent(ui -> ui.navigate(ChatView.class, new RouteParam("target", selectedUser.getUsername())));
        });

        return possibleChats;
    }


}
