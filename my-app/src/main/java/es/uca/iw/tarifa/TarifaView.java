package es.uca.iw.tarifa;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToFloatConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Route("tarifa-view")
@RolesAllowed("MARKETING")
public class TarifaView extends VerticalLayout {

    private final ComboBox<Tarifa> tarifaComboBox = new ComboBox<>("Seleccionar Tarifa");
    private final FormLayout formLayout = new FormLayout();
    private final Button guardarButton = new Button("Guardar");

    private final Binder<Tarifa> binder = new Binder<>(Tarifa.class);

    // Aquí deberías inyectar tu servicio o repositorio de tarifas para obtener la lista de tarifas
    private final TarifaService tarifaService;

    public TarifaView(TarifaService tarifaService) {
        this.tarifaService = tarifaService;

        // ComboBox
        List<Tarifa> tarifas = tarifaService.getAllTarifas();
        tarifaComboBox.setItems(tarifas);
        tarifaComboBox.setItemLabelGenerator(Tarifa::getNombre);

        // Formulario
        formLayout.addFormItem(tarifaComboBox, "Tarifa");

        TextField nombre = new TextField();
        formLayout.addFormItem(nombre, "Nombre");
        binder.bind(nombre, "nombre");

        TextField precio = new TextField();
        formLayout.addFormItem(precio, "Precio");
        binder.forField(precio)
                .withConverter(new StringToFloatConverter("Ingrese un valor válido"))
                .bind(Tarifa::getPrecio, Tarifa::setPrecio);

        Checkbox permiteRoaming = new Checkbox();
        formLayout.addFormItem(permiteRoaming, "Roaming");
        binder.bind(permiteRoaming, "permiteRoaming");

        TextField descripcion = new TextField();
        formLayout.addFormItem(descripcion, "Descripción");
        binder.bind(descripcion, "descripcion");

        Checkbox fijo = new Checkbox();
        formLayout.addFormItem(fijo, "Fijo");
        binder.bind(fijo, "fijo");

        Checkbox fibra = new Checkbox();
        formLayout.addFormItem(fibra, "Fibra");
        binder.bind(fibra, "fibra");

        TextField megas = new TextField();
        formLayout.addFormItem(megas, "Megas Disponibles");
        binder.forField(megas)
                .withConverter(new StringToIntegerConverter("Ingrese un valor válido"))
                .bind(Tarifa::getAvailableMB, Tarifa::setAvailableMB);

        TextField min = new TextField();
        formLayout.addFormItem(min, "Minutos Disponibles");
        binder.forField(min)
                .withConverter(new StringToIntegerConverter("Ingrese un valor válido"))
                .bind(Tarifa::getAvailableMin, Tarifa::setAvailableMin);

        TextField sms = new TextField();
        formLayout.addFormItem(sms, "Mensajes Disponibles");
        binder.forField(sms)
                .withConverter(new StringToIntegerConverter("Ingrese un valor válido"))
                .bind(Tarifa::getAvailableSMS, Tarifa::setAvailableSMS);

        guardarButton.addClickListener(event -> guardarTarifa());
        tarifaComboBox.addValueChangeListener(event -> mostrarDetallesTarifa(event.getValue()));
        add(formLayout, guardarButton);
    }

    private void mostrarDetallesTarifa(Tarifa tarifa) {
        if (tarifa != null) {
            binder.setBean(tarifa);
        }
    }

    private void guardarTarifa() {
        Tarifa tarifa = binder.getBean();
        tarifaService.guardarTarifa(tarifa);
        Notification.show("Tarifa guardada correctamente");
    }
}