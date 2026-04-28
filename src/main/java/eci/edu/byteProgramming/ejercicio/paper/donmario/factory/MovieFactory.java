package donmario.factory;

import donmario.model.DigitalMovie;
import donmario.model.Movie;
import donmario.model.PhysicalMovie;

public class MovieFactory {

    public static final String PHYSICAL = "fisica";
    public static final String DIGITAL  = "digital";

    private MovieFactory() {}

    public static Movie create(String type, String title, int price, boolean available) {
        return switch (type.toLowerCase()) {
            case PHYSICAL -> new PhysicalMovie(title, price, available);
            case DIGITAL  -> new DigitalMovie(title, price, available);
            default -> throw new IllegalArgumentException("Tipo no soportado: " + type);
        };
    }
}