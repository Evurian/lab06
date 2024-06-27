import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class SplayTreeNode<T extends Comparable<T>> {
    T value;
    SplayTreeNode<T> left, right;

    SplayTreeNode(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

}

public class SplayTree<T extends Comparable<T>> {
    private SplayTreeNode<T> root;
    private String preO;

    public SplayTree() {
        this.root = null;
    }
    public SplayTreeNode<T> getRaiz(){
        return root;
    }
    // Rotación a la derecha
    private SplayTreeNode<T> rightRotate(SplayTreeNode<T> x) {
        SplayTreeNode<T> y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    // Rotación a la izquierda
    private SplayTreeNode<T> leftRotate(SplayTreeNode<T> x) {
        SplayTreeNode<T> y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }

    // Operación de splaying
    private SplayTreeNode<T> splay(SplayTreeNode<T> root, T value) {
        if (root == null || root.value.compareTo(value) == 0) {
            return root;
        }

        // Zig-Zig (Left Left)
        if (root.value.compareTo(value) > 0) {
            if (root.left == null) return root;

            if (root.left.value.compareTo(value) > 0) {
                root.left.left = splay(root.left.left, value);
                root = rightRotate(root);
            } else if (root.left.value.compareTo(value) < 0) {
                root.left.right = splay(root.left.right, value);
                if (root.left.right != null) {
                    root.left = leftRotate(root.left);
                }
            }

            return (root.left == null) ? root : rightRotate(root);
        } else { // Zig-Zag (Right Right)
            if (root.right == null) return root;

            if (root.right.value.compareTo(value) < 0) {
                root.right.right = splay(root.right.right, value);
                root = leftRotate(root);
            } else if (root.right.value.compareTo(value) > 0) {
                root.right.left = splay(root.right.left, value);
                if (root.right.left != null) {
                    root.right = rightRotate(root.right);
                }
            }

            return (root.right == null) ? root : leftRotate(root);
        }
    }

    public void insert(T value) {
        if (root == null) {
            root = new SplayTreeNode<>(value);
            return;
        }

        root = splay(root, value);

        if (root.value.compareTo(value) == 0) {
            return;
        }

        SplayTreeNode<T> newNode = new SplayTreeNode<>(value);

        if (root.value.compareTo(value) > 0) {
            newNode.right = root;
            newNode.left = root.left;
            root.left = null;
        } else {
            newNode.left = root;
            newNode.right = root.right;
            root.right = null;
        }

        root = newNode;
    }

    public void delete(T value) {
        if (root == null) return;

        root = splay(root, value);

        if (root.value.compareTo(value) != 0) return;

        if (root.left == null) {
            root = root.right;
        } else {
            SplayTreeNode<T> temp = root;
            root = splay(root.left, value);
            root.right = temp.right;
        }
    }

    public boolean search(T value) {
        root = splay(root, value);
        return root != null && root.value.compareTo(value) == 0;
    }

    public void printTree() {
        printTree(root, "", true);
    }

    private void printTree(SplayTreeNode<T> node, String prefix, boolean isTail) {
        if (node != null) {
            System.out.println(prefix + (isTail ? "└── " : "├── ") + node.value);
            printTree(node.left, prefix + (isTail ? "    " : "│   "), false);
            printTree(node.right, prefix + (isTail ? "    " : "│   "), true);
        }
    }
     public void preOrden(SplayTreeNode<T> nodo) {
        if (nodo != null) {
            preO = preO + "," + nodo.value;
            preOrden(nodo.left);
            preOrden(nodo.right);
        }
    }
    public String getPreo(){
        return preO;
    }
    public void insertarNodoCadena(String cadenaNodos) {
        String[] nodos = cadenaNodos.split(",");
        for (String nodo : nodos) {
            try {
                T value = convertToT(nodo.trim());
                insert(value);
            } catch (Exception e) {
                System.err.println("Error converting value: " + nodo + ". Skipping.");
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private T convertToT(String value) {
        // Aquí asumimos que T es Integer. Esto puede adaptarse para otros tipos.
        if (value instanceof String) {
            return (T) value;
        } else if (Integer.class.isAssignableFrom(((Class<T>)((Object)value).getClass()))) {
            return (T) Integer.valueOf(value);
        }
        throw new IllegalArgumentException("Unsupported type: " + value);
    }
    
    public static void main(String[] args) {
        SplayTree<Integer> splayTree = new SplayTree<>();
        
        splayTree.insert(10);
        splayTree.insert(20);
        splayTree.insert(30);
        splayTree.insert(40);
        splayTree.insert(50);
        splayTree.insert(25);

        splayTree.preOrden(splayTree.root);

        System.out.println("Tree after insertion:");
        splayTree.printTree();

        splayTree.search(30);
        System.out.println("Tree after searching 30:");
        splayTree.printTree();

        splayTree.delete(40);
        System.out.println("Tree after deleting 40:");
        splayTree.printTree();

        splayTree.preOrden(splayTree.root);
        System.out.println(splayTree.preO);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new ArbolBinarioGrafico();
                frame.setSize(400, 400);
                frame.setVisible(true);
            }
        });
    }

    
}
