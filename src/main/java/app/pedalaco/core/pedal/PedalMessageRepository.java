package app.pedalaco.core.pedal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface PedalMessageRepository extends JpaRepository<PedalMessage, UUID>, JpaSpecificationExecutor<PedalMessage> {
}
