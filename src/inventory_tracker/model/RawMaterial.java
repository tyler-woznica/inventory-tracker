package inventory_tracker.model;

public class RawMaterial {

    // data will be pulled from database
    private int id;
    private String name;
    private double quantityoz;
    private double unitCost;

    public RawMaterial(int id, String name, double quantityoz, double unitCost) {
        this.id = id;
        this.name = name;
        this.quantityoz = quantityoz;
        this.unitCost = unitCost;
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

    public double getQuantityoz() {
        return quantityoz;
    }

    public void setQuantityoz(double quantityoz) {
        this.quantityoz = quantityoz;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    @Override
    public String toString() {
        return "RawMaterial{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantityoz=" + quantityoz +
                ", unitCost=" + unitCost +
                '}';
    }
}
