package es.uca.iw.views.client_views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.AuthenticatedUser;
import es.uca.iw.cliente.Cliente;
import es.uca.iw.cliente.ServiciosCliente;
import es.uca.iw.contrato.Contrato;
import es.uca.iw.contrato.ServiciosContrato;
import es.uca.iw.tarifa.Tarifa;
import es.uca.iw.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Route(value = "ajustes-contrato", layout = MainLayout.class)
@RolesAllowed("USER")
@PageTitle("Ajustes de contrato")
public class AjustesContrato extends VerticalLayout {
    private final AuthenticatedUser authenticatedUser;

    private final ServiciosContrato serviciosContrato;
    private final ComboBox<Contrato> contratoComboBox = new ComboBox<>("Seleccionar Contrato");
    private final Button guardarButton = new Button("Guardar");
    private final Checkbox compartirDatos;
    private final Button guardar;

    public AjustesContrato(AuthenticatedUser authenticatedUser, ServiciosContrato serviciosContrato){
        this.authenticatedUser = authenticatedUser;
        this.serviciosContrato = serviciosContrato;

        add(new H2("Seleccione uno de sus contratos"));
        compartirDatos = new Checkbox();
        compartirDatos.setLabel("Compartir Datos");
        compartirDatos.setVisible(false);
        guardar = new Button();
        guardar.setText("Guardar Datos");
        guardar.setVisible(false);
        Optional<Cliente> optionalCliente = authenticatedUser.get();

        add(contratoComboBox, compartirDatos, guardar);

        if(optionalCliente.isPresent()){
            // ComboBox
            List<Contrato> contratos = serviciosContrato.getContratosByCliente(optionalCliente.get());
            contratoComboBox.setItems(contratos);
            contratoComboBox.setItemLabelGenerator(contrato -> String.valueOf(contrato.getId()));
            contratoComboBox.addValueChangeListener(event -> ejecutarfuncion(event.getValue()));
        }

    }

    private void ejecutarfuncion(Contrato value) {
        compartirDatos.setVisible(true);
        guardar.setVisible(true);

        guardar.addClickListener(event -> actualizarContrato(value));
    }

    private void actualizarContrato(Contrato value) {

        value.setCompartirDatos(compartirDatos.getValue());
        serviciosContrato.guardarContrato(value);
    }
}