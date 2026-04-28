package donmario;

import donmario.factory.MovieFactory;
import donmario.membership.BasicMembership;
import donmario.membership.MembershipStrategy;
import donmario.membership.PremiumMembership;
import donmario.model.Movie;
import donmario.rental.RentalService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Catálogo
        List<Movie> catalog = new ArrayList<>();
        catalog.add(MovieFactory.create(MovieFactory.PHYSICAL, "Interestellar", 8000, true));
        catalog.add(MovieFactory.create(MovieFactory.PHYSICAL, "El Padrino",    7000, false));
        catalog.add(MovieFactory.create(MovieFactory.DIGITAL,  "Inception",     5000, true));
        catalog.add(MovieFactory.create(MovieFactory.DIGITAL,  "Matrix",        6000, true));

        Scanner scanner = new Scanner(System.in);

        // Mostrar catálogo
        System.out.println("\n=== VIDEOCLUB DON MARIO ===\n");
        System.out.println("Peliculas disponibles:\n");
        for (int i = 0; i < catalog.size(); i++) {
            Movie m = catalog.get(i);
            String estado = m.isAvailable() ? "Disponible" : "No disponible";
            System.out.printf("  [%d] [%s] %s - $%,.0f - %s%n",
                    i + 1, m.getType(), m.getTitle(), (double) m.getPrice(), estado);
        }

        // Selección de membresía
        System.out.println("\nTipo de membresia:");
        System.out.println("  [1] Basica");
        System.out.println("  [2] Premium (20% descuento)");
        System.out.print("Seleccione: ");

        MembershipStrategy membership;
        String membershipInput = scanner.nextLine().trim();
        membership = membershipInput.equals("2") ? new PremiumMembership() : new BasicMembership();

        // Selección de películas
        System.out.print("\nSeleccione peliculas (numeros separados por coma): ");
        String movieInput = scanner.nextLine().trim();

        List<Movie> selected = new ArrayList<>();
        List<String> errors  = new ArrayList<>();

        Arrays.stream(movieInput.split(","))
              .map(String::trim)
              .forEach(token -> {
                  try {
                      int idx = Integer.parseInt(token) - 1;
                      if (idx < 0 || idx >= catalog.size()) {
                          errors.add("Numero " + (idx + 1) + " fuera de rango.");
                          return;
                      }
                      Movie m = catalog.get(idx);
                      if (!m.isAvailable()) {
                          errors.add("'" + m.getTitle() + "' no esta disponible.");
                          return;
                      }
                      selected.add(m);
                  } catch (NumberFormatException e) {
                      errors.add("'" + token + "' no es un numero valido.");
                  }
              });

        if (!errors.isEmpty()) {
            System.out.println("\nAdvertencias:");
            errors.forEach(e -> System.out.println("   - " + e));
        }

        // Generar recibo
        if (selected.isEmpty()) {
            System.out.println("\nNo se selecciono ninguna pelicula disponible.");
        } else {
            new RentalService().printReceipt(selected, membership);
        }

        scanner.close();
    }
}