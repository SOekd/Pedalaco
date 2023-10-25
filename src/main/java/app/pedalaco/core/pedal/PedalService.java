package app.pedalaco.core.pedal;

import app.pedalaco.core.user.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PedalService {

    private final PedalRepository pedalRepository;


    public Pedal save(Pedal pedal) {
        return pedalRepository.save(pedal);
    }

    public Optional<Pedal> findById(UUID id) {
        return pedalRepository.findById(id);
    }

    public Page<Pedal> list(Pageable pageable) {
        return pedalRepository.findAll(pageable);
    }

    public Page<Pedal> list(Pageable pageable, Specification<Pedal> filter) {
        return pedalRepository.findAll(filter, pageable);
    }

}
