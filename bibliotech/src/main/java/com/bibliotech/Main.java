package com.bibliotech;

import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import com.bibliotech.service.*;
import com.bibliotech.exception.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    // Repositorios
    private static final RecursoRepository recursoRepository = new InMemoryRecursoRepository();
    private static final SocioRepository socioRepository = new InMemorySocioRepository();
    private static final PrestamoRepository prestamoRepository = new InMemoryPrestamoRepository();

    // servicios (DIP - inyección por constructor)
    private static final RecursoService recursoService = new RecursoServiceImp(recursoRepository);
    private static final SocioService socioService = new SocioServiceImp(socioRepository);
    private static final PrestamoService prestamoService = new PrestamoServiceImpl(prestamoRepository, recursoRepository, socioRepository);

    public static void main(String[] args) {
        System.out.println("=== Bienvenido a BiblioTech ===");
        boolean corriendo = true;

        while (corriendo) {
            mostrarMenuPrincipal();
            int opcion = leerEntero();

            switch (opcion) {
                case 1 -> menuRecursos();
                case 2 -> menuSocios();
                case 3 -> menuPrestamos();
                case 0 -> {
                    System.out.println("Hasta luego!");
                    corriendo = false;
                }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Gestión de Recursos");
        System.out.println("2. Gestión de Socios");
        System.out.println("3. Gestión de Préstamos");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    // RECURSOS
    private static void menuRecursos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Gestión de Recursos ---");
            System.out.println("1. Registrar Libro");
            System.out.println("2. Registrar Ebook");
            System.out.println("3. Buscar por ISBN");
            System.out.println("4. Listar todos");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            switch (leerEntero()) {
                case 1 -> registrarLibro();
                case 2 -> registrarEbook();
                case 3 -> buscarRecursoPorIsbn();
                case 4 -> listarRecursos();
                case 0 -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void registrarLibro() {
        try {
            System.out.print("ISBN (13 digitos): ");
            String isbn = scanner.nextLine();
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Año: ");
            int anio = leerEntero();
            System.out.print("Categoría (FICCION, CIENCIA, HISTORIA, TECNOLOGIA, ARTE, OTRO): ");
            Categoria categoria = Categoria.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("Número de páginas: ");
            int paginas = leerEntero();
            System.out.print("Editorial: ");
            String editorial = scanner.nextLine();
            System.out.print("Ubicación estantería (ej: A1-E3): ");
            String ubicacion = scanner.nextLine();

            Libro libro = new Libro(isbn, titulo, autor, anio, categoria, paginas, editorial, ubicacion);
            recursoService.registrarRecurso(libro);
            System.out.println("Libro registrado exitosamente.");
        } catch (IsbnDuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Categoría inválida.");
        }
    }

    private static void registrarEbook() {
        try {
            System.out.print("ISBN (13 digitos): ");
            String isbn = scanner.nextLine();
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Año: ");
            int anio = leerEntero();
            System.out.print("Categoría (FICCION, CIENCIA, HISTORIA, TECNOLOGIA, ARTE, OTRO): ");
            Categoria categoria = Categoria.valueOf(scanner.nextLine().toUpperCase());
            System.out.print("Formato (PDF, EPUB): ");
            String formato = scanner.nextLine();
            System.out.print("Tamaño (MB) (ej: 5.2): ");
            double tamanio = Double.parseDouble(scanner.nextLine());
            System.out.print("URL de descarga (https://...): ");
            String url = scanner.nextLine();

            Ebook ebook = new Ebook(isbn, titulo, autor, anio, categoria, formato, tamanio, url);
            recursoService.registrarRecurso(ebook);
            System.out.println("Ebook registrado exitosamente.");
        } catch (IsbnDuplicadoException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Categoría inválida.");
        }
    }

    private static void buscarRecursoPorIsbn() {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        recursoService.buscarPorIsbn(isbn)
                .ifPresentOrElse(
                        r -> System.out.println("Encontrado: " + r.titulo() + " - " + r.autor()),
                        () -> System.out.println("No se encontró ningún recurso con ese ISBN.")
                );
    }

    private static void listarRecursos() {
        List<Recurso> recursos = recursoService.buscarTodos();
        if (recursos.isEmpty()) {
            System.out.println("No hay recursos registrados.");
        } else {
            recursos.forEach(r -> System.out.println("- [" + r.isbn() + "] " + r.titulo() + " - " + r.autor()));
        }
    }

    // SOCIOS
    private static void menuSocios() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Gestión de Socios ---");
            System.out.println("1. Registrar Estudiante");
            System.out.println("2. Registrar Docente");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Listar todos");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            switch (leerEntero()) {
                case 1 -> registrarSocio("ESTUDIANTE");
                case 2 -> registrarSocio("DOCENTE");
                case 3 -> buscarSocioPorId();
                case 4 -> listarSocios();
                case 0 -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void registrarSocio(String tipo) {
        try {
            System.out.print("Nombre (solo letras): ");
            String nombre = scanner.nextLine();
            System.out.print("DNI (7 u 8 dígitos): ");
            String dni = scanner.nextLine();
            System.out.print("Email (ej: usuario@dominio.com): ");
            String email = scanner.nextLine();

            if (tipo.equals("ESTUDIANTE")) {
                socioService.registrarEstudiante(nombre, dni, email);
            } else {
                socioService.registrarDocente(nombre, dni, email);
            }
            System.out.println(tipo + " registrado exitosamente.");
        } catch (BibliotecaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void buscarSocioPorId() {
        System.out.print("ID del socio: ");
        int id = leerEntero();
        socioService.buscarPorId(id)
                .ifPresentOrElse(
                        s -> System.out.println("Encontrado: " + s.getNombre() + " - DNI: " + s.getDni()),
                        () -> System.out.println("No se encontró ningún socio con ese ID.")
                );
    }

    private static void listarSocios() {
        List<Socio> socios = socioService.buscarTodos();
        if (socios.isEmpty()) {
            System.out.println("No hay socios registrados.");
        } else {
            socios.forEach(s -> System.out.println("- [" + s.getId() + "] " + s.getNombre() + " - " + s.getClass().getSimpleName()));
        }
    }

    // PRESTAMOS
    private static void menuPrestamos() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n--- Gestión de Préstamos ---");
            System.out.println("1. Realizar préstamo");
            System.out.println("2. Devolver préstamo");
            System.out.println("3. Listar todos");
            System.out.println("4. Listar por socio");
            System.out.println("5. Listar activos");
            System.out.println("6. Listar vencidos");
            System.out.println("7. Próximos a vencer");
            System.out.println("8. Verificar disponibilidad de recurso");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");

            switch (leerEntero()) {
                case 1 -> realizarPrestamo();
                case 2 -> devolverPrestamo();
                case 3 -> listarPrestamos();
                case 4 -> listarPrestamosPorSocio();
                case 5 -> listarActivos();
                case 6 -> listarVencidos();
                case 7 -> proximosAVencer();
                case 8 -> verificarDisponibilidad();
                case 0 -> volver = true;
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private static void listarActivos() {
        List<Prestamo> prestamos = prestamoService.listarActivos();
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos activos.");
        } else {
            prestamos.forEach(p -> System.out.println(
                    "- [" + p.getId() + "] " + p.getRecurso().titulo() +
                            " -> " + p.getSocio().getNombre() +
                            " | Vence: " + p.getFechaLimite()
            ));
        }
    }

    private static void listarVencidos() {
        List<Prestamo> prestamos = prestamoService.listarVencidos();
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos vencidos.");
        } else {
            prestamos.forEach(p -> System.out.println(
                    "- [" + p.getId() + "] " + p.getRecurso().titulo() +
                            " -> " + p.getSocio().getNombre() +
                            " | Venció: " + p.getFechaLimite()
            ));
        }
    }

    private static void proximosAVencer() {
        System.out.print("Días a verificar (ej: 3): ");
        int dias = leerEntero();
        List<Prestamo> prestamos = prestamoService.proximosAVencer(dias);
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos próximos a vencer en " + dias + " días.");
        } else {
            prestamos.forEach(p -> System.out.println(
                    "- [" + p.getId() + "] " + p.getRecurso().titulo() +
                            " -> " + p.getSocio().getNombre() +
                            " | Vence: " + p.getFechaLimite()
            ));
        }
    }

    private static void verificarDisponibilidad() {
        System.out.print("ISBN del recurso (13 dígitos): ");
        String isbn = scanner.nextLine();
        boolean disponible = prestamoService.verificarDisponibilidad(isbn);
        if (disponible) {
            System.out.println("El recurso está disponible para préstamo.");
        } else {
            System.out.println("El recurso no está disponible, tiene un préstamo activo.");
        }
    }

    private static void realizarPrestamo() {
        try {
            System.out.print("ID del socio: ");
            int socioId = leerEntero();
            System.out.print("ISBN del recurso (13 dígitos): ");
            String isbn = scanner.nextLine();
            System.out.print("Días de préstamo (1-30): ");
            int dias = leerEntero();

            prestamoService.realizarPrestamo(socioId, isbn, dias);
            System.out.println("Préstamo realizado exitosamente.");
        } catch (BibliotecaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void devolverPrestamo() {
        try {
            System.out.print("ID del préstamo: ");
            int prestamoId = leerEntero();
            prestamoService.devolverPrestamo(prestamoId);
            System.out.println("Devolución registrada exitosamente.");
        } catch (BibliotecaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listarPrestamos() {
        List<Prestamo> prestamos = prestamoService.buscarTodos();
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos registrados.");
        } else {
            prestamos.forEach(p -> System.out.println(
                    "- [" + p.getId() + "] " + p.getRecurso().titulo() +
                            " -> " + p.getSocio().getNombre() +
                            " | Estado: " + p.getEstado() +
                            " | Vence: " + p.getFechaLimite()
            ));
        }
    }

    private static void listarPrestamosPorSocio() {
        System.out.print("ID del socio: ");
        int socioId = leerEntero();
        List<Prestamo> prestamos = prestamoService.buscarPorSocioId(socioId);
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos para ese socio.");
        } else {
            prestamos.forEach(p -> System.out.println(
                    "- [" + p.getId() + "] " + p.getRecurso().titulo() +
                            " | Estado: " + p.getEstado() +
                            " | Vence: " + p.getFechaLimite()
            ));
        }
    }

    // UTILS
    private static int leerEntero() {
        try {
            int valor = Integer.parseInt(scanner.nextLine());
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida, se usará 0.");
            return 0;
        }
    }
}