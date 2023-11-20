package app.pedalaco;

import com.vaadin.collaborationengine.CollaborationEngineConfiguration;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.io.File;

@SpringBootApplication
@PWA(name = "Pedalaco", shortName = "Pedalaco")
@Theme(value = "my-theme", variant = Lumo.DARK)
@Push
public class PedalacoApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(PedalacoApplication.class, args);
	}

	@Bean
	public CollaborationEngineConfiguration ceConfigBean() {
		CollaborationEngineConfiguration configuration = new CollaborationEngineConfiguration(
				licenseEvent -> {
					// See <<ce.production.license-events>>
				});


		val projectDir = new File("").getAbsolutePath();
		configuration.setDataDir(projectDir);
		return configuration;
	}

}
