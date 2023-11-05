package app.pedalaco.core.pedal;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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

    public void delete(Pedal pedal) {
        pedalRepository.delete(pedal);
    }

    public Page<Pedal> list(Pageable pageable, Specification<Pedal> filter) {
        return pedalRepository.findAll(filter, pageable);
    }

    public Page<Pedal> listValid(Pageable pageable) {
        Specification<Pedal> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("date"), LocalDateTime.now()));
        return list(pageable, spec);
    }

    public long countValid() {
        Specification<Pedal> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("date"), LocalDateTime.now()));
        return pedalRepository.count(spec);
    }

    public long size() {
        return pedalRepository.count();
    }


}
