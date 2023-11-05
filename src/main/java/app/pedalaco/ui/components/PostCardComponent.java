package app.pedalaco.ui.components;

import app.pedalaco.core.pedal.Pedal;
import app.pedalaco.core.pedal.PedalService;
import app.pedalaco.core.user.User;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.val;

public class PostCardComponent extends VerticalLayout {

    private final User user;

    private final PedalService pedalService;

    private final Pedal pedal;


    private final String mapKey;
    private final Runnable renderCallback;

    private GoogleMapMarker marker;

    public PostCardComponent(User user, PedalService pedalService, Pedal pedal, String mapKey, Runnable renderCallback) {
        this.user = user;
        this.pedalService = pedalService;
        this.pedal = pedal;
        this.mapKey = mapKey;
        this.renderCallback = renderCallback;

        render();
    }

    private void render() {

        // border
        getStyle().set("border", "1px solid #ccc");
        getStyle().set("border-radius", "5px");
        // shadow
        getStyle().set("box-shadow", "0 2px 4px 0 rgba(0,0,0,.2)");

        setMargin(true);

        setBoxSizing(BoxSizing.BORDER_BOX);

        setMaxWidth("90%");

        val title = new H3(pedal.getTitle());
        title.addClassName(LumoUtility.FontSize.XSMALL);

        val description = new Paragraph(pedal.getDescription());
        description.addClassName(LumoUtility.FontSize.XXSMALL);
        description.setMaxWidth("100%");
        description.getStyle().set("white-space", "normal");
        description.getStyle().set("overflow-wrap", "break-word");
        description.getStyle().set("word-wrap", "break-word");

        GoogleMap googleMap = new GoogleMap(mapKey, null, null);
        googleMap.setMapType(GoogleMap.MapType.ROADMAP);
        googleMap.disableStreetViewControl(true);
        googleMap.disableMapTypeControl(true);
        googleMap.setCenter(new LatLon(pedal.getStartX(), pedal.getStartY()));
        googleMap.setZoom(18);

        googleMap.addMarker(new GoogleMapMarker("Inicio", new LatLon(pedal.getStartX(), pedal.getStartY()), false, "https://i.imgur.com/6EYeiMu.png"));
        googleMap.addMarker(new GoogleMapMarker("Fim", new LatLon(pedal.getEndX(), pedal.getEndY()), false, "https://i.imgur.com/k6AqYGA.png"));

        marker = new GoogleMapMarker("Você", new LatLon(pedal.getStartX(), pedal.getStartY()), false, "https://i.imgur.com/ZAgej1K.png");
        marker.setAnimationEnabled(true);
        googleMap.addMarker(marker);

        googleMap.setSizeFull();

        googleMap.setHeight(200, Unit.PIXELS);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();

        Button comments = new Button(VaadinIcon.COMMENTS.create());
        comments.setText("Comentar");
        horizontalLayout.add(comments);

        if (pedal.getAuthor().getId().equals(user.getId())) {
            Button cancel = new Button(VaadinIcon.EDIT.create());
            cancel.setText("Cancelar");
            cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
            cancel.addClickListener(event -> cancelPedal().open());
            horizontalLayout.add(cancel);
        } else if (pedal.getParticipants().stream().anyMatch(it -> it.getId().equals(user.getId()))) {
            Button exit = new Button(VaadinIcon.EXIT.create());
            exit.setText("Sair");
            exit.addThemeVariants(ButtonVariant.LUMO_ERROR);
            horizontalLayout.add(exit);
        } else {
            Button join = new Button(VaadinIcon.PLUS.create());
            join.setText("Participar");
            join.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            horizontalLayout.add(join);
        }

        if (pedal.getImage() != null) {
            Button image = new Button(VaadinIcon.PICTURE.create());
            image.setText("Imagem");
            horizontalLayout.add(image);
        }

        add(title, description, googleMap, horizontalLayout);

    }

    public void updateMarker(double x, double y) {
        marker.setPosition(new LatLon(x, y));
    }

    private ConfirmDialog cancelPedal() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Cancelar Pedal");
        dialog.setText(
                "Você tem certeza que deseja cancelar o pedal?\n\n\nAo cancelar um pedal, você será banido de criar novos pedais por 30 dias.");


        dialog.setRejectable(true);
        dialog.setRejectText("Sair");

        dialog.setConfirmText("Confirmar");
        dialog.addConfirmListener(event -> {

            pedalService.delete(pedal);
            renderCallback.run();

        });

        return dialog;
    }

}
