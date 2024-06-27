import java.util.Random;

class SkipListNode<T> {
    T value;
    SkipListNode<T>[] forward;
    
    @SuppressWarnings("unchecked")
    public SkipListNode(int level, T value) {
        forward = new SkipListNode[level + 1];
        this.value = value;
    }
}

public class SkipList<T extends Comparable<T>> {
    private static final int MAX_LEVEL = 16;
    private final SkipListNode<T> header = new SkipListNode<>(MAX_LEVEL, null);
    private final Random random = new Random();
    private int level = 0;

    @SuppressWarnings("unchecked")
    public void insert(T value) {
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL + 1];
        SkipListNode<T> x = header;
        
        for (int i = level; i >= 0; i--) {
            while (x.forward[i] != null && x.forward[i].value.compareTo(value) < 0) {
                x = x.forward[i];
            }
            update[i] = x;
        }

        x = x.forward[0];

        if (x == null || !x.value.equals(value)) {
            int lvl = randomLevel();
            if (lvl > level) {
                for (int i = level + 1; i <= lvl; i++) {
                    update[i] = header;
                }
                level = lvl;
            }

            x = new SkipListNode<>(lvl, value);
            for (int i = 0; i <= lvl; i++) {
                x.forward[i] = update[i].forward[i];
                update[i].forward[i] = x;
            }
        }
    }

    public boolean search(T value) {
        SkipListNode<T> x = header;
        for (int i = level; i >= 0; i--) {
            while (x.forward[i] != null && x.forward[i].value.compareTo(value) < 0) {
                x = x.forward[i];
            }
        }
        x = x.forward[0];
        return x != null && x.value.equals(value);
    }

    @SuppressWarnings("unchecked")
    public void delete(T value) {
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL + 1];
        SkipListNode<T> x = header;
        
        for (int i = level; i >= 0; i--) {
            while (x.forward[i] != null && x.forward[i].value.compareTo(value) < 0) {
                x = x.forward[i];
            }
            update[i] = x;
        }

        x = x.forward[0];

        if (x != null && x.value.equals(value)) {
            for (int i = 0; i <= level; i++) {
                if (update[i].forward[i] != x) {
                    break;
                }
                update[i].forward[i] = x.forward[i];
            }

            while (level > 0 && header.forward[level] == null) {
                level--;
            }
        }
    }

    private int randomLevel() {
        int lvl = 0;
        while (lvl < MAX_LEVEL && random.nextDouble() < 0.5) {
            lvl++;
        }
        return lvl;
    }
    public void printList() {
        System.out.println("Skip List:");
        for (int i = 0; i <= level; i++) {
            SkipListNode<T> node = header.forward[i];
            System.out.print("Level " + i + ": ");
            while (node != null) {
                System.out.print(node.value + " ");
                node = node.forward[i];
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        SkipList<Integer> skipList = new SkipList<>();
        
        skipList.insert(3);
        skipList.insert(6);
        skipList.insert(7);
        skipList.insert(9);
        skipList.insert(12);
        skipList.insert(19);
        skipList.insert(17);
        skipList.insert(26);
        skipList.insert(21);
        skipList.insert(25);
        skipList.printList();

        System.out.println("Search 19: " + skipList.search(19));
        System.out.println("Search 15: " + skipList.search(15));
        skipList.printList();
        
        skipList.delete(19);
        System.out.println("Search 19 after deletion: " + skipList.search(19));
        skipList.printList();

    }
}
