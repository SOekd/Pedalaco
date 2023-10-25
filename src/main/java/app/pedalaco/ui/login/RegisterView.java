package app.pedalaco.ui.login;

import app.pedalaco.core.security.AuthenticatedUser;
import app.pedalaco.core.user.User;
import app.pedalaco.core.user.UserRole;
import app.pedalaco.core.user.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@AnonymousAllowed
@PageTitle("Registrar")
@Route(value = "register")
public class RegisterView extends VerticalLayout implements BeforeEnterObserver {


    private final AuthenticatedUser authenticatedUser;

    private final UserService userService;

    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterView(AuthenticatedUser authenticatedUser, UserService userService) {
        this.authenticatedUser = authenticatedUser;
        this.userService = userService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    private void render() {
        var binder = new Binder<User>();

        EmailField email = new EmailField("Email");
        binder.forField(email)
                .asRequired("O email é obrigatório")
                .withValidator(RegisterView::isValidEmail, "O email é inválido")
                .withValidator(content -> userService.getByEmail(content).isEmpty(), "O email já está em uso")
                .bind(User::getEmail, User::setEmail);

        TextField name = new TextField("Nome");
        binder.forField(name)
                .asRequired("O nome é obrigatório")
                .withValidator(content -> !content.isEmpty(), "O nome deve ter no mínimo 1 caracter")
                .bind(User::getName, User::setName);

        TextField username = new TextField("Usuário");
        binder.forField(username)
                .asRequired("O usuário é obrigatório")
                .withValidator(content -> content.length() >= 4, "O usuário deve ter no mínimo 4 caracteres")
                .withValidator(content -> userService.getByUsername(content).isEmpty(), "O usuário já está em uso")
                .bind(User::getUsername, User::setUsername);

        PasswordField passwordField = new PasswordField("Senha");
        PasswordField passwordConfirmationField = new PasswordField("Confirmar Senha");

        AtomicReference<String> passwordStart = new AtomicReference<>("");
        binder.forField(passwordField)
                .asRequired("A senha é obrigatória")
                .withValidator(password -> password.length() >= 8, "A senha deve ter no mínimo 8 caracteres")
//                .withValidator(RegisterView::isSecurePassword, "A senha deve conter letras maiúsculas, minúsculas e números")
                .bind((user) -> passwordStart.get(), (user, password) -> passwordStart.set(password));

        AtomicReference<String> passwordToConfirm = new AtomicReference<>("");
        binder.forField(passwordConfirmationField)
                .asRequired("A senha é obrigatória")
                .withValidator(password -> password.equals(passwordField.getValue()), "As senhas não coincidem")
                .bind(user -> passwordToConfirm.get(), (user, password) -> passwordToConfirm.set(password));


        FormLayout formLayout = new FormLayout();
        formLayout.add(email, username, name, passwordField, passwordConfirmationField);
        formLayout.setColspan(name, 2);

        var registerButton = new Button("Registrar");

        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.setWidthFull();

        registerButton.addClickListener(listener -> {

            if (binder.validate().hasErrors()) {
                binder.validate().getValidationErrors().forEach(validationError -> {
                    var notification = new Notification(validationError.getErrorMessage(), 3000);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
                });
                return;
            }

            userService.update(User.builder()
                    .email(email.getValue())
                    .username(username.getValue())
                    .name(name.getValue())
                    .experiencePoints(0L)
                    .biography("")
                    .roles(Set.of(UserRole.USER))
                    .hashedPassword(passwordEncoder.encode(passwordField.getValue()))
                    .build());

            getUI().ifPresent(ui -> ui.navigate("login"));

        });

        H2 title = new H2("REGISTRAR");

        add(title, formLayout, registerButton);
        setPadding(true);
        addClassName("register-form");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            event.forwardTo("");
        }

        render();
    }


    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pattern.matcher(email).matches();
    }

    private static boolean isSecurePassword(String password) {
        if (password == null) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasUpperCase && hasLowerCase && hasDigit && password.length() >= 8;
    }

}
