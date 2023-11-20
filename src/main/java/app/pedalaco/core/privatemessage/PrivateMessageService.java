package app.pedalaco.core.privatemessage;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {

    private final PrivateMessageRepository privateMessageRepository;
    public void save(@NotNull PrivateMessage message) {
        privateMessageRepository.save(message);
    }


    public List<PrivateMessage> findAllByTopicSince(String topicId, Instant since) {
        return privateMessageRepository.findAllByTopicSince(topicId, since);
    }
}
