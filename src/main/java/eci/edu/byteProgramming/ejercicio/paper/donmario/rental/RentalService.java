package donmario.rental;

import donmario.membership.MembershipStrategy;
import donmario.model.Movie;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RentalService {

    private static final NumberFormat FMT =
            NumberFormat.getNumberInstance(new Locale("es", "CO"));

    public void printReceipt(List<Movie> movies, MembershipStrategy membership) {
        int subtotal = movies.stream().mapToInt(Movie::getPrice).sum();
        int total    = membership.applyDiscount(subtotal);
        int discount = subtotal - total;

        StringBuilder sb = new StringBuilder();
        sb.append("\n--- RECIBO DE ALQUILER ---\n");
        sb.append("Cliente: ").append(membership.getName()).append("\n");
        sb.append("Peliculas:\n");

        for (Movie m : movies) {
            sb.append(String.format(" - %s (%s) - $%s%n",
                    m.getTitle(), m.getType(), FMT.format(m.getPrice())));
        }

        sb.append("Subtotal: $").append(FMT.format(subtotal)).append("\n");

        if (membership.getDiscountPercent() > 0) {
            sb.append("Descuento (").append(membership.getDiscountPercent())
              .append("%): $").append(FMT.format(discount)).append("\n");
        }

        sb.append("Total a pagar: $").append(FMT.format(total)).append("\n");
        sb.append("--------------------------\n");
        sb.append("¡Disfrute su pelicula!\n");

        System.out.println(sb);
    }
}