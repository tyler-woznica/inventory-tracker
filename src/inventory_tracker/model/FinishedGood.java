package inventory_tracker.model;

public class FinishedGood {

    // data will be retrieved from database
    private int id;
    private String name;
    private int quantity;

    public FinishedGood(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "FinishedGood{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
