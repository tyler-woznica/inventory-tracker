package inventory_tracker.model;

import java.sql.Timestamp;

class Order {

    // Database fields
    private int id;
    private int customerId;
    private Timestamp orderDate; // FIXED: Changed from int to Timestamp
    private double total;
    private int alert; // Alert flag for order issues (e.g., insufficient stock)

    /**
     * Constructor with ID for retrieving from database
     * @param id Order ID from database
     * @param customerId ID of the customer who placed the order
     * @param orderDate Timestamp when the order was placed
     */
    public Order(int id, int customerId, Timestamp orderDate) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.total = 0.0; // Will be calculated by database triggers
        this.alert = 0; // Default: no alert
    }

    /**
     * Full constructor including total and alert
     * @param id Order ID from database
     * @param customerId ID of the customer who placed the order
     * @param orderDate Timestamp when the order was placed
     * @param total Total amount of the order
     * @param alert Alert status for the order
     */
    public Order(int id, int customerId, Timestamp orderDate, double total, int alert) {
        this.id = id;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.total = total;
        this.alert = alert;
    }

    /**
     * Constructor without ID for creating new orders
     * @param customerId ID of the customer placing the order
     */
    public Order(int customerId) {
        this.customerId = customerId;
        this.orderDate = new Timestamp(System.currentTimeMillis()); // Current time
        this.total = 0.0; // Will be calculated by database triggers
        this.alert = 0; // Default: no alert
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getAlert() { return alert; }
    public void setAlert(int alert) { this.alert = alert; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", orderDate=" + orderDate +
                ", total=" + total +
                ", alert=" + alert +
                '}';
    }
}