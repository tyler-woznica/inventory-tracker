package inventory_tracker.model;

public class Customer {

    // Database fields
    private int id;
    private String businessName;
    private String email;
    private Long phone;
    private String city;
    private String state;

    /**
     * Constructor with ID (used when retrieving from database)
     * @param id Customer ID from database
     * @param businessName Business name of the customer
     * @param email Email address of the customer
     * @param phone Phone number as Long to handle large numbers
     * @param city City where customer is located
     * @param state State where customer is located
     */
    public Customer(int id, String businessName, String email, Long phone, String city, String state) {
        this.id = id;
        this.businessName = businessName;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.state = state;
    }

    /**
     * Constructor without ID (used when creating new customers)
     * @param businessName Business name of the customer
     * @param email Email address of the customer
     * @param phone Phone number as Long to handle large numbers
     * @param city City where customer is located
     * @param state State where customer is located
     */
    public Customer(String businessName, String email, Long phone, String city, String state) {
        this.businessName = businessName;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.state = state;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getPhone() { return phone; }
    public void setPhone(Long phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", businessName='" + businessName + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}