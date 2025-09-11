package inventory_tracker.model;

class Item {

    // Database fields
    private int id;
    private String name;
    private int quantity;
    private double price;
    private int alert; // Alert flag (0 or 1) for low stock

    /**
     * Constructor without ID and alert (used for creating new items)
     * @param name Name of the inventory item
     * @param quantity Current quantity in stock
     * @param price Price per unit
     */
    public Item(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.alert = 0; // Default: no alert
    }

    /**
     * Constructor with ID (used when retrieving from database)
     * @param id Item ID from database
     * @param name Name of the inventory item
     * @param quantity Current quantity in stock
     * @param price Price per unit
     */
    public Item(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.alert = 0; // Default: no alert
    }

    /**
     * Full constructor including alert field
     * @param id Item ID from database
     * @param name Name of the inventory item
     * @param quantity Current quantity in stock
     * @param price Price per unit
     * @param alert Alert status (0 = no alert, 1 = low stock alert)
     */
    public Item(int id, String name, int quantity, double price, int alert) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.alert = alert;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getAlert() { return alert; }
    public void setAlert(int alert) { this.alert = alert; }

    /**
     * Checks if this item is currently on low stock alert
     * @return true if alert flag is set, false otherwise
     */
    public boolean isLowStock() {
        return alert == 1;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", alert=" + alert +
                '}';
    }
}