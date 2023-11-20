package app.pedalaco.core.privatemessage;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PrivateMessageRepository extends CrudRepository<PrivateMessage, UUID> {

    @Query("SELECT m FROM PrivateMessage m WHERE m.topic = ?1 AND m.sentAt >= ?2 ORDER BY m.sentAt ASC")
    List<PrivateMessage> findAllByTopicSince(String topicId, Instant since);

}