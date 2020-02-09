import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

public class risingCity {

    static int globalTime = 0;
    static MinHeap minHeap;
    static RedBlackTree rbt;
    static TreeMap<Integer, String> inputSequence;


    public static void main(String[] args) {

        File file = new File(System.getProperty("user.dir") + File.separator + "output.txt");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        minHeap = new MinHeap();
        rbt = new RedBlackTree();


        inputSequence = FileUtil.readFile(args[0]);
        if (inputSequence.isEmpty()) {
            FileUtil.writeOutputToFile("input file is empty");
            return;
        }
        startConstruction();

    }

    private static void processFirstOperation() {

        globalTime = inputSequence.firstKey();
        String firstOperation = inputSequence.get(inputSequence.firstKey());
        inputSequence.remove(globalTime);

        if (firstOperation.charAt(0) == 'I' || firstOperation.charAt(0) == 'i') {
            insert(firstParameter(firstOperation), secondParameter(firstOperation), 0);
        } else if (firstOperation.contains(",")) {
            print(firstParameter(firstOperation), secondParameter(firstOperation));
        } else {
            print(Integer.parseInt(firstOperation.substring(firstOperation.indexOf('(') + 1, firstOperation.indexOf(')'))));
        }
    }

    private static void startConstruction() {

        Building currentBuilding = null;
        int constructionDay = 0;

        processFirstOperation();

        while (!rbt.isEmpty() || !minHeap.isEmpty()) {
            globalTime++;
            if (inputSequence.containsKey(globalTime)) {
                String operation = inputSequence.get(globalTime);
                inputSequence.remove(globalTime);
                if (operation.charAt(0) == 'I' || operation.charAt(0) == 'i') {
                    insert(firstParameter(operation), secondParameter(operation), 0);
                } else if (operation.contains(",")) {
                    print(firstParameter(operation), secondParameter(operation));
                } else {
                    print(Integer.parseInt(operation.substring(operation.indexOf('(') + 1, operation.indexOf(')'))));
                }
            }

            if (currentBuilding == null) {
                currentBuilding = minHeap.extractMin();
                constructionDay++;
                currentBuilding.setExecuted_time(currentBuilding.getExecuted_time() + 1);
                if ((currentBuilding.getExecuted_time()) == currentBuilding.getTotal_time()) {

                    rbt.delete(currentBuilding);

                    FileUtil.writeOutputToFile("(" + currentBuilding.getBuildingNum() + "," + globalTime + ")");
                    currentBuilding = null;
                    constructionDay = 0;
                }
            } else {
                constructionDay++;
                currentBuilding.setExecuted_time(currentBuilding.getExecuted_time() + 1);

                if ((currentBuilding.getExecuted_time()) == currentBuilding.getTotal_time()) {

                    rbt.delete(currentBuilding);

                    FileUtil.writeOutputToFile("(" + currentBuilding.getBuildingNum() + "," + globalTime + ")");
                    currentBuilding = null;
                    constructionDay = 0;
                } else if (constructionDay == 5) {
                    rbt.delete(currentBuilding);
                    insert(currentBuilding.getBuildingNum(), currentBuilding.getTotal_time(), currentBuilding.getExecuted_time());
                    currentBuilding = null;
                    constructionDay = 0;
                }
            }
        }
        if (!inputSequence.isEmpty()) {
            startConstruction();
        } else {
            FileUtil.writeOutputToFile("(" + globalTime + ")");
        }
    }

    private static void print(int buildingNum) {
        String output = rbt.searchNode(buildingNum);
        if (output != null) {
            FileUtil.writeOutputToFile(output);
        } else {
            FileUtil.writeOutputToFile("(0,0,0)");
        }
    }

    private static void print(int buildingNum1, int buildingNum2) {
        List<Building> output = rbt.printTreenInRange(buildingNum1, buildingNum2);
        StringBuilder triplets = new StringBuilder();
        for (Building b : output) {
            triplets.append("(" + b.getBuildingNum() + "," + b.getExecuted_time() + "," + b.getTotal_time() + ")");
            triplets.append(" ");
        }
        if (triplets.length() == 0) {
            FileUtil.writeOutputToFile("(0,0,0)");
        } else {
            FileUtil.writeOutputToFile(triplets.toString().trim());
        }
    }

    private static int firstParameter(String operation) {
        return Integer.parseInt(operation.substring(operation.indexOf('(') + 1, operation.indexOf(',')));
    }

    private static int secondParameter(String operation) {
        return Integer.parseInt(operation.substring(operation.indexOf(',') + 1, operation.indexOf(')')));
    }

    private static void insert(int buildingNum, int total_time, int executed_time) {
        Building building = new Building(buildingNum, total_time, executed_time);
        minHeap.insert(building);
        minHeap.buildHeap();
        rbt.insert(building);
    }

}
