package inventory_tracker.model;

public class OrderItem {

    // data retrieved from database
    private int id;
    private int orderID;
    private int finishedGoodID;
    private int quantity;
    private double unitPrice;

    public OrderItem(int id, int orderID, int finishedGoodID, int quantity, double unitPrice) {
        this.id = id;
        this.orderID = orderID;
        this.finishedGoodID = finishedGoodID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getFinishedGoodID() {
        return finishedGoodID;
    }

    public void setFinishedGoodID(int finishedGoodID) {
        this.finishedGoodID = finishedGoodID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderID=" + orderID +
                ", finishedGoodID=" + finishedGoodID +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
