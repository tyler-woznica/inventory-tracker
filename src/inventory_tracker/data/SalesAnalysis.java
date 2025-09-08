package inventory_tracker.data;

public class SalesAnalysis {

    // holds data
    static class ResultRow {

        String label;
        double value;

        ResultRow(String label, double value) {
            this.label = label;
            this.value = value;
        }

        @Override
        public String toString() {
            return label + " - " + value;
        }
    }






}
