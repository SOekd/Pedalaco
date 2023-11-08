package app.pedalaco.core.privatemessage;

import app.pedalaco.core.user.User;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class PrivateChatService {

    private final PrivateChatRepository privateChatRepository;

    public PrivateChat createOrLoadChat(@NotNull User author, @NotNull User target) {

        PrivateChat alreadyCreatedChat = Stream.concat(Stream.concat(author.getAuthoredChats().stream(), author.getReceivedChats().stream()), Stream.concat(target.getReceivedChats().stream(), target.getAuthoredChats().stream()))
                .distinct()
                .filter(chat -> chat.getReceiver().equals(author) && chat.getAuthor().equals(target)
                        || chat.getAuthor().equals(author) && chat.getReceiver().equals(target))
                .findAny()
                .orElse(null);

        if (alreadyCreatedChat != null) {
            return alreadyCreatedChat;
        }

        PrivateChat newChat = new PrivateChat(null, author, target, new ArrayList<>());
        return privateChatRepository.save(newChat);
    }

}
