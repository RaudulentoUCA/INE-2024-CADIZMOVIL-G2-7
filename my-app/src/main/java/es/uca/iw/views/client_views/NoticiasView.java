package es.uca.iw.views.client_views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import es.uca.iw.AuthenticatedUser;
import es.uca.iw.views.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "noticias", layout = MainLayout.class)
@RolesAllowed("USER")
@PageTitle("Noticias y promociónes")

public class NoticiasView extends VerticalLayout {
    private final AuthenticatedUser authenticatedUser;

    public NoticiasView(AuthenticatedUser authenticatedUser){
        this.authenticatedUser = authenticatedUser;
    }
}