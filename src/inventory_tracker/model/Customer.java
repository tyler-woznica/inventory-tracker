package inventory_tracker.model;

public class Customer {

    // data retrieved from database
    private int id;
    private String business_name;
    private String email;
    private Long phone;
    private String city;
    private String state;

    public Customer(int id, String business_name, String email, Long phone, String city, String state) {
        this.id = id;
        this.business_name = business_name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.state = state;
    }

    public Customer(String business_name, String email, Long phone, String city, String state) {
        this.business_name = business_name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", business_name='" + business_name + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

}


