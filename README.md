## Informe: Implementación de Skip List en Java
### Integrantes: 
    - Edysson Darwin Quispe Marca
    - Hernan Andy Choquehuanca Zapana
### Introducción

Una Skip List es una estructura de datos probabilística que permite realizar búsquedas, inserciones y eliminaciones en tiempo logarítmico esperado. Es una alternativa eficiente a las estructuras de datos balanceadas como los árboles AVL y los árboles rojos-negros.

### Estructura del Nodo de Skip List

Cada nodo de la Skip List contiene:
- Un valor (`value`).
- Un array de nodos `forward`, que representan los enlaces a los nodos en diferentes niveles.

```java
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
```

### Clase Skip List

La clase `SkipList` maneja la estructura y proporciona métodos para insertar, buscar, eliminar e imprimir la lista de manera formateada.

```java
public class SkipList<T extends Comparable<T>> {
    private static final int MAX_LEVEL = 16;
    private final SkipListNode<T> header = new SkipListNode<>(MAX_LEVEL, null);
    private final Random random = new Random();
    private int level = 0;
```

#### Método para Insertar un Valor

El método `insert` inserta un valor en la Skip List, ajustando los niveles según sea necesario.

```java
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
```

#### Método para Buscar un Valor

El método `search` busca un valor en la Skip List y devuelve `true` si el valor existe.

```java
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
```

#### Método para Eliminar un Valor

El método `delete` elimina un valor de la Skip List, ajustando los enlaces de los nodos según sea necesario.

```java
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
```

#### Método para Generar un Nivel Aleatorio

El método `randomLevel` genera un nivel aleatorio para la inserción de un nuevo nodo.

```java
    private int randomLevel() {
        int lvl = 0;
        while (lvl < MAX_LEVEL && random.nextDouble() < 0.5) {
            lvl++;
        }
        return lvl;
    }
```

#### Método para Imprimir la Lista Formateada

El método `printFormattedList` imprime la Skip List en el formato especificado.

```java
    public void printFormattedList() {
        int maxValue = 0;
        SkipListNode<T> node = header.forward[0];
        while (node != null) {
            maxValue = Math.max(maxValue, (Integer) node.value);
            node = node.forward[0];
        }

        int spacing = String.valueOf(maxValue).length() + 2;
        StringBuilder[] levelStrings = new StringBuilder[level + 1];
        for (int i = 0; i <= level; i++) {
            levelStrings[i] = new StringBuilder();
        }

        node = header.forward[0];
        while (node != null) {
            for (int i = 0; i <= level; i++) {
                if (node.forward.length > i) {
                    levelStrings[i].append(String.format("%" + spacing + "s", node.value));
                } else {
                    levelStrings[i].append(String.format("%" + spacing + "s", ""));
                }
            }
            node = node.forward[0];
        }

        for (int i = level; i >= 0; i--) {
            System.out.println("Level " + i + ":" + levelStrings[i].toString());
        }
    }
}
```

### Ejemplo de Uso

```java
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
    
    System.out.println("Original Skip List:");
    skipList.printFormattedList();

    System.out.println("Search 19: " + skipList.search(19));
    System.out.println("Search 15: " + skipList.search(15));
    
    skipList.delete(19);
    System.out.println("Skip List after deleting 19:");
    skipList.printFormattedList();
}
```

### Conclusión

La implementación de la Skip List en Java proporciona una estructura de datos eficiente para realizar búsquedas, inserciones y eliminaciones en tiempo logarítmico esperado. La capacidad de imprimir la lista en un formato específico facilita la visualización de la estructura interna de la Skip List.


## Informe: Implementación de Splay Tree en Java

### Introducción

Un Splay Tree es una estructura de datos autoajustable que realiza operaciones de búsqueda, inserción y eliminación en tiempo logarítmico amortiguado. Esta estructura garantiza que los elementos frecuentemente accesados se mantengan cerca de la raíz, mejorando la eficiencia de operaciones subsiguientes.

### Estructura del Nodo de Splay Tree

Cada nodo en el Splay Tree contiene:
- Un valor (`value`).
- Un nodo izquierdo (`left`).
- Un nodo derecho (`right`).

```java
class SplayTreeNode<T extends Comparable<T>> {
    T value;
    SplayTreeNode<T> left, right;

    SplayTreeNode(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }
}
```

### Clase Splay Tree

La clase `SplayTree` maneja la estructura y proporciona métodos para insertar, buscar, eliminar e imprimir el árbol. Además, incluye métodos de rotación y splaying.

```java
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
    }
}
```

### Métodos Principales

1. **Rotaciones**

    - `rightRotate`: Realiza una rotación a la derecha.
    - `leftRotate`: Realiza una rotación a la izquierda.

    ```java
    private SplayTreeNode<T> rightRotate(SplayTreeNode<T> x) {
        SplayTreeNode<T> y = x.left;
        x.left = y.right;
        y.right = x;
        return y;
    }

    private SplayTreeNode<T> leftRotate(SplayTreeNode<T> x) {
        SplayTreeNode<T> y = x.right;
        x.right = y.left;
        y.left = x;
        return y;
    }
    ```

2. **Splaying**

    El método `splay` lleva el nodo con el valor dado a la raíz del árbol.

    ```java
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
    ```

3. **Inserción**



    El método `insert` añade un nuevo valor al Splay Tree.

    ```java
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
    ```

4. **Eliminación**

    El método `delete` elimina un valor del Splay Tree.

    ```java
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
    ```

5. **Búsqueda**

    El método `search` busca un valor en el Splay Tree.

    ```java
    public boolean search(T value) {
        root = splay(root, value);
        return root != null && root.value.compareTo(value) == 0;
    }
    ```

6. **Impresión del Árbol**

    El método `printTree` imprime el árbol en un formato legible.

    ```java
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
    ```

7. **Recorrido en Preorden**

    El método `preOrden` recorre el árbol en preorden y almacena los valores en una cadena.

    ```java
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
    ```

8. **Inserción de Nodos desde una Cadena**

    El método `insertarNodoCadena` inserta nodos en el árbol desde una cadena de texto.

    ```java
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
        if (value instanceof String) {
            return (T) value;
        } else if (Integer.class.isAssignableFrom(((Class<T>)((Object)value).getClass()))) {
            return (T) Integer.valueOf(value);
        }
        throw new IllegalArgumentException("Unsupported type: " + value);
    }
    ```

### Ejemplo de Uso

```java
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
}
```

### Conclusión

La implementación del Splay Tree en Java proporciona una estructura de datos eficiente para realizar búsquedas, inserciones y eliminaciones en tiempo logarítmico amortiguado. El Splay Tree asegura que los elementos accedidos frecuentemente se mantengan cerca de la raíz, optimizando el rendimiento para operaciones futuras. La capacidad de imprimir el árbol y realizar recorridos en preorden facilita la visualización y comprensión de la estructura interna del árbol.
