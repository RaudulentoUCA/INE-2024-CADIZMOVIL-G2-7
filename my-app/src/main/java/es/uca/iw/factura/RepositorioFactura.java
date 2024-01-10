package es.uca.iw.factura;
import es.uca.iw.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepositorioFactura extends JpaRepository<Factura, Integer> {

    List<Factura> findAll();

    List<Factura> findAllByCliente(Cliente cliente);
}
