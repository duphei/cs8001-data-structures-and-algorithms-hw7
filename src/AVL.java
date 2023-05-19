import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Your implementation of an AVL.
 *
 * @author Depei Yu
 * @version 1.0
 * @userid dyu79
 * @GTID 903312858
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data cannot be null.");
        }
        size = 0;
        for (T item : data) {
            add(item);
        }
    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data cannot be null.");
        }
        root = addHelperMethod(data, root);

    }

    /**
     * Helper method for adding data.
     * @param data the data to add
     * @param node the node to add from
     * @return the node to add from with the new node added to the tree.
     **/
    private AVLNode<T> addHelperMethod(T data, AVLNode<T> node) {
        // We have reached a leaf node. Return a new node with the datz
        if (node == null) {
            size += 1;
            return new AVLNode<>(data);
        } else {
            // If the data is less than the data in the node, go left
            if (data.compareTo(node.getData()) < 0) {
                node.setLeft(addHelperMethod(data, node.getLeft()));
            } else if (data.compareTo(node.getData()) > 0) {
                // If the data is greater than the data in the node, go right
                node.setRight(addHelperMethod(data, node.getRight()));
            }
            // If the data is already present in the tree
            if (data.compareTo(node.getData()) == 0) {
                return node;
            }
        }

        // Update heights and balance factors
        recalculateNodeHeightAndBalanceFactor(node);

        // Perform any rotations necessary
        if (node.getBalanceFactor() > 1) {
            if (node.getLeft().getBalanceFactor() < 0) {
                node.setLeft(leftRotation(node.getLeft()));
            }
            node = rightRotation(node);
        } else if (node.getBalanceFactor() < -1) {
            if (node.getRight().getBalanceFactor() > 0) {
                node.setRight(rightRotation(node.getRight()));
            }
            node = leftRotation(node);
        }
        return node;
    }

    /**
     * Recalculates a node's height and balance factor.
     * @param node the node to recalculate
     **/
    private void recalculateNodeHeightAndBalanceFactor(AVLNode<T> node) {
        int leftHeight = 0;
        int rightHeight = 0;
        int maxHeight = 0;

        // Returns -1 height for a null node
        if (node.getLeft() == null) {
            leftHeight = -1;
        } else {
            leftHeight = node.getLeft().getHeight();
        }

        if (node.getRight() == null) {
            rightHeight = -1;
        } else {
            rightHeight = node.getRight().getHeight();
        }

        // Get the max of the two child heights
        if (leftHeight > rightHeight) {
            maxHeight = leftHeight;
        } else {
            maxHeight = rightHeight;
        }

        // Calculate height for node
        node.setHeight(maxHeight + 1);
        // Set balance factor for node
        node.setBalanceFactor(leftHeight - rightHeight);
    }

    /**
     * Performs a left rotation on a node.
     * @param node the node to perform a left rotation on
     * @return the node that has been left rotated.
     **/
    private AVLNode<T> leftRotation(AVLNode<T> node) {
        AVLNode<T> rightChild = node.getRight();
        node.setRight(rightChild.getLeft());
        rightChild.setLeft(node);
        recalculateNodeHeightAndBalanceFactor(node);
        recalculateNodeHeightAndBalanceFactor(rightChild);
        return rightChild;
    }

    /**
     * Performs a right rotation on a node.
     * @param node the node to perform a right rotation on
     * @return the node that has been right rotated.
     **/
    private AVLNode<T> rightRotation(AVLNode<T> node) {
        AVLNode<T> leftChild = node.getLeft();
        node.setLeft(leftChild.getRight());
        leftChild.setRight(node);
        recalculateNodeHeightAndBalanceFactor(node);
        recalculateNodeHeightAndBalanceFactor(leftChild);
        return leftChild;
    }


    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data, NOT successor. As a reminder, rotations can occur
     * after removing the predecessor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data cannot be null.");
        } else {
            if (size == 0) {
                throw new java.util.NoSuchElementException("Data is not in the tree.");
            }
            AVLNode<T> nodeToRemove = nodeRemoved(data, root);
            if (nodeToRemove == null) {
                throw new java.util.NoSuchElementException("The data is not in the tree.");
            }
            T dataToReturn = nodeToRemove.getData();
            root = removeHelperMethod(data, root);
            return dataToReturn;
        }

    }

    /**
     * Helper method for removing data.
     * @param data the data to remove
     * @param node the node to remove from
     * @return the node removed.
     **/
    private AVLNode<T> removeHelperMethod(T data, AVLNode<T> node) {
        // If the node is null, data is not in the tree
        if (node == null) {
            return null;
        } else {
            // If the node to be removed is this node
            if (data.compareTo(node.getData()) == 0) {
                // If the node to be removed has no children
                if ((node.getLeft() == null) && (node.getRight() == null)) {
                    size -= 1;
                    return null;
                }
                // If the node to be removed has one left child
                if ((node.getLeft() != null) && node.getRight() == null) {
                    size -= 1;
                    return node.getLeft();
                }
                // If the node to be removed has one right child
                if ((node.getLeft() == null) && (node.getRight() != null)) {
                    size -= 1;
                    return node.getRight();
                }
                // If the node to be removed has two children
                if ((node.getLeft() != null) && node.getRight() != null) {
                    AVLNode<T> predecessorNode = getPredecessorNode(node.getLeft());
                    node.setData(predecessorNode.getData());
                    node.setLeft(removeHelperMethod(node.getData(), node.getLeft()));

                    // Update heights and balance factors
                    recalculateNodeHeightAndBalanceFactor(node);

                    // Perform any necessary rotations
                    if (node.getBalanceFactor() > 1) {
                        if (node.getLeft().getBalanceFactor() < 0) {
                            node.setLeft(leftRotation(node.getLeft()));
                        }
                        node = rightRotation(node);
                    } else if (node.getBalanceFactor() < -1) {
                        if (node.getRight().getBalanceFactor() > 0) {
                            node.setRight(rightRotation(node.getRight()));
                        }
                        node = leftRotation(node);
                    }
                    return node;
                }
            } else if (data.compareTo(node.getData()) < 0) {
                node.setLeft(removeHelperMethod(data, node.getLeft()));
            } else {
                AVLNode<T> tempNode = removeHelperMethod(data, node.getRight());
                node.setRight(tempNode);
            }
        }
        // Update heights and balance factors
        recalculateNodeHeightAndBalanceFactor(node);

        // Perform any necessary rotations
        if (node.getBalanceFactor() < -1) {
            if (node.getRight().getBalanceFactor() > 0) {
                node.setRight(rightRotation(node.getRight()));
            }
            node = leftRotation(node);
        } else if (node.getBalanceFactor() > 1) {
            if (node.getLeft().getBalanceFactor() < 0) {
                node.setLeft(leftRotation(node.getLeft()));
            }
            node = rightRotation(node);
        }
        return node;
    }

    /**
     * Helper method for getting the node to be removed.
     * @param data the data to remove.
     * @param node the root of the tree to remove from
     * @return the node that would be removed from the tree.
     **/
    private AVLNode<T> nodeRemoved(T data, AVLNode<T> node) {
        // If node is null, data is not in the tree
        if (node == null) {
            throw new java.util.NoSuchElementException("THe data is not in the tree.");
        } else {

            // If the node to be removed is this node
            if (data.compareTo(node.getData()) == 0) {
                return node;
            } else if (data.compareTo(node.getData()) < 0) {
                AVLNode<T> tempNode = nodeRemoved(data, node.getLeft());
                return tempNode;

            } else {
                AVLNode<T> tempNode = nodeRemoved(data, node.getRight());
                return tempNode;
            }
        }
    }

    /**
     * Helper method for getting the predecessor node of a node.
     * @param node the node to get the predecessor of.
     * @return the predecessor node.
     **/
    private AVLNode<T> getPredecessorNode(AVLNode<T> node) {
        // Call this on the left child of the node that you want to get the predecessor of
        AVLNode<T> nodeToReturn;
        // Go right until leaf node is reached
        if (node.getRight() != null) {
            nodeToReturn = getPredecessorNode(node.getRight());
        } else {
            nodeToReturn = node;
        }
        return nodeToReturn;
    }
    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data cannot be null.");
        } else {
            AVLNode<T> tempNode = getHelperMethod(data, root);
            if (tempNode == null) {
                throw new java.util.NoSuchElementException("The data is not in the tree.");
            } else {
                return tempNode.getData();
            }
        }
    }

    /**
     * Helper method for getting a node.
     * @param data the data to get from the tree.
     * @param node the root of the tree to get data from.
     * @return the node containing the data to get.
     **/
    private AVLNode<T> getHelperMethod(T data, AVLNode<T> node) {
        if (node == null) {
            return null;
        } else {
            if (data.compareTo(node.getData()) == 0) {
                return node;
            } else if (data.compareTo(node.getData()) < 0) {
                return getHelperMethod(data, node.getLeft());
            } else {
                return getHelperMethod(data, node.getRight());
            }
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data cannot be null.");
        }
        AVLNode<T> tempNode = getHelperMethod(data, root);
        return tempNode != null;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Should be O(1).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (size == 0) {
            return -1;
        } else {
            return root.getHeight();
        }
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        size = 0;
        root = null;
    }

    /**
     * Returns the data on branches of the tree with the maximum depth. If you
     * encounter multiple branches of maximum depth while traversing, then you
     * should list the remaining data from the left branch first, then the
     * remaining data in the right branch. This is essentially a preorder
     * traversal of the tree, but only of the branches of maximum depth.
     *
     * This must be done recursively.
     *
     * Your list should not have duplicate data, and the data of a branch should be
     * listed in order going from the root to the leaf of that branch.
     *
     * Should run in worst case O(n), but you should not explore branches that
     * do not have maximum depth. You should also not need to traverse branches
     * more than once.
     *
     * Hint: How can you take advantage of the balancing information stored in
     * AVL nodes to discern deep branches?
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * Returns: [10, 5, 2, 1, 0, 7, 8, 9, 15, 20, 25, 30]
     *
     * @return the list of data in branches of maximum depth in preorder
     * traversal order
     */
    public List<T> deepestBranches() {
        List<T> listToReturn = deepestBranchesHelperMethod(root);
        return listToReturn;
    }

    /**
     * Helper method for getting the deepest branches of a tree.
     * @param node the root of the tree to get data from.
     * @return a list containing the deepest branches.
     **/
    private List<T> deepestBranchesHelperMethod(AVLNode<T> node) {
        List<T> listToReturn = new ArrayList<>();
        // Start from root, which you know has the highest height
        // Do a preorder traversal, but only add if the height is exactly 1 less
        if (node != null) {
            listToReturn.add(node.getData());
            if ((node.getLeft() != null) && (node.getLeft().getHeight() == (node.getHeight() - 1))) {
                listToReturn.addAll(deepestBranchesHelperMethod(node.getLeft()));
            }
            if ((node.getRight() != null) && (node.getRight().getHeight() == (node.getHeight() - 1))) {
                listToReturn.addAll(deepestBranchesHelperMethod(node.getRight()));
            }
        }
        return listToReturn;
    }

    /**
     * Returns a sorted list of data that are within the threshold bounds of
     * data1 and data2. That is, the data should be > data1 and < data2.
     *
     * This must be done recursively.
     *
     * Should run in worst case O(n), but this is heavily dependent on the
     * threshold data. You should not explore branches of the tree that do not
     * satisfy the threshold.
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * sortedInBetween(7, 14) returns [8, 9, 10, 13]
     * sortedInBetween(3, 8) returns [4, 5, 6, 7]
     * sortedInBetween(8, 8) returns []
     *
     * @param data1 the smaller data in the threshold
     * @param data2 the larger data in the threshold
     * @return a sorted list of data that is > data1 and < data2
     * @throws IllegalArgumentException if data1 or data2 are null
     * or if data1 > data2
     */
    public List<T> sortedInBetween(T data1, T data2) {
        if (data1 == null) {
            throw new java.lang.IllegalArgumentException("Data1 cannot be null.");
        }

        if (data2 == null) {
            throw new java.lang.IllegalArgumentException("Data2 cannot be null.");
        }

        if (data1.compareTo(data2) > 0) {
            throw new java.lang.IllegalArgumentException("Data1 cannot be greater than Data2.");
        }

        List<T> listToReturn = sortedInBetweenHelperMethod(root, data1, data2);
        return listToReturn;

    }

    /**
     * Helper method for getting data between the threshold bounds of data1 and data2.
     * @param node the root of the tree to get data from.
     * @param data1 the smaller data in the threshold
     * @param data2 the larger data in the threshold
     * @return a list containing sorted data between the two thresholds.
     **/
    private List<T> sortedInBetweenHelperMethod(AVLNode<T> node, T data1, T data2) {
        // Do an inorder traversal, but only add to the list if the data is found between the interval
        ArrayList<T> listToReturn = new ArrayList<>();
        if (node != null) {
            listToReturn.addAll(sortedInBetweenHelperMethod(node.getLeft(), data1, data2));
            if ((data1.compareTo(node.getData()) < 0) && (data2.compareTo(node.getData()) > 0)) {
                listToReturn.add(node.getData());
            }
            listToReturn.addAll(sortedInBetweenHelperMethod(node.getRight(), data1, data2));
        }
        return listToReturn;
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}
