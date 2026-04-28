package donmario.model;

public class DigitalMovie implements Movie {

    private final String title;
    private final int price;
    private boolean available;

    public DigitalMovie(String title, int price, boolean available) {
        this.title = title;
        this.price = price;
        this.available = available;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public String getType() { return "Digital"; }

    @Override
    public int getPrice() { return price; }

    @Override
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }
}