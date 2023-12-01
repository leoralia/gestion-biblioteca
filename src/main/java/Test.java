/*escribir html - errotes (opciones) - escribir en consola y archivo - 
manejo fechas - fecha "string a objetos" - mantener las clases - lista de las 
clases - recibir datos
*/
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    private static List<Libro> listaLibros = new ArrayList<>();
    private static List<Usuario> listaUsuarios = new ArrayList<>();
    private static List<Prestamo> listaPrestamos = new ArrayList<>();

    public static void main(String[] args) {
        inicializarLibros();

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("\n=== Menú ===");
            System.out.println("1. Registrar Préstamo");
            System.out.println("2. Generar Reporte HTML");
            System.out.println("3. Salir");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea pendiente

            switch (opcion) {
                case 1:
                    registrarPrestamo(scanner);
                    break;
                case 2:
                    generarReporteHTML();
                    break;
                case 3:
                    System.out.println("¡Hasta luego!");
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        }
    }

    // Método - lista de libros - Libro.java
    private static void inicializarLibros() {
        listaLibros.add(new Libro("Cien años de soledad", "Gabriel García Márquez"));
        listaLibros.add(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes Saavedra"));
        listaLibros.add(new Libro("La sombra del viento", "Carlos Ruiz Zafón"));
        listaLibros.add(new Libro("Como agua para chocolate", "Laura Esquivel"));
        listaLibros.add(new Libro("Rayuela", "Julio Cortázar"));
        listaLibros.add(new Libro("Crónica de una muerte anunciada", "Gabriel García Márquez"));
        listaLibros.add(new Libro("Ficciones", "Jorge Luis Borges"));
        listaLibros.add(new Libro("La casa de los espíritus", "Isabel Allende"));
        listaLibros.add(new Libro("Pedro Páramo", "Juan Rulfo"));
        listaLibros.add(new Libro("Aura", "Carlos Fuentes"));

    }

    // Metodo - mostrar libros
    private static void mostrarLibros() {
        System.out.println("\n=== Lista de Libros ===");
        for (int i = 0; i < listaLibros.size(); i++) {
            System.out.println((i + 1) + ". " + listaLibros.get(i).getTitulo());
        }
    }

    // Método - registro prestamo
private static void registrarPrestamo(Scanner scanner) {
        System.out.println("=== Registrar Préstamo ===");

        System.out.println("Ingrese el nombre del usuario:");
        String nombreUsuario = scanner.nextLine();

        System.out.println("Ingrese el número del libro a prestar (ingrese '0' para finalizar):");

        // Mostrar libros
        mostrarLibros();

        List<Libro> librosSeleccionados = new ArrayList<>();

        while (true) {
            int indiceLibro = scanner.nextInt();
            scanner.nextLine();

            if (indiceLibro == 0) {
                break; // salida
            }

            if (indiceLibro < 1 || indiceLibro > listaLibros.size()) {
                System.out.println("Número de libro no válido. Inténtelo de nuevo.");
            } else {
                // libro seleccionado y agregardo a lista
                Libro libroSeleccionado = listaLibros.get(indiceLibro - 1);
                librosSeleccionados.add(libroSeleccionado);
            }
        }

        // usuario seleccionado
        Usuario usuarioSeleccionado = obtenerUsuario(nombreUsuario);

        // fecha de préstamo - la actual
        LocalDate fechaPrestamo = LocalDate.now();

        // fecha de devolucion
        System.out.println("Ingrese la fecha de devolución (formato: dd/MM/yyyy):");
        String fechaDevolucionStr = scanner.nextLine();
        LocalDate fechaDevolucion = obtenerFecha(fechaDevolucionStr);

        // Crear un nuevo prestamo para cada libro seleccionado
        for (Libro libro : librosSeleccionados) {
            // Crear un nuevo préstamo
            Prestamo prestamo = new Prestamo(libro, usuarioSeleccionado, fechaPrestamo);
            prestamo.setFechaDevolucion(fechaDevolucion);

            // Agregar el préstamo a la lista 
            listaPrestamos.add(prestamo);
        }

        System.out.println("Préstamo registrado con éxito.");
}
    
    //
    // Método - usuario
    private static Usuario obtenerUsuario(String nombreUsuario) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getNombre().equalsIgnoreCase(nombreUsuario)) {
                return usuario;
            }
        }

        // Si no se encuentra, crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario(nombreUsuario);
        listaUsuarios.add(nuevoUsuario);
        return nuevoUsuario;
    }

    // Método - string a fecha
    private static LocalDate obtenerFecha(String fechaStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(fechaStr, formatter);
    }

    // Método informe html
    private static void generarReporteHTML() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("informe_prestamos.html"))) {
            //tabla y estilo
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<title>Gestión de Biblioteca</title>");
            writer.println("<style>");
            writer.println("table { border-collapse: collapse; width: 100%; }");
            writer.println("th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }");
            writer.println("th { background-color: #336699; color: #fff; }");
            writer.println("</style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<h1>Gestión de Biblioteca</h1>");

            writer.println("<table>");
            writer.println("<tr><th>Usuario</th><th>Libro</th><th>Autor</th><th>Fecha de Préstamo</th><th>Fecha de Devolución</th></tr>");
            
            // usuario-libro-autor-prestamo-devolucion - iterar
            for (Prestamo prestamo : listaPrestamos) {
                writer.println("<tr>");
                writer.println("<td>" + prestamo.getUsuario().getNombre() + "</td>");
                writer.println("<td>" + prestamo.getLibro().getTitulo() + "</td>");
                writer.println("<td>" + prestamo.getLibro().getAutor() + "</td>");                
                writer.println("<td>" + prestamo.getFechaPrestamo() + "</td>");
                writer.println("<td>" + prestamo.getFechaDevolucion() + "</td>");
                writer.println("</tr>");
            }

            writer.println("</table>");
            writer.println("</body>");
            writer.println("</html>");

            System.out.println("Informe HTML generado con éxito.");
        } 
        //excep - error
        catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}