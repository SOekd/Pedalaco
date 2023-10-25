package app.pedalaco.ui.login;

import app.pedalaco.core.security.AuthenticatedUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import javax.print.DocFlavor;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

    }

    private void render(boolean error) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("ENTRAR");
        i18nForm.setUsername("E-Mail");
        i18nForm.setPassword("Senha");
        i18nForm.setSubmit("Entrar");

        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("ERRO");
        i18nErrorMessage.setMessage(
                "Nome de usuário ou senha incorretos. Por favor, tente novamente.");
        i18n.setErrorMessage(i18nErrorMessage);

        LoginForm loginForm = new LoginForm();
        loginForm.setI18n(i18n);

        loginForm.setForgotPasswordButtonVisible(false);

        loginForm.setAction("login");

        if (error) {
            loginForm.setError(true);
        }

        Button registerButton = new Button("Registrar", listener -> getUI().ifPresent(ui -> ui.navigate("register")));

        add(loginForm, registerButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            event.forwardTo("");
            return;
        }

        render(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}
