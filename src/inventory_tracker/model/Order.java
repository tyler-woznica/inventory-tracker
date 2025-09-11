package inventory_tracker.model;

import java.time.LocalDateTime;

public class Order {

    // data retrieved from database
    private int id;
    private int customerID;
    // orderDate may type may need to be changed based on SQL
    private LocalDateTime orderDate;

    public Order(int id, int customerID, LocalDateTime orderDate) {
        this.id = id;
        this.customerID = customerID;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customerID=" + customerID +
                ", orderDate=" + orderDate +
                '}';
    }
}
