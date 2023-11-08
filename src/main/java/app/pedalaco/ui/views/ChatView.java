package app.pedalaco.ui.views;

import app.pedalaco.core.privatemessage.PrivateChat;
import app.pedalaco.core.security.AuthenticatedUser;
import app.pedalaco.core.user.UserService;
import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Route(value = "chat/:target", layout = ToolbarComponent.class)
@PermitAll
public class ChatView extends VerticalLayout implements BeforeEnterListener {

    private final AuthenticatedUser authenticatedUser;

    private final UserService userService;

    public ChatView(AuthenticatedUser authenticatedUser, UserService userService) {
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
    }

    private void render() {
//        MessageList list = new MessageList();
//        MessageInput input = new MessageInput();
//        input.addSubmitListener(submitEvent -> {
//            MessageListItem newMessage = new MessageListItem(
//                    submitEvent.getValue(), Instant.now(), "Milla Sting");
//            newMessage.setUserColorIndex(3);
//            List<MessageListItem> items = new ArrayList<>(list.getItems());
//            items.add(newMessage);
//            list.setItems(items);
//        });
//
//        Person person = DataService.getPeople(1).get(0);
//        MessageListItem message1 = new MessageListItem(
//                "Nature does not hurry, yet everything gets accomplished.",
//                LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC),
//                "Matt Mambo");
//        message1.setUserColorIndex(1);
//        MessageListItem message2 = new MessageListItem(
//                "Using your talent, hobby or profession in a way that makes you contribute with something good to this world is truly the way to go.",
//                LocalDateTime.now().minusMinutes(55).toInstant(ZoneOffset.UTC),
//                "Linsey Listy", person.getPictureUrl());
//        message2.setUserColorIndex(2);
//        list.setItems(message1, message2);
//
//        VerticalLayout chatLayout = new VerticalLayout(list, input);
//        chatLayout.setHeight("500px");
//        chatLayout.setWidth("400px");
//        chatLayout.expand(list);
//        add(chatLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        val targetName = event.getRouteParameters().get("target").orElse(null);
        if (targetName == null) {
            getUI().ifPresent(ui -> ui.navigate(ChatsMenuView.class));
            return;
        }

        val target = userService.getByUsername(targetName).orElse(null);
        if (target == null)  {
            getUI().ifPresent(ui -> ui.navigate(ChatsMenuView.class));
            return;
        }

        val user = authenticatedUser.get().orElseThrow();


    }
}
