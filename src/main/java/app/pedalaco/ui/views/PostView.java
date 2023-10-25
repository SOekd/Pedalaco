package app.pedalaco.ui.views;

import app.pedalaco.core.maps.MapsCredentialProvider;
import app.pedalaco.core.pedal.Pedal;
import app.pedalaco.core.security.AuthenticatedUser;
import app.pedalaco.core.util.ImageHelper;
import app.pedalaco.ui.components.ToolbarComponent;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapPoint;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapPolygon;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.parameters.P;
import org.vaadin.firitin.components.upload.UploadFileHandler;
import org.vaadin.firitin.geolocation.Geolocation;
import org.vaadin.firitin.geolocation.GeolocationOptions;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

@Route(value = "post", layout = ToolbarComponent.class)
@AnonymousAllowed
public class PostView extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;

    private final MapsCredentialProvider mapsCredentialProvider;

    private Double locationX;

    private Double locationY;

    public PostView(AuthenticatedUser authenticatedUser, MapsCredentialProvider mapsCredentialProvider) {
        this.authenticatedUser = authenticatedUser;
        this.mapsCredentialProvider = mapsCredentialProvider;
        setAlignItems(Alignment.CENTER);

        render();
    }

    private void render() {
        add(
                new H2("Criar Pedal"),
                new Paragraph("Você está na página de criação de pedal!")
        );

        renderForm();
    }

    private void renderForm() {
        var formLayout = new FormLayout();

        var binder = new Binder<Pedal>();

        var titleField = new TextField("Título");
        titleField.setMaxLength(50);
        titleField.setPlaceholder("Digite o título do pedal");
        binder.forField(titleField)
                .asRequired("O título é obrigatório!")
                .withValidator(title -> title.length() >= 5, "O título deve ter pelo menos 5 caracteres!")
                .withValidator(title -> title.length() <= 50, "O título deve ter no máximo 50 caracteres!")
                .bind(Pedal::getTitle, Pedal::setTitle);

        var descriptionField = new TextArea("Descrição");
        descriptionField.setMaxLength(1000);
        descriptionField.setPlaceholder("Digite a descrição do pedal");
        descriptionField.setClearButtonVisible(true);
        descriptionField.setHeight("230px");
        descriptionField.setWidth("180px");
        binder.forField(descriptionField)
                .asRequired("A descrição é obrigatória!")
                .withValidator(description -> description.length() >= 20, "A descrição deve ter pelo menos 20 caracteres!")
                .withValidator(description -> description.length() <= 1000, "A descrição deve ter no máximo 1000 caracteres!")
                .withValidator(description -> description.matches("^[a-zA-Z0-9\\s\\p{Punct}]+$"), "A descrição deve conter apenas letras, números e caracteres especiais!")
                .bind(Pedal::getDescription, Pedal::setDescription);

        var dateField = new DateTimePicker("Dia e hora do pedal");
        dateField.setValue(LocalDateTime.now().plusDays(1));
        binder.forField(dateField)
                .asRequired("A data é obrigatória!")
                .withValidator(date -> date.isAfter(LocalDateTime.now()), "A data deve ser posterior à data atual!")
                .bind(Pedal::getDate, Pedal::setDate);

        VerticalLayout imageLayout = new VerticalLayout();

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);


        // o evento não é implementado nativamente no component, no suporte do framework me recomendaram usar isso.
        upload.getElement().addEventListener("file-remove", event -> imageLayout.removeAll()).addEventData("event.detail.file.name");

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();

            Runnable errorNotification = () -> getUI().get().access(() -> {
                Notification notification = new Notification("A imagem pode estar corrompida ou não é realmente uma imagem.", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
                imageLayout.removeAll();
            });

            try {
                var image = ImageIO.read(inputStream);

                if (image == null) {
                    errorNotification.run();
                    return;
                }

                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                ImageIO.write(image, ImageHelper.getFormat(fileName), bout);

                var bytes = bout.toByteArray();
                StreamResource resource = new StreamResource(fileName, () -> new ByteArrayInputStream(bytes));

                bout.close();

                Image imageField = new Image();
                imageField.setMaxHeight("300px");
                imageField.setMaxWidth("300px");
                imageField.setSrc(resource);
                imageField.setVisible(true);
                imageLayout.removeAll();
                imageLayout.add(imageField);
            } catch (Exception ex) {
                ex.printStackTrace();
                errorNotification.run();
            }
        });

        upload.addDetachListener(listener -> imageLayout.removeAll());


        upload.setAcceptedFileTypes("image/jpeg", "image/png");

        upload.setMaxFileSize(5 * 1024 * 1024);
        upload.setDropAllowed(false);

        UploadI18N i18n = new UploadI18N();

        var addFiles = new UploadI18N.AddFiles();
        addFiles.setOne("Adicionar Imagem");
        i18n.setAddFiles(addFiles);

        var dropFiles = new UploadI18N.DropFiles();
        dropFiles.setOne("Arraste e solte uma imagem aqui");
        i18n.setDropFiles(dropFiles);

        var error = new UploadI18N.Error();
        error.setIncorrectFileType("Você deve selecionar uma imagem com extensão .jpg ou .png!");
        error.setFileIsTooBig("A imagem deve ter no máximo 5MB!");
        error.setTooManyFiles("Você deve selecionar apenas uma imagem!");
        i18n.setError(error);

        var file = new UploadI18N.File();
        file.setRemove("Remover");
        file.setRetry("Tentar novamente");
        file.setStart("Iniciar");
        i18n.setFile(file);

        upload.setI18n(i18n);

        formLayout.add(titleField, descriptionField, dateField);

        var post = new Button("Criar Pedal");

        VerticalLayout mapsLayout = new VerticalLayout();
        Geolocation.getCurrentPosition(
                event -> {
                    GoogleMap gmaps = new GoogleMap(mapsCredentialProvider.getCredentials().toString(), null, null);
                    gmaps.setMapType(GoogleMap.MapType.ROADMAP);
                    gmaps.setCenter(new LatLon(event.getCoords().getLatitude(), event.getCoords().getLongitude()));

                    gmaps.setHeight("400px");
                    gmaps.setWidth("400px");

                    gmaps.setZoom(15);

                    System.out.println(event.getCoords().getLatitude());
                    System.out.println(event.getCoords().getLongitude());

                    getUI().ifPresent(ui -> ui.access(() -> {
                        mapsLayout.add(gmaps);
                    }));
                },
                browserError -> Notification.show("ERROR: " + browserError),
                new GeolocationOptions(true, 2000, 7000)
        );


        post.addClickListener(listener -> {
            System.out.println(mapsCredentialProvider.getCredentials().toString());

            if (!binder.isValid()) {
                Notification notification = new Notification("Preencha todos os campos corretamente!", 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
                return;
            }

            var pedal = new Pedal();
            binder.writeBeanIfValid(pedal);

            var author = authenticatedUser.get().orElseThrow();
            pedal.setAuthor(author);

        });

        add(formLayout, upload, imageLayout, mapsLayout, post);
    }


}
