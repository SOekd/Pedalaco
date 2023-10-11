package app.pedalaco.ui.views;

import app.pedalaco.ui.components.ToolbarComponent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value ="chat/:chatID", layout = ToolbarComponent.class)
@AnonymousAllowed
public class ChatView extends VerticalLayout implements BeforeEnterListener {

    private Span span= new Span();
    public ChatView(){
        add(span);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(event.getRouteParameters() == null){
            span.setText("unknown conversation");
        }else{
            span.setText("conversation with "+event.getRouteParameters().get("userId"));
        }
    }
}
