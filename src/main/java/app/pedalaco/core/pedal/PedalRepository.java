package app.pedalaco.core.pedal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PedalRepository extends JpaRepository<Pedal, UUID>, JpaSpecificationExecutor<Pedal> {

    @Query("SELECT p FROM Pedal p JOIN p.participants participants WHERE participants.id = :userId")
    List<Pedal> findPedalsByParticipantId(UUID userId);


}
