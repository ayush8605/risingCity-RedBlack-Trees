import java.util.ArrayList;

public class MinHeap {

    private ArrayList<Building> list;

    public MinHeap() {

        this.list = new ArrayList<>();
    }

    public void insert(Building building) {

        list.add(building);
        int i = list.size() - 1;
        int parent = parent(i);

        while (parent != i && isSmaller(list.get(i), list.get(parent))) {

            swap(i, parent);
            i = parent;
            parent = parent(i);
        }
    }

    private boolean isSmaller(Building build1, Building build2) {
        if (build1.getExecuted_time() == build2.getExecuted_time()) {
            return (build1.getBuildingNum() < build2.getBuildingNum());
        } else {
            return build1.getExecuted_time() < build2.getExecuted_time();
        }
    }

    public void buildHeap() {

        for (int i = list.size() / 2; i >= 0; i--) {
            minHeapify(i);
        }
    }

    public Building extractMin() {

        if (list.size() == 0) {

            throw new IllegalStateException("MinHeap is EMPTY");
        } else if (list.size() == 1) {

            Building min = list.remove(0);
            return min;
        }

        // remove the last item ,and set it as new root
        Building min = list.get(0);
        Building lastItem = list.remove(list.size() - 1);
        list.set(0, lastItem);

        // bubble-down until heap property is maintained
        minHeapify(0);

        // return min build
        return min;
    }

    private void minHeapify(int i) {

        int left = left(i);
        int right = right(i);
        int smallest = -1;

        // find the smallest build between current node and its children.
        if (left <= list.size() - 1 && list.get(left).getExecuted_time() == list.get(i).getExecuted_time()) {
            smallest = list.get(left).getBuildingNum() < list.get(i).getBuildingNum() ? left : i;
        } else if (left <= list.size() - 1 && list.get(left).getExecuted_time() < list.get(i).getExecuted_time()) {
            smallest = left;
        } else {
            smallest = i;
        }

        if (right <= list.size() - 1 && list.get(right).getExecuted_time() == list.get(smallest).getExecuted_time()) {
            smallest = list.get(right).getBuildingNum() < list.get(smallest).getBuildingNum() ? right : smallest;
        } else if (right <= list.size() - 1 && list.get(right).getExecuted_time() < list.get(smallest).getExecuted_time()) {
            smallest = right;
        }

        // if the smallest build is not the current build then bubble-down it.
        if (smallest != i) {

            swap(i, smallest);
            minHeapify(smallest);
        }
    }

    boolean isEmpty() {

        return list.size() == 0;
    }

    private int right(int i) {

        return 2 * i + 2;
    }

    private int left(int i) {

        return 2 * i + 1;
    }

    private int parent(int i) {

        if (i % 2 == 1) {
            return i / 2;
        }

        return (i - 1) / 2;
    }

    private void swap(int i, int parent) {

        Building temp = list.get(parent);
        list.set(parent, list.get(i));
        list.set(i, temp);
    }

}