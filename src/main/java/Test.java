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
            mostrarLibros();

            System.out.println("\n=== Menú ===");
            System.out.println("1. Registrar Préstamo");
            System.out.println("2. Generar Reporte");
            System.out.println("3. Generar Reporte HTML");
            System.out.println("4. Salir");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea pendiente

            switch (opcion) {
                case 1:
                    registrarPrestamo(scanner);
                    break;
                case 2:
                    generarReporte();
                    break;
                case 3:
                    generarReporteHTML();
                    break;
                case 4:
                    System.out.println("¡Hasta luego!");
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
            }
        }
    }

    // Método para inicializar la lista de libros
    private static void inicializarLibros() {
        listaLibros.add(new Libro("Java Programming", "John Doe"));
        listaLibros.add(new Libro("Python Basics", "Jane Smith"));
        listaLibros.add(new Libro("Web Development", "Sam Johnson"));
    }

    // Método para mostrar la lista de libros
    private static void mostrarLibros() {
        System.out.println("\n=== Lista de Libros ===");
        for (int i = 0; i < listaLibros.size(); i++) {
            System.out.println((i + 1) + ". " + listaLibros.get(i).getTitulo());
        }
    }

    // Método para registrar un préstamo
    private static void registrarPrestamo(Scanner scanner) {
        System.out.println("=== Registrar Préstamo ===");

        System.out.println("Ingrese el nombre del usuario:");
        String nombreUsuario = scanner.nextLine();

        System.out.println("Ingrese el número del libro a prestar:");
        int indiceLibro = scanner.nextInt();
        scanner.nextLine(); // Consumir la nueva línea pendiente

        // Obtener el libro y el usuario seleccionados
        Libro libroSeleccionado = listaLibros.get(indiceLibro - 1);
        Usuario usuarioSeleccionado = obtenerUsuario(nombreUsuario);

        // Obtener la fecha actual como fecha de préstamo
        LocalDate fechaPrestamo = LocalDate.now();

        // Solicitar la fecha de devolución
        System.out.println("Ingrese la fecha de devolución (formato: dd/MM/yyyy):");
        String fechaDevolucionStr = scanner.nextLine();
        LocalDate fechaDevolucion = obtenerFecha(fechaDevolucionStr);

        // Crear un nuevo préstamo
        Prestamo prestamo = new Prestamo(libroSeleccionado, usuarioSeleccionado, fechaPrestamo);
        prestamo.setFechaDevolucion(fechaDevolucion);

        // Agregar el préstamo a la lista de préstamos
        listaPrestamos.add(prestamo);

        System.out.println("Préstamo registrado con éxito.");
    }

    // Método para obtener un usuario existente o crear uno nuevo
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

    // Método para obtener una fecha a partir de una cadena
    private static LocalDate obtenerFecha(String fechaStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(fechaStr, formatter);
    }

    // Método para generar un informe en consola
    private static void generarReporte() {
        System.out.println("\n=== Reporte de Préstamos ===");
        for (Prestamo prestamo : listaPrestamos) {
            System.out.println("Libro: " + prestamo.getLibro().getTitulo());
            System.out.println("Autor: " + prestamo.getLibro().getAutor());
            System.out.println("Usuario: " + prestamo.getUsuario().getNombre());
            System.out.println("Fecha de Préstamo: " + prestamo.getFechaPrestamo());
            System.out.println("Fecha de Devolución: " + prestamo.getFechaDevolucion());
            System.out.println("----------------------");
        }
    }

    // Método para generar un informe HTML
    private static void generarReporteHTML() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("informe_prestamos.html"))) {
            writer.println("<html>");
            writer.println("<head>");
            writer.println("<title>Informe de Préstamos</title>");
            writer.println("<style>");
            writer.println("table { border-collapse: collapse; width: 100%; }");
            writer.println("th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }");
            writer.println("th { background-color: #f2f2f2; }");
            writer.println("</style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("<h1>Informe de Préstamos</h1>");

            writer.println("<table>");
            writer.println("<tr><th>Libro</th><th>Autor</th><th>Usuario</th><th>Fecha de Préstamo</th><th>Fecha de Devolución</th></tr>");

            for (Prestamo prestamo : listaPrestamos) {
                writer.println("<tr>");
                writer.println("<td>" + prestamo.getLibro().getTitulo() + "</td>");
                writer.println("<td>" + prestamo.getLibro().getAutor() + "</td>");
                writer.println("<td>" + prestamo.getUsuario().getNombre() + "</td>");
                writer.println("<td>" + prestamo.getFechaPrestamo() + "</td>");
                writer.println("<td>" + prestamo.getFechaDevolucion() + "</td>");
                writer.println("</tr>");
            }

            writer.println("</table>");
            writer.println("</body>");
            writer.println("</html>");

            System.out.println("Informe HTML generado con éxito.");
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + e.getMessage());
        }
    }
}
