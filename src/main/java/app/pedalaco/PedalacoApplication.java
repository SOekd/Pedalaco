package app.pedalaco;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@PWA(name = "Pedalaco", shortName = "Pedalaco")
@Theme(value = "my-theme", variant = Lumo.DARK)
public class PedalacoApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(PedalacoApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void afterStartup() {


	}

}
