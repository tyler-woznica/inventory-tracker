package inventory_tracker.model;

// Bill of Materials Item
public class BOMItem {

    // data retrieved from database
    private int id;
    private int finishedGoodID;
    private int rawMaterialID;
    private int quantityReq;

    public BOMItem(int id, int finishedGoodID, int rawMaterialID, int quantityReq) {
        this.id = id;
        this.finishedGoodID = finishedGoodID;
        this.rawMaterialID = rawMaterialID;
        this.quantityReq = quantityReq;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFinishedGoodID() {
        return finishedGoodID;
    }

    public void setFinishedGoodID(int finishedGoodID) {
        this.finishedGoodID = finishedGoodID;
    }

    public int getRawMaterialID() {
        return rawMaterialID;
    }

    public void setRawMaterialID(int rawMaterialID) {
        this.rawMaterialID = rawMaterialID;
    }

    public int getQuantityReq() {
        return quantityReq;
    }

    public void setQuantityReq(int quantityReq) {
        this.quantityReq = quantityReq;
    }

    @Override
    public String toString() {
        return "BOMItem{" +
                "id=" + id +
                ", finishedGoodID=" + finishedGoodID +
                ", rawMaterialID=" + rawMaterialID +
                ", quantityReq=" + quantityReq +
                '}';
    }
}
