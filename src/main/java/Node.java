public class Node {
    private int index;
    private String info;
    private int parent;
    private int left;

    public Node(int index, String info, int parent, int left) {
        this.index = index;
        this.info = info;
        this.parent = parent;
        this.left = left;
    }

    @Override
    public String toString() {
        return "Node{" +
                "index=" + index +
                ", info='" + info + '\'' +
                ", parent=" + parent +
                ", left=" + left +
                '}';
    }
}
