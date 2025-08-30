package inventory_tracker.model;

public class Customer {

    // data will be retrieved from database
    public static int id;
    private String businessName;
    private String contactName;
    private String email;
    private int phoneNumber;
    private String street;
    private String city;
    private String state;
    private int zip;

    public Customer(String businessName, String contactName, String email, int phoneNumber, String street, String city, String state, int zip) {
        this.businessName = businessName;
        this.contactName = contactName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Customer.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
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

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "businessName='" + businessName + '\'' +
                ", contactName='" + contactName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip=" + zip +
                '}';
    }

}


