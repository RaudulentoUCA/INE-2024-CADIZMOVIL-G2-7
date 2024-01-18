package es.uca.iw.views.formulario;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import es.uca.iw.cliente.Cliente;
import es.uca.iw.cliente.ServiciosCliente;
import es.uca.iw.views.MainLayout;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Route(value = "recuperar-contra", layout = MainLayout.class)
@AnonymousAllowed
public class RecuperarContraView extends VerticalLayout {

    private String correoGuardado;

    private final ServiciosCliente serviciosCliente;
    TextField correoTextField, contrasenaTextField, codigoTextField;
    Button enviarCorreoButton, nuevaAccionButton;
    Span aclaracion;

    String codigo;

    public RecuperarContraView(ServiciosCliente serviciosCliente) {
        this.serviciosCliente = serviciosCliente;

        // Campo y botón para introducir el correo
        H2 texto = new H2("Recuperar Contraseña");
        correoTextField = new TextField("Email");
        enviarCorreoButton = new Button("Enviar", e -> enviarCorreo(correoTextField.getValue()));

        // Campos inicialmente ocultos
        aclaracion = new Span("Se ha envíado un código al correo introducido, por favor escriba el código y la nueva contraseña.");
        contrasenaTextField = new TextField("Nueva Contraseña");
        codigoTextField = new TextField("Código recibido");
        nuevaAccionButton = new Button("Establecer nueva Contraseña", e -> nuevaAccion(codigoTextField.getValue()));
        contrasenaTextField.setVisible(false);
        nuevaAccionButton.setVisible(false);
        codigoTextField.setVisible(false);
        aclaracion.setVisible(false);

        // Diseño vertical para los elementos
        add(texto, correoTextField, enviarCorreoButton, aclaracion, contrasenaTextField, codigoTextField, nuevaAccionButton);
    }

    private void enviarCorreo(String correo) {
        // Guardar el correo
        correoGuardado = correo;

        // Generar el código aleatorio
        codigo = generarCodigoAleatorio();

        // Ocultar el primer conjunto de elementos
        correoTextField.setVisible(false);
        enviarCorreoButton.setVisible(false);

        // Mostrar el segundo conjunto de elementos
        aclaracion.setVisible(true);
        contrasenaTextField.setVisible(true);
        codigoTextField.setVisible(true);
        nuevaAccionButton.setVisible(true);



        try {
            // Configuración del servidor de correo saliente (SMTP)
            String host = "smtp.gmail.com";
            String username = "raulero999z@gmail.com";
            String password = "jbhy zmka jida huqk";

            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            // Crear una sesión con autenticación
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Crear un objeto Message
            Message message = new MimeMessage(session);
            // Configurar el remitente
            message.setFrom(new InternetAddress(username));
            // Configurar el destinatario
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoGuardado));
            // Asunto del correo
            message.setSubject("Recuperar Contraseña CadizMovil");

            // Establecer el contenido del mensaje
            String cuerpoMensaje = "Estimado usuario,\n\n"
                    + "Hemos recibido una solicitud para restablecer la contraseña de tu cuenta en CadizMovil.\n\n"
                    + "Tu código de verificación es: " + codigo + "\n\n"
                    + "Utiliza este código para restablecer tu contraseña en la aplicación.\n\n"
                    + "Gracias, equipo de soporte CadizMovil.";
            message.setText(cuerpoMensaje);

            // Enviar el mensaje
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void nuevaAccion(String nuevoValor) {
        // Realizar la nueva acción con el nuevo valor
        if (codigo.equals(nuevoValor)) {
            Optional<Cliente> user = serviciosCliente.cargarUsuarioPorEmail(correoGuardado);
            if (user.isPresent() && contrasenaTextField.getValue() != null) {
                user.get().setPassword(contrasenaTextField.getValue());
                serviciosCliente.registrarCliente(user.get());
                Notification.show("Contraseña cambiada");
            }
        }
    }

    private String generarCodigoAleatorio() {
        int numeroAleatorio = generarNumeroAleatorio(10000000, 99999999);
        return generarVocalAleatoria() + String.valueOf(numeroAleatorio) + generarVocalAleatoria();
    }

    private int generarNumeroAleatorio(int min, int max) {
        SecureRandom random = new SecureRandom();
        return random.nextInt((max - min) + 1) + min;
    }

    private char generarVocalAleatoria() {
        char[] vocales = {'a', 'e', 'i', 'o', 'u'};
        SecureRandom random = new SecureRandom();
        return vocales[random.nextInt(vocales.length)];
    }
}