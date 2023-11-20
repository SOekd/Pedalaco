package app.pedalaco.core.privatemessage;

import app.pedalaco.core.user.User;
import app.pedalaco.core.user.UserService;
import com.vaadin.collaborationengine.CollaborationMessage;
import com.vaadin.collaborationengine.CollaborationMessagePersister;
import com.vaadin.collaborationengine.MessageManager;
import com.vaadin.collaborationengine.UserInfo;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Stream;

@Component
public class PrivateMessagePersister implements CollaborationMessagePersister {

    private final UserService userService;

    private final PrivateMessageService privateMessageService;

    public PrivateMessagePersister(UserService userService, PrivateMessageService privateMessageService) {
        this.userService = userService;
        this.privateMessageService = privateMessageService;
    }

    @Override
    public Stream<CollaborationMessage> fetchMessages(FetchQuery query) {
        return privateMessageService
                .findAllByTopicSince(query.getTopicId(), query.getSince())
                .stream()
                .map(messageEntity -> {
                    User author = messageEntity.getAuthor();
                    UserInfo userInfo = new UserInfo(String.valueOf(author.getId()), author.getName());
                    return new CollaborationMessage(userInfo, messageEntity.getMessage(), messageEntity.getSentAt());
                });
    }

    @Override
    public void persistMessage(PersistRequest request) {
        val message = request.getMessage();

        val user = userService.get(UUID.fromString(message.getUser().getId())).orElseThrow(() -> new RuntimeException("Unknown user in chat message"));

        val privateMessage = PrivateMessage.builder()
                .author(user)
                .message(message.getText())
                .topic(request.getTopicId())
                .sentAt(message.getTime())
                .build();

        privateMessageService.save(privateMessage);
    }

}
