package app.pedalaco.core.privatemessage;

import app.pedalaco.core.pedal.PedalMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PrivateChatRepository extends CrudRepository<PrivateChat, UUID> {

}