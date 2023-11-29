package app.pedalaco.ui.components;

import app.pedalaco.core.pedal.Pedal;
import app.pedalaco.core.pedal.PedalService;
import app.pedalaco.core.privatemessage.PrivateMessagePersister;
import app.pedalaco.core.user.User;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMap;
import com.flowingcode.vaadin.addons.googlemaps.GoogleMapMarker;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.MessageManager;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.BoxSizing;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.val;

import java.io.ByteArrayInputStream;

public class PostCardComponent extends VerticalLayout {

    private final User user;

    private final PedalService pedalService;

    private final PrivateMessagePersister messagePersister;
    private final Pedal pedal;


    private final String mapKey;
    private final Runnable renderCallback;

    private GoogleMapMarker marker;
    private CollaborationMessageList list;

    public PostCardComponent(User user, PedalService pedalService, PrivateMessagePersister messagePersister, Pedal pedal, String mapKey, Runnable renderCallback) {
        this.user = user;
        this.pedalService = pedalService;
        this.messagePersister = messagePersister;
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
        comments.addClickListener(listener -> {

            Dialog dialog = new Dialog();

            dialog.setMaxWidth("100%");
            dialog.setMaxHeight("100%");

            dialog.setCloseOnEsc(true);

            dialog.setModal(true);

            VerticalLayout dialogLayout = new VerticalLayout();

            list = new CollaborationMessageList(new UserInfo(String.valueOf(user.getId()), user.getName()), "chat/" + pedal.getId());
            list.setSizeFull();

            CollaborationMessageInput input = new CollaborationMessageInput(list);
            input.setWidthFull();
            VerticalLayout chatContainer = new VerticalLayout();
            chatContainer.addClassNames(LumoUtility.Flex.AUTO, LumoUtility.Overflow.HIDDEN);

            new MessageManager(this, new UserInfo(String.valueOf(user.getId()), user.getName()), "chat/" + pedal.getId(), messagePersister);

            chatContainer.add(list, input);
            dialogLayout.add(chatContainer);
            dialogLayout.setSizeFull();
            dialogLayout.expand(list);

            dialogLayout.setPadding(false);
            dialogLayout.setMargin(false);

            dialogLayout.setMinHeight("100%");
            dialogLayout.setMinWidth("100%");
            dialog.add(dialogLayout);

            dialog.open();
        });
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
            exit.addClickListener(event -> {
                pedal.getParticipants().removeIf(it -> it.getId().equals(user.getId()));
                pedalService.save(pedal);
                renderCallback.run();
            });
            horizontalLayout.add(exit);
        } else {
            Button join = new Button(VaadinIcon.PLUS.create());
            join.setText("Participar");
            join.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            join.addClickListener(event -> {
                pedal.getParticipants().add(user);
                pedalService.save(pedal);
                renderCallback.run();
            });
            horizontalLayout.add(join);
        }

        if (pedal.getImage() != null) {
            Button image = new Button(VaadinIcon.PICTURE.create());
            image.setText("Imagem");
            horizontalLayout.add(image);
        }

        val participants = new HorizontalLayout();
        participants.setAlignItems(Alignment.CENTER);
        participants.setSpacing(false);
        participants.setMargin(false);
        participants.setPadding(false);
        participants.setJustifyContentMode(JustifyContentMode.END);

        Span label = new Span(VaadinIcon.USER.create());
        Span counter = new Span(String.valueOf(pedal.getParticipants().size()));
        counter.getElement().getThemeList().add("badge pill small contrast");
        counter.getStyle().set("margin-inline-start", "var(--lumo-space-s)");
        participants.add(label, counter);

        participants.addClickListener(listener -> {

            Dialog participantDialog = new Dialog();

            participantDialog.setMaxWidth("100%");
            participantDialog.setMaxHeight("100%");

            participantDialog.setCloseOnEsc(true);

            participantDialog.setModal(true);

            VerticalLayout paritipantDialogLayout = new VerticalLayout();


            pedal.getParticipants().forEach(it -> {
                HorizontalLayout participant = new HorizontalLayout();
                participant.setAlignItems(Alignment.CENTER);

                Avatar avatar = new Avatar(it.getName());


                if (it.getProfilePicture() != null)
                    avatar.setImageResource(new StreamResource("profile-picture", () -> new ByteArrayInputStream(it.getProfilePicture())));

                val username = new Span(it.getUsername());


                participant.add(avatar, username);

                paritipantDialogLayout.add(participant);
            });


            paritipantDialogLayout.setSizeFull();

            paritipantDialogLayout.setPadding(false);
            paritipantDialogLayout.setMargin(false);

            paritipantDialogLayout.setMinHeight("100%");
            paritipantDialogLayout.setMinWidth("100%");

            participantDialog.add(paritipantDialogLayout);

            participantDialog.open();
        });

        horizontalLayout.add(participants);

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
