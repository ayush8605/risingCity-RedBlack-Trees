public class Building {

    private int buildingNum;
    private int executed_time;
    private int total_time;

    Building(int buildingNum, int total_time, int executed_time) {
        this.buildingNum = buildingNum;
        this.total_time = total_time;
        this.executed_time = executed_time;
    }

    int getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(int buildingNum) {
        this.buildingNum = buildingNum;
    }

    int getExecuted_time() {
        return executed_time;
    }

    void setExecuted_time(int executed_time) {
        this.executed_time = executed_time;
    }

    int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }
}
