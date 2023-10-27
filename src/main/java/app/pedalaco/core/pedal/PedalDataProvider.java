package app.pedalaco.core.pedal;

import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

@SpringComponent
public class PedalDataProvider implements CallbackDataProvider.FetchCallback<Pedal, Void> {

    private final PedalService pedalService;

    @Autowired
    public PedalDataProvider(PedalService pedalService) {
        this.pedalService = pedalService;
    }

    @Override
    public Stream<Pedal> fetch(Query<Pedal, Void> query) {
        return pedalService.list(PageRequest.of(query.getPage(),
                query.getPageSize(), Sort.by("date"))).stream();
    }

}
