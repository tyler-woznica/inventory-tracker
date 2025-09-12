package inventory_tracker.model;

public class OrderItem {

    // Database fields
    private int id;
    private int orderId;
    private int inventoryId;
    private int quantity;

    /**
     * Constructor without ID for creating new order items
     * @param orderId ID of the order this item belongs to
     * @param inventoryId ID of the inventory item being ordered
     * @param quantity Quantity of the item being ordered
     */
    public OrderItem(int orderId, int inventoryId, int quantity) {
        this.orderId = orderId;
        this.inventoryId = inventoryId;
        this.quantity = quantity;
    }

    /**
     * Constructor with ID for retrieving from database
     * @param id OrderItem ID from database
     * @param orderId ID of the order this item belongs to
     * @param inventoryId ID of the inventory item being ordered
     * @param quantity Quantity of the item being ordered
     */
    public OrderItem(int id, int orderId, int inventoryId, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.inventoryId = inventoryId;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", inventoryId=" + inventoryId +
                ", quantity=" + quantity +
                '}';
    }
}