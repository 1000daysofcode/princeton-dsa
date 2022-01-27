/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

public class BST {
    private Node root;
    private int count;

    public BST() {
        root = null;
        count = 0;
    }

    public BST(int value) {
        root = new Node(value);
        count = 1;
    }

    public void insert(int item) {
        if (contains(item)) return;
        if (root == null) root = new Node(item);
        insertIn(root, item);
    }

    public void insertIn(Node pointer, int item) {
        if (pointer.value() == item) return;
        if (pointer.value() > item) if (pointer.left != null) insertIn(pointer.left, item);
        else {
            pointer.left = new Node(item);
            count++;
        }
        if (pointer.value() < item) if (pointer.right != null) insertIn(pointer.right, item);
        else {
            pointer.right = new Node(item);
            count++;
        }
    }

    // Difficult
    public Node remove(int item) {
        if (!contains(item)) throw new IllegalArgumentException();
        return findAndRemove(null, root, item, 'n');
    }

    // Difficult
    private Node findAndRemove(Node parent, Node current, int item, char pSide) {
        if (current.value() > item) return findAndRemove(current, current.left, item, 'l');
        if (current.value() < item) return findAndRemove(current, current.right, item, 'r');
        Node nodeToReturn = current;
        // if no children
        if (current.left == null && current.right == null) {
            if (current.value() < parent.value()) parent.right = null;
            else parent.left = null;
            return nodeToReturn;
        }
        // if one child on left
        if (children(current) == 'l') {
            if (pSide == 'l') parent.left = current.left;
            if (pSide == 'r') parent.right = current.left;
            current = null;
            return nodeToReturn;
        }
        // if one child on right
        else if (children(current) == 'r') {
            if (pSide == 'l') parent.left = current.right;
            if (pSide == 'r') parent.right = current.right;
            current = null;
            return nodeToReturn;
        }
        // if two children
        else {
            Node smallestNode = removeSmallest(current, current.right);
            if (pSide == 'l') {
                parent.left = smallestNode;
                smallestNode.left = current.left;
                smallestNode.right = current.right;
            }
            if (pSide == 'r') {
                parent.right = smallestNode;
                smallestNode.left = current.left;
                smallestNode.right = current.right;
            }
            current = null;
            return nodeToReturn;
        }
    }

    private Node removeSmallest(Node parent, Node smallestNode) {
        if (smallestNode.left == null) {
            Node nodeToReturn = smallestNode;
            if (smallestNode.right == null) parent.left = null;
            else parent.left = smallestNode.right;
            return nodeToReturn;
        }
        return removeSmallest(smallestNode, smallestNode.left);
    }

    private char children(Node node) {
        if (node.left != null && node.right == null) return 'l';
        if (node.left == null && node.right != null) return 'r';
        return 'b';
    }

    // Tree traversal
    public Iterable<Integer> inOrder() {
        Stack<Integer> reverseOrder = new Stack<>();
        Node pointer = root;
        if (pointer.right != null) traverseBST(pointer.right, reverseOrder);
        reverseOrder.push(pointer.value());
        if (pointer.left != null) traverseBST(pointer.left, reverseOrder);
        return reverseOrder;
    }

    private void traverseBST(Node node, Stack<Integer> stack) {
        if (node.right != null) traverseBST(node.right, stack);
        stack.push(node.value());
        if (node.left != null) traverseBST(node.left, stack);
    }

    public void preOrder() {
        Node pointer = root;
        System.out.println("Preorder:");
        System.out.print((pointer.value()) + " ");
        if (pointer.right != null) travPreOrder(pointer.right);
        if (pointer.left != null) travPreOrder(pointer.left);
    }

    private void travPreOrder(Node node) {
        System.out.print((node.value()) + " ");
        if (node.right != null) travPreOrder(node.right);
        if (node.left != null) travPreOrder(node.left);
    }

    public void postOrder() {
        Node pointer = root;
        System.out.println("Postorder: ");
        if (pointer.right != null) travPostOrder(pointer.right);
        if (pointer.left != null) travPostOrder(pointer.left);
        System.out.print((pointer.value()));
        System.out.println();
    }

    private void travPostOrder(Node node) {
        if (node.right != null) travPostOrder(node.right);
        if (node.left != null) travPostOrder(node.left);
        System.out.print(node.value() + " ");
    }

    public boolean contains(int item) {
        if (root == null || (root.left == null && root.right == null && root.value() != item))
            return false;
        return search(root, item);
    }

    private boolean search(Node pointer, int item) {
        if (pointer.value() == item) return true;
        if (item < pointer.value())
            if (pointer.left != null) return search(pointer.left, item);
        if (item > pointer.value())
            if (pointer.right != null) return search(pointer.right, item);
        return false;
    }

    private class Node {
        private int val;
        private Node left;
        private Node right;

        public Node(int value) {
            val = value;
        }

        private int value() {
            return val;
        }
    }

    public int size() {
        return count;
    }

    public static void main(String[] args) {
        System.out.println("Adding 6 items to BST");

        BST bst = new BST();

        for (int i = 1; i < 7; i++) bst.insert(i);

        System.out.println("Print added elements in order");

        for (Integer each : bst.inOrder()) System.out.print(each + " ");
        System.out.println();

        System.out.println("Does BST contain 5? " + bst.contains(6));
        System.out.println("Does BST contain 6? " + bst.contains(7));

        System.out.println("Remove 3");
        bst.remove(3);
        System.out.println("Does BST contain 3? " + bst.contains(3));

        System.out.println("Print all elements in order");

        for (int each : bst.inOrder()) System.out.print(each + " ");
        System.out.println();

        System.out.println("Print BST in preorder. From left to right:");
        bst.preOrder();

        System.out.println("\nPrint BST in postorder. From left to right:");
        bst.postOrder();
        System.out.println("Post order looks same as inorder");
    }
}
