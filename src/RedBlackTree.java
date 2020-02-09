import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class RedBlackTree {

    private final int RED = 0;
    private final int BLACK = 1;

    private class Node {

        Building build;
        int colour = BLACK;
        Node lChild = nil, rChild = nil, parent = nil;

        Node(Building build) {
            this.build = build;
        }
    }

    private final Node nil = new Node(new Building(-1, -1, risingCity.globalTime + 5));
    private Node root = nil;

    List<Building> printTreenInRange(int r1, int r2) {

        Stack<Node> s = new Stack<>();
        List<Building> output = new ArrayList<>();
        Node currentBuilding = root;
        if (root == null)
            return new ArrayList<>();

        // traverse the tree
        while (currentBuilding != null || s.size() > 0) {

            /* Reach the left most Node of the
            curr Node */
            while (currentBuilding != null) {
                /* place pointer to a tree node on
                   the stack before traversing
                  the node's lChild subtree */
                s.push(currentBuilding);
                currentBuilding = currentBuilding.lChild;
            }

            /* Current must be NULL at this point */
            currentBuilding = s.pop();

            if (currentBuilding.build.getBuildingNum() >= r1 && currentBuilding.build.getBuildingNum() <= r2) {
                output.add(currentBuilding.build);
            }


            /* we have visited the node and its
               lChild subtree.  Now, it's right
               subtree's turn */
            currentBuilding = currentBuilding.rChild;
        }
        return output;
    }

    String searchNode(int buildingNum) {
        Node searchedBuilding = findNode(new Node(new Building(buildingNum, 0, 0)), root);
        if (searchedBuilding != null) {
            return "(" + searchedBuilding.build.getBuildingNum() + "," + searchedBuilding.build.getExecuted_time() + "," + searchedBuilding.build.getTotal_time() + ")";
        }
        return null;
    }

    void insert(Building building) {
        Node freshNode = new Node(building);
        Node temp = root;
        if (root == nil) {
            root = freshNode;
            freshNode.colour = BLACK;
            freshNode.parent = nil;
        } else {
            freshNode.colour = RED;
            while (true) {
                if (freshNode.build.getBuildingNum() < temp.build.getBuildingNum()) {
                    if (temp.lChild == nil) {
                        temp.lChild = freshNode;
                        freshNode.parent = temp;
                        break;
                    } else {
                        temp = temp.lChild;
                    }
                } else if (freshNode.build.getBuildingNum() >= temp.build.getBuildingNum()) {
                    if (temp.rChild == nil) {
                        temp.rChild = freshNode;
                        freshNode.parent = temp;
                        break;
                    } else {
                        temp = temp.rChild;
                    }
                }
            }
            balanceTree(freshNode);
        }
    }

    private Node getSmallestInTree(Node subTreeRoot) {
        while (subTreeRoot.lChild != nil) {
            subTreeRoot = subTreeRoot.lChild;
        }
        return subTreeRoot;
    }

    //Method to balance tree if it is unbalanced, takes inserted node as argument
    private void balanceTree(Node unbalancedNode) {
        while (unbalancedNode.parent.colour == RED) {
            Node neighbour = nil;
            if (unbalancedNode.parent == unbalancedNode.parent.parent.lChild) {
                neighbour = unbalancedNode.parent.parent.rChild;

                if (neighbour != nil && neighbour.colour == RED) {
                    unbalancedNode.parent.colour = BLACK;
                    neighbour.colour = BLACK;
                    unbalancedNode.parent.parent.colour = RED;
                    unbalancedNode = unbalancedNode.parent.parent;
                    continue;
                }
                if (unbalancedNode == unbalancedNode.parent.rChild) {
                    //Double rotation needed
                    unbalancedNode = unbalancedNode.parent;
                    moveL(unbalancedNode);
                }
                unbalancedNode.parent.colour = BLACK;
                unbalancedNode.parent.parent.colour = RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                moveR(unbalancedNode.parent.parent);
            } else {
                neighbour = unbalancedNode.parent.parent.lChild;
                if (neighbour != nil && neighbour.colour == RED) {
                    unbalancedNode.parent.colour = BLACK;
                    neighbour.colour = BLACK;
                    unbalancedNode.parent.parent.colour = RED;
                    unbalancedNode = unbalancedNode.parent.parent;
                    continue;
                }
                if (unbalancedNode == unbalancedNode.parent.lChild) {
                    //Double rotation needed
                    unbalancedNode = unbalancedNode.parent;
                    moveR(unbalancedNode);
                }
                unbalancedNode.parent.colour = BLACK;
                unbalancedNode.parent.parent.colour = RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation
                moveL(unbalancedNode.parent.parent);
            }
        }
        root.colour = BLACK;
    }

    // Method for lChild rotation
    private void moveL(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.lChild) {
                node.parent.lChild = node.rChild;
            } else {
                node.parent.rChild = node.rChild;
            }
            node.rChild.parent = node.parent;
            node.parent = node.rChild;
            if (node.rChild.lChild != nil) {
                node.rChild.lChild.parent = node;
            }
            node.rChild = node.rChild.lChild;
            node.parent.lChild = node;
        } else {//Need to rotate root
            Node right = root.rChild;
            root.rChild = right.lChild;
            right.lChild.parent = root;
            root.parent = right;
            right.lChild = root;
            right.parent = nil;
            root = right;
        }
    }

    // Method for rChild rotation
    private void moveR(Node node) {
        if (node.parent != nil) {
            if (node == node.parent.lChild) {
                node.parent.lChild = node.lChild;
            } else {
                node.parent.rChild = node.lChild;
            }

            node.lChild.parent = node.parent;
            node.parent = node.lChild;
            if (node.lChild.rChild != nil) {
                node.lChild.rChild.parent = node;
            }
            node.lChild = node.lChild.rChild;
            node.parent.rChild = node;
        } else {//Need to rotate root
            Node left = root.lChild;
            root.lChild = root.lChild.rChild;
            left.rChild.parent = root;
            root.parent = left;
            left.rChild = root;
            left.parent = nil;
            root = left;
        }
    }

    //Deletion Code .

    private void transplant(Node target, Node with) {
        if (target.parent == nil) {
            root = with;
        } else if (target == target.parent.lChild) {
            target.parent.lChild = with;
        } else
            target.parent.rChild = with;
        with.parent = target.parent;
    }

    void delete(Building building) {
        Node z = new Node(building);
        if ((z = findNode(z, root)) == null) {
            return;
        }
        Node x;
        Node y = z; // temporary reference y
        int y_original_color = y.colour;

        if (z.lChild == nil) {
            x = z.rChild;
            transplant(z, z.rChild);
        } else if (z.rChild == nil) {
            x = z.lChild;
            transplant(z, z.lChild);
        } else {
            y = getSmallestInTree(z.rChild);
            y_original_color = y.colour;
            x = y.rChild;
            if (y.parent == z)
                x.parent = y;
            else {
                transplant(y, y.rChild);
                y.rChild = z.rChild;
                y.rChild.parent = y;
            }
            transplant(z, y);
            y.lChild = z.lChild;
            y.lChild.parent = y;
            y.colour = z.colour;
        }
        if (y_original_color == BLACK)
            delBalance(x);
    }

    private void delBalance(Node x) {
        while (x != root && x.colour == BLACK) {
            if (x == x.parent.lChild) {
                Node w = x.parent.rChild;
                if (w.colour == RED) {
                    w.colour = BLACK;
                    x.parent.colour = RED;
                    moveL(x.parent);
                    w = x.parent.rChild;
                }
                if (w.lChild.colour == BLACK && w.rChild.colour == BLACK) {
                    w.colour = RED;
                    x = x.parent;
                    continue;
                } else if (w.rChild.colour == BLACK) {
                    w.lChild.colour = BLACK;
                    w.colour = RED;
                    moveR(w);
                    w = x.parent.rChild;
                }
                if (w.rChild.colour == RED) {
                    w.colour = x.parent.colour;
                    x.parent.colour = BLACK;
                    w.rChild.colour = BLACK;
                    moveL(x.parent);
                    x = root;
                }
            } else {
                Node w = x.parent.lChild;
                if (w.colour == RED) {
                    w.colour = BLACK;
                    x.parent.colour = RED;
                    moveR(x.parent);
                    w = x.parent.lChild;
                }
                if (w.rChild.colour == BLACK && w.lChild.colour == BLACK) {
                    w.colour = RED;
                    x = x.parent;
                    continue;
                } else if (w.lChild.colour == BLACK) {
                    w.rChild.colour = BLACK;
                    w.colour = RED;
                    moveL(w);
                    w = x.parent.lChild;
                }
                if (w.lChild.colour == RED) {
                    w.colour = x.parent.colour;
                    x.parent.colour = BLACK;
                    w.lChild.colour = BLACK;
                    moveR(x.parent);
                    x = root;
                }
            }
        }
        x.colour = BLACK;
    }

    boolean isEmpty() {
        return root == nil;
    }

    private Node findNode(Node findNode, Node node) {
        if (root == nil) {
            return null;
        }

        if (findNode.build.getBuildingNum() < node.build.getBuildingNum()) {
            if (node.lChild != nil) {
                return findNode(findNode, node.lChild);
            }
        } else if (findNode.build.getBuildingNum() > node.build.getBuildingNum()) {
            if (node.rChild != nil) {
                return findNode(findNode, node.rChild);
            }
        } else if (findNode.build.getBuildingNum() == node.build.getBuildingNum()) {
            return node;
        }
        return null;
    }
}