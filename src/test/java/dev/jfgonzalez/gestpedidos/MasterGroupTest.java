package dev.jfgonzalez.gestpedidos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import dev.jfgonzalez.gestpedidos.model.*;
import dev.jfgonzalez.gestpedidos.service.*;

public class MasterGroupTest {

	@DisplayName("La venta genera metadatos basicos de factura")
	@Test
	public void testRealizarVentaGeneraMetadatosFactura() {
		Tienda tienda = new Tienda();
		Cliente cliente = new Cliente(1, "Ana", 4, false, "España");
		Pedido pedido = new Pedido(10, cliente);
		pedido.addProducto(new Producto(7, "Producto base", 15), 2);

		Factura factura = tienda.realizarVenta(cliente, pedido);

		assertNotNull(factura.getCodigoFactura(), "La factura deberia generar un codigo no nulo");
		assertEquals(LocalDate.now(), factura.getFechaEmision(),
				"La fecha de emision deberia ser la fecha actual");
		assertTrue(factura.getCodigoFactura().startsWith("FACT-" + LocalDate.now()),
				"El codigo de factura deberia empezar por el prefijo FACT- seguido de la fecha actual");
	}

	@DisplayName("La venta falla si el pedido no contiene productos")
	@Test
	public void testRealizarVentaConPedidoVacioFalla() {
		Tienda tienda = new Tienda();
		Cliente cliente = new Cliente(2, "Luis", 1, false, "España");
		Pedido pedido = new Pedido(11, cliente);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> tienda.realizarVenta(cliente, pedido),
				"Una venta con un pedido vacio deberia lanzar IllegalArgumentException");

		assertEquals(Pedido.PRODUCT_LIST_EMPTY_EXCEPTION_MESSAGE, exception.getMessage(),
				"El mensaje de error deberia indicar que la lista de productos esta vacia");
	}

	@DisplayName("El envio sin productos fisicos usa solo la tarifa base del pais")
	@Test
	public void testCalcularEnvioSinProductosFisicos() {
		Cliente cliente = new Cliente(3, "Marta", 2, false, "Portugal");
		Pedido pedido = new Pedido(12, cliente);
		pedido.addProducto(new Producto(1, "Licencia", 20), 1);
		pedido.addProducto(new ProductoDigital(2, "Curso", 30), 2);

		assertEquals(5.0, pedido.calcularEnvio(cliente.getPais()),
				"Un pedido sin productos fisicos esta aplicando solo la tarifa base del pais");
	}

	@DisplayName("Diagnostico: un pedido solo digital no deberia generar gastos de envio")
	@Test
	public void testCalcularEnvioSoloDigitalDebeSerCero() {
		Cliente cliente = new Cliente(30, "Marta", 2, false, "Portugal");
		Pedido pedido = new Pedido(120, cliente);
		pedido.addProducto(new ProductoDigital(10, "Curso", 30), 2);

		assertEquals(0.0, pedido.calcularEnvio(cliente.getPais()),
				"Un pedido compuesto solo por productos digitales no deberia generar gastos de envio");
	}

	@DisplayName("La venta falla si el pais del cliente es nulo")
	@Test
	public void testRealizarVentaConPaisNuloFalla() {
		Tienda tienda = new Tienda();
		Cliente cliente = new Cliente(31, "Marta", 2, false, null);
		Pedido pedido = new Pedido(121, cliente);
		pedido.addProducto(new ProductoFisico(11, "Libro", 20, 2), 1);

		assertThrows(IllegalArgumentException.class,
				() -> tienda.realizarVenta(cliente, pedido),
				"La venta deberia fallar si el pais del cliente es nulo");
	}

	@DisplayName("La venta falla si el pais del cliente esta vacio")
	@Test
	public void testRealizarVentaConPaisVacioFalla() {
		Tienda tienda = new Tienda();
		Cliente cliente = new Cliente(32, "Marta", 2, false, "   ");
		Pedido pedido = new Pedido(122, cliente);
		pedido.addProducto(new ProductoFisico(12, "Libro", 20, 2), 1);

		assertThrows(IllegalArgumentException.class,
				() -> tienda.realizarVenta(cliente, pedido),
				"La venta deberia fallar si el pais del cliente esta vacio o en blanco");
	}

	@DisplayName("La venta de un pedido solo fisico no deberia generar IVA digital")
	@Test
	public void testRealizarVentaSoloFisicoSinIvaDigital() {
		Tienda tienda = new Tienda();
		Cliente cliente = new Cliente(33, "Elena", 0, false, "España");
		Pedido pedido = new Pedido(123, cliente);
		pedido.addProducto(new ProductoFisico(13, "Teclado", 50, 2), 2);

		Factura factura = tienda.realizarVenta(cliente, pedido);

		assertEquals(100.0, factura.getTotalNeto(),
				"La venta solo con productos fisicos deberia sumar correctamente el total neto");
		assertEquals(0.4, factura.getTotalEnvio(),
				"La venta solo con productos fisicos deberia calcular el envio segun el peso total");
		assertEquals(0.0, factura.getTotalIva(),
				"La venta solo con productos fisicos no deberia incluir IVA digital");
	}

	@DisplayName("La venta de productos genericos no deberia generar envio ni IVA digital")
	@Test
	public void testRealizarVentaSoloProductosGenericosSinEnvioNiIva() {
		Tienda tienda = new Tienda();
		Cliente cliente = new Cliente(34, "Pablo", 0, false, "España");
		Pedido pedido = new Pedido(124, cliente);
		pedido.addProducto(new Producto(14, "Pack", 12), 3);

		Factura factura = tienda.realizarVenta(cliente, pedido);

		assertEquals(36.0, factura.getTotalNeto(),
				"La venta con productos genericos deberia sumar correctamente el total neto");
		assertEquals(0.0, factura.getTotalEnvio(),
				"La venta con productos no fisicos no deberia generar gastos de envio en España");
		assertEquals(0.0, factura.getTotalIva(),
				"La venta con productos no digitales no deberia generar IVA digital");
		assertEquals(36.0, factura.getTotalFinal(),
				"El total final deberia coincidir con el neto cuando no hay envio, IVA ni descuento");
	}

	@DisplayName("Diagnostico: la venta de productos genericos en Portugal no deberia cobrar envio")
	@Test
	public void testRealizarVentaSoloProductosGenericosEnPortugalSinEnvio() {
		Tienda tienda = new Tienda();
		Cliente cliente = new Cliente(37, "Sonia", 0, false, "Portugal");
		Pedido pedido = new Pedido(128, cliente);
		pedido.addProducto(new Producto(19, "Pack", 12), 3);

		Factura factura = tienda.realizarVenta(cliente, pedido);

		assertEquals(0.0, factura.getTotalEnvio(),
				"Una venta con productos genericos y sin peso fisico no deberia generar gastos de envio aunque el destino sea Portugal");
	}

	@DisplayName("Diagnostico: Pedido deberia permitir creacion con lista de productos y cantidades")
	@Test
	public void testPedidoDebePermitirCreacionConColeccionesIniciales() {
		Cliente cliente = new Cliente(50, "Nora", 3, false, "España");
		Producto productoFisico = new ProductoFisico(1, "Libro", 12, 1.5);
		Producto productoDigital = new ProductoDigital(2, "Curso", 25);
		List<Producto> productos = List.of(productoFisico, productoDigital);
		Map<Integer, Integer> cantidades = new HashMap<>();
		cantidades.put(productoFisico.getId(), 2);
		cantidades.put(productoDigital.getId(), 1);

		Pedido pedido = new Pedido(150, cliente, productos, cantidades);

		assertEquals(150, pedido.getIdPedido(),
				"El pedido creado con colecciones deberia conservar el id indicado");
		assertEquals(cliente, pedido.getCliente(),
				"El pedido creado con colecciones deberia conservar el cliente indicado");
		assertEquals(2, pedido.getProductos().size(),
				"El pedido creado con colecciones deberia incluir todos los productos recibidos");
		assertEquals(2, pedido.getCantidades().get(productoFisico.getId()),
				"La cantidad del producto fisico deberia inicializarse correctamente");
		assertEquals(1, pedido.getCantidades().get(productoDigital.getId()),
				"La cantidad del producto digital deberia inicializarse correctamente");
	}

	@DisplayName("Diagnostico: un Pedido creado con colecciones deberia poder calcular total")
	@Test
	public void testPedidoCreadoConColeccionesDebeCalcularTotal() {
		Cliente cliente = new Cliente(51, "Raul", 0, false, "España");
		Producto productoBase = new Producto(3, "Cuaderno", 5);
		Producto productoDigital = new ProductoDigital(4, "Plantilla", 10);
		List<Producto> productos = List.of(productoBase, productoDigital);
		Map<Integer, Integer> cantidades = new HashMap<>();
		cantidades.put(productoBase.getId(), 4);
		cantidades.put(productoDigital.getId(), 3);

		Pedido pedido = new Pedido(151, cliente, productos, cantidades);

		assertEquals(50.0, pedido.calcularTotal(),
				"El total del pedido creado con colecciones deberia sumar correctamente todos los importes");
	}

	@DisplayName("Diagnostico: la creacion con colecciones deberia fallar si falta una cantidad")
	@Test
	public void testPedidoCreadoConColeccionesDebeFallarSiFaltaCantidad() {
		Cliente cliente = new Cliente(52, "Eva", 1, false, "España");
		Producto productoBase = new Producto(5, "Agenda", 9);
		Producto productoDigital = new ProductoDigital(6, "Suscripcion", 14);
		List<Producto> productos = List.of(productoBase, productoDigital);
		Map<Integer, Integer> cantidades = new HashMap<>();
		cantidades.put(productoBase.getId(), 1);

		assertThrows(IllegalArgumentException.class,
				() -> new Pedido(152, cliente, productos, cantidades),
				"La creacion del pedido deberia fallar si falta la cantidad de alguno de los productos");
	}

	@DisplayName("Diagnostico: el pedido no deberia compartir la lista externa de productos")
	@Test
	public void testPedidoNoDebeCompartirListaExterna() {
		Cliente cliente = new Cliente(53, "Nora", 0, false, "España");
		Producto producto1 = new Producto(21, "Cuaderno", 8);
		Producto producto2 = new ProductoDigital(22, "Curso", 10);
		List<Producto> productos = new ArrayList<>();
		productos.add(producto1);
		productos.add(producto2);
		Map<Integer, Integer> cantidades = new HashMap<>();
		cantidades.put(producto1.getId(), 1);
		cantidades.put(producto2.getId(), 2);

		Pedido pedido = new Pedido(153, cliente, productos, cantidades);
		productos.clear();

		assertEquals(2, pedido.getProductos().size(),
				"El pedido no deberia verse afectado por cambios en la lista usada para construirlo");
	}

	@DisplayName("Diagnostico: el pedido no deberia compartir el mapa externo de cantidades")
	@Test
	public void testPedidoNoDebeCompartirMapaExterno() {
		Cliente cliente = new Cliente(54, "Nora", 0, false, "España");
		Producto producto1 = new Producto(23, "Libro", 15);
		Producto producto2 = new ProductoDigital(24, "Plantilla", 6);
		List<Producto> productos = List.of(producto1, producto2);
		Map<Integer, Integer> cantidades = new HashMap<>();
		cantidades.put(producto1.getId(), 1);
		cantidades.put(producto2.getId(), 2);

		Pedido pedido = new Pedido(154, cliente, productos, cantidades);
		cantidades.put(producto1.getId(), 99);

		assertEquals(1, pedido.getCantidades().get(producto1.getId()),
				"El pedido no deberia reflejar cambios externos en el mapa de cantidades usado para construirlo");
	}

	@DisplayName("Diagnostico: los getters del pedido no deberian permitir corromper su estado interno")
	@Test
	public void testGettersDePedidoNoDeberianPermitirMutacionExterna() {
		Cliente cliente = new Cliente(55, "Nora", 0, false, "España");
		Pedido pedido = new Pedido(155, cliente);
		Producto producto = new Producto(25, "Agenda", 11);
		pedido.addProducto(producto, 2);

		pedido.getProductos().clear();

		assertEquals(1, pedido.getProductos().size(),
				"Acceder a la lista de productos no deberia permitir borrar el contenido interno del pedido");
	}

	@DisplayName("Diagnostico: cambiar el id de un producto no deberia romper los calculos del pedido")
	@Test
	public void testCambiarIdDeProductoNoDeberiaRomperPedido() {
		Cliente cliente = new Cliente(56, "Nora", 0, false, "España");
		Pedido pedido = new Pedido(156, cliente);
		Producto producto = new Producto(26, "Pack", 20);
		pedido.addProducto(producto, 2);

		producto.setId(99);

		assertEquals(40.0, pedido.calcularTotal(),
				"Modificar el id de un producto ya incluido en el pedido no deberia corromper el total calculado");
	}

	@DisplayName("El pedido calcula IVA alternativo para productos digitales")
	@ParameterizedTest
	@CsvSource({
		"REDUCIDO,5.0",
		"SUPER,2.0"
	})
	public void testCalcularIvaConTiposAlternativos(String tipoIva, double ivaEsperado) {
		Cliente cliente = new Cliente(4, "Sara", 8, true, "España");
		Pedido pedido = new Pedido(13, cliente);
		pedido.addProducto(new ProductoDigital(1, "Suscripcion", 25), 2);
		pedido.addProducto(new ProductoFisico(2, "Libro", 18, 1.5), 1);

		assertEquals(ivaEsperado, pedido.calcularIva(tipoIva),
				"El IVA calculado deberia coincidir con el tipo de IVA solicitado para los productos digitales");
	}

	@DisplayName("La factura generada debe mantener la identidad aritmetica de la venta")
	@Test
	public void testRealizarVentaMantieneLaIdentidadAritmetica() {
		Tienda tienda = new Tienda();
		Cliente cliente = new Cliente(35, "Lucia", 10, true, "Portugal");
		Pedido pedido = new Pedido(125, cliente);
		pedido.addProducto(new ProductoFisico(15, "Tablet", 100, 1), 1);
		pedido.addProducto(new ProductoDigital(16, "Licencia", 50), 2);

		Factura factura = tienda.realizarVenta(cliente, pedido);

		assertEquals(factura.getTotalNeto() + factura.getTotalEnvio() + factura.getTotalIva() - factura.getDescuento(),
				factura.getTotalFinal(),
				"La factura deberia cumplir que totalFinal = totalNeto + totalEnvio + totalIva - descuento");
	}

	@DisplayName("Dos ventas consecutivas deberian generar codigos de factura distintos")
	@Test
	public void testRealizarVentaGeneraCodigosDistintos() {
		Tienda tienda = new Tienda();
		Cliente cliente = new Cliente(36, "Irene", 0, false, "España");
		Pedido pedido1 = new Pedido(126, cliente);
		Pedido pedido2 = new Pedido(127, cliente);
		pedido1.addProducto(new Producto(17, "Producto A", 10), 1);
		pedido2.addProducto(new Producto(18, "Producto B", 10), 1);

		Factura factura1 = tienda.realizarVenta(cliente, pedido1);
		Factura factura2 = tienda.realizarVenta(cliente, pedido2);

		assertTrue(!factura1.getCodigoFactura().equals(factura2.getCodigoFactura()),
				"Dos ventas consecutivas deberian producir codigos de factura diferentes");
	}

	@DisplayName("Factura permite creacion completa mediante constructor y acceso por getters")
	@Test
	public void testFacturaConstructorConTodosLosCampos() {
		Factura factura = new Factura("FACT-2026-05-29 12345", LocalDate.of(2026, 5, 29), 100.0, 21.0,
				5.0, 123.0, 3.0);

		assertEquals("FACT-2026-05-29 12345", factura.getCodigoFactura(),
				"El constructor de Factura deberia asignar el codigo correctamente");
		assertEquals(LocalDate.of(2026, 5, 29), factura.getFechaEmision(),
				"El constructor de Factura deberia asignar la fecha de emision correctamente");
		assertEquals(100.0, factura.getTotalNeto(),
				"El constructor de Factura deberia asignar el total neto correctamente");
		assertEquals(21.0, factura.getTotalIva(),
				"El constructor de Factura deberia asignar el total de IVA correctamente");
		assertEquals(5.0, factura.getTotalEnvio(),
				"El constructor de Factura deberia asignar el coste de envio correctamente");
		assertEquals(123.0, factura.getTotalFinal(),
				"El constructor de Factura deberia asignar el total final correctamente");
		assertEquals(3.0, factura.getDescuento(),
				"El constructor de Factura deberia asignar el descuento correctamente");
	}

	@DisplayName("Factura permite modificar todos los campos mediante setters")
	@Test
	public void testFacturaSettersYGetters() {
		Factura factura = new Factura();
		factura.setCodigoFactura("FACT-2026-05-29 12345");
		factura.setFechaEmision(LocalDate.of(2026, 5, 29));
		factura.setTotalNeto(100.0);
		factura.setTotalIva(21.0);
		factura.setTotalEnvio(5.0);
		factura.setDescuento(3.0);
		factura.setTotalFinal(123.0);

		assertEquals("FACT-2026-05-29 12345", factura.getCodigoFactura(),
				"El setter del codigo de factura deberia persistir el valor indicado");
		assertEquals(LocalDate.of(2026, 5, 29), factura.getFechaEmision(),
				"El setter de la fecha de emision deberia persistir el valor indicado");
		assertEquals(100.0, factura.getTotalNeto(),
				"El setter del total neto deberia persistir el valor indicado");
		assertEquals(21.0, factura.getTotalIva(),
				"El setter del IVA deberia persistir el valor indicado");
		assertEquals(5.0, factura.getTotalEnvio(),
				"El setter del envio deberia persistir el valor indicado");
		assertEquals(3.0, factura.getDescuento(),
				"El setter del descuento deberia persistir el valor indicado");
		assertEquals(123.0, factura.getTotalFinal(),
				"El setter del total final deberia persistir el valor indicado");
	}

	@DisplayName("Factura toString incluye todos los datos relevantes")
	@Test
	public void testFacturaToStringIncluyeContenidoEsperado() {
		Factura factura = new Factura();
		factura.setCodigoFactura("FACT-2026-05-29 12345");
		factura.setFechaEmision(LocalDate.of(2026, 5, 29));
		factura.setTotalNeto(100);
		factura.setTotalIva(21);
		factura.setTotalEnvio(5);
		factura.setDescuento(3);
		factura.setTotalFinal(123);

		String facturaTexto = factura.toString();

		assertTrue(facturaTexto.contains("Factura: FACT-2026-05-29 12345"),
				"El texto de la factura deberia incluir el codigo de factura");
		assertTrue(facturaTexto.contains("Fecha de emision: 2026-05-29"),
				"El texto de la factura deberia incluir la fecha de emision");
		assertTrue(facturaTexto.contains("Total neto: 100.0"),
				"El texto de la factura deberia incluir el total neto");
		assertTrue(facturaTexto.contains("Total IVA: 21.0"),
				"El texto de la factura deberia incluir el total de IVA");
		assertTrue(facturaTexto.contains("Total envio: 5.0"),
				"El texto de la factura deberia incluir el coste de envio");
		assertTrue(facturaTexto.contains("Descuento: 3.0"),
				"El texto de la factura deberia incluir el descuento aplicado");
		assertTrue(facturaTexto.contains("Total final: 123.0"),
				"El texto de la factura deberia incluir el total final");
	}

	@DisplayName("Diagnostico: la venta debe rechazar clientes distintos al del pedido")
	@Test
	public void testRealizarVentaConClienteDistintoAlPedidoFalla() {
		Tienda tienda = new Tienda();
		Cliente clientePedido = new Cliente(40, "Laura", 1, false, "España");
		Cliente clienteExterno = new Cliente(41, "Mario", 10, true, "Portugal");
		Pedido pedido = new Pedido(140, clientePedido);
		pedido.addProducto(new ProductoFisico(20, "Tablet", 100, 2), 1);

		assertThrows(IllegalArgumentException.class,
				() -> tienda.realizarVenta(clienteExterno, pedido),
				"La venta deberia fallar si el cliente recibido no coincide con el cliente del pedido");
	}
}
