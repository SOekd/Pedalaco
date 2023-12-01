package app.pedalaco.ui.views;

import app.pedalaco.core.cities.DefaultCityProvider;
import app.pedalaco.core.privatemessage.PrivateMessagePersister;
import app.pedalaco.core.security.AuthenticatedUser;
import app.pedalaco.core.user.User;
import app.pedalaco.core.user.UserService;
import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.collaborationengine.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Route(value = "chats", layout = ToolbarComponent.class)
@PermitAll
public class ChatsMenuView extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;

    private final List<ChatInfo> chats;

    private ChatInfo currentChat;

    private CollaborationMessageList list;

    public ChatsMenuView(AuthenticatedUser authenticatedUser, UserService userService, DefaultCityProvider cityProvider, PrivateMessagePersister messagePersister) {
        this.authenticatedUser = authenticatedUser;

        List<String> allCities = new ArrayList<>();
        cityProvider.getCitiesAndStates().values().forEach(allCities::addAll);
        chats = allCities.stream().map(ChatInfo::new).toList();

        for (ChatInfo chat : chats) {
            MessageManager messageManager = new MessageManager(this, getUserInfo(authenticatedUser.get().orElseThrow()), chat.getCollaborationTopic(), messagePersister);
            messageManager.setMessageHandler(context -> {
                if (currentChat != chat) {
                    chat.incrementUnread();
                }
            });
        }

        currentChat = getChatInfo("Joinville");

        HorizontalLayout filterLayout = new HorizontalLayout(getPossibleChats());
        filterLayout.addClassName("chats-filter");
        add(filterLayout);

        this.addClassName("chats-body");
        this.setSpacing(false);

        renderChat();
    }

    private void renderChat() {
        val user = authenticatedUser.get().orElseThrow();
        val userInfo = getUserInfo(user);

        list = new CollaborationMessageList(userInfo, currentChat.getCollaborationTopic());
        list.setSizeFull();

        CollaborationMessageInput input = new CollaborationMessageInput(list);
        input.setWidthFull();
        input.addClassName("chats-input");

        VerticalLayout chatContainer = new VerticalLayout();
        chatContainer.addClassNames(LumoUtility.Flex.AUTO, LumoUtility.Overflow.HIDDEN);
        chatContainer.addClassName("chats-messages");


        CollaborationAvatarGroup avatarGroup = new CollaborationAvatarGroup(userInfo, currentChat.getCollaborationTopic());
        avatarGroup.setMaxItemsVisible(4);
        avatarGroup.addClassName("chats-group");
        HorizontalLayout groupContainer = new HorizontalLayout(avatarGroup);
        groupContainer.addClassName("chats-group-container");
        chatContainer.add(list, input);
        VerticalLayout verticalLayout = new VerticalLayout(groupContainer, chatContainer);
        verticalLayout.addClassName("chats-container");
        add(verticalLayout);
        setSizeFull();
        expand(list);
    }

    private UserInfo getUserInfo(User user) {
        return new UserInfo(user.getId().toString(), user.getName());
    }

    private ChatInfo getChatInfo(String name) {
        return chats.stream().filter(chat -> chat.getName().equals(name)).findFirst().orElseThrow();
    }

    private ComboBox<String> getPossibleChats() {

        ComboBox.ItemFilter<String> filter = (filteringUser, filterString) -> filteringUser.toLowerCase().startsWith(filterString.toLowerCase());

        ComboBox<String> possibleChats = new ComboBox<>();
        possibleChats.addClassName("chats-filter-content");

        val renderer = new ComponentRenderer<Component, String>(chat -> {
            val chatLayout = new HorizontalLayout();
            val chatInfo = getChatInfo(chat);
            chatLayout.add(new Span(chatInfo.getName()), chatInfo.unreadBadge);

            chatLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
            chatLayout.addClassName("chats-layout");

            return chatLayout;
        });

        possibleChats.setRenderer(renderer);


        possibleChats.setItems(filter, chats.stream().map(ChatInfo::getName).toList());
        possibleChats.setValue("Joinville");
        possibleChats.addValueChangeListener(listener -> {
            if (listener.getValue() == null)
                return;

            val selectedCity = listener.getValue();
            currentChat = getChatInfo(selectedCity);
            currentChat.resetUnread();
            list.setTopic(currentChat.getCollaborationTopic());

        });

        return possibleChats;
    }

    public static class ChatInfo {
        private String name;
        private int unread;
        private Span unreadBadge;

        private ChatInfo(String name, int unread) {
            this.name = name;
            this.unread = unread;

            unreadBadge = new Span(String.valueOf(unread));
            unreadBadge.getElement().getThemeList().add("badge pill small contrast");
            unreadBadge.getStyle().set("margin-inline-start", "var(--lumo-space-s)");
            String counterLabel = String.format("%d mensagens não lidas", unread);
            unreadBadge.getElement().setAttribute("aria-label", counterLabel);
            unreadBadge.getElement().setAttribute("title", counterLabel);
        }

        private ChatInfo(String name) {
            this(name, 0);
        }

        public void resetUnread() {
            unread = 0;
            updateBadge();
        }

        public void incrementUnread() {
            unread++;
            updateBadge();
        }

        private void updateBadge() {
            unreadBadge.setText(unread + "");
            unreadBadge.setVisible(unread != 0);
        }

        public void setUnreadBadge(Span unreadBadge) {
            this.unreadBadge = unreadBadge;
            updateBadge();
        }

        public String getName() {
            return name;
        }

        public String getCollaborationTopic() {
            return "chat/" + name;
        }
    }

}
