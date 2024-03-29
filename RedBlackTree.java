import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Red-Black Tree implementation with a Node inner class for representing
 * the nodes of the tree. Currently, this implements a Binary Search Tree that
 * we will turn into a red black tree by modifying the insert functionality.
 * In this activity, we will start with implementing rotations for the binary
 * search tree insert algorithm.
 */
public class RedBlackTree<T extends Comparable<T>> implements RedBlackTreeInterface<T> {

    /**
     * This class represents a node holding a single value within a binary tree.
     */
    protected static class Node<T> {
        public T data;
        public int blackHeight; // NEW CODE
        // The context array stores the context of the node in the tree:
        // - context[0] is the parent reference of the node,
        // - context[1] is the left child reference of the node,
        // - context[2] is the right child reference of the node.
        // The @SupressWarning("unchecked") annotation is used to supress an unchecked
        // cast warning. Java only allows us to instantiate arrays without generic
        // type parameters, so we use this cast here to avoid future casts of the
        // node type's data field.
        @SuppressWarnings("unchecked")
        public Node<T>[] context = (Node<T>[])new Node[3];
        public Node(T data) { 
            this.data = data; 
            blackHeight = 0;
        }
        
        /**
         * @return true when this node has a parent and is the right child of
         * that parent, otherwise return false
         */
        public boolean isRightChild() {
            return context[0] != null && context[0].context[2] == this;
        }

    }

    protected Node<T> root; // reference to root node of tree, null when empty
    protected int size = 0; // the number of values in the tree

    protected void enforceRBTreePropertiesAfterInsert(Node<T> newRedNode) {
        // In the case that the newly inserted node is the root
        if (newRedNode.equals(root)) {
            root.blackHeight = 1;
            return;
        }
        
        // Getting all the relationships we will need
        Node<T> grandparent = newRedNode.context[0].context[0] == null? null:newRedNode.context[0].context[0];
        Node<T> parent = newRedNode.context[0];
        Node<T> uncle;
        if (newRedNode.context[0].isRightChild() && !newRedNode.context[0].equals(root) && grandparent != null) {
            uncle = grandparent.context[1];
        }
        else if (!newRedNode.context[0].isRightChild() && !newRedNode.context[0].equals(root) && grandparent != null) {
            uncle = grandparent.context[2];
        }
        else {
            uncle = null;
        }

        // ----------------------------------------------------------------------------------------------

        // If parent of newly inserted red node is black, then we can just be done
        if (parent.blackHeight == 1) {
            return;
        }

        // If parent of newly inserted red node is red, then we have additional cases to consider, concerning the parent's sibling
        else if (parent.blackHeight == 0) {

            // If the UNCLE is BLACK OR NULL, then we do the suitable rotation and recolor; there are four scenarios
            if (uncle == null || uncle.blackHeight == 1) {
                // Parent is a left child
                if (!parent.isRightChild()) {
                    // Newly inserted node is a right child
                    if (newRedNode.isRightChild()) {
                        this.rotate(newRedNode, parent);
                        grandparent = newRedNode.context[0];
                        parent = newRedNode;
                        newRedNode = newRedNode.context[1];
                    }
                    // Newly inserted node is a left child, or we pushed the above case into this one
                    // newRedNode.blackHeight = 1; // Would not do this, maintains black height of tree, assumes valid RBT
                    parent.blackHeight = 1;
                    grandparent.blackHeight = 0;
                    this.rotate(parent, grandparent);
                }

                // Parent is a right child
                else {
                    // Newly inserted node is a left child
                    if (!newRedNode.isRightChild()) {
                        this.rotate(newRedNode, parent);
                        grandparent = newRedNode.context[0];
                        parent = newRedNode;
                        newRedNode = newRedNode.context[2];
                    }
                    // Newly inserted node is a right child, or we pushed the above case into this one
                    // newRedNode.blackHeight = 1; // Would not do this, maintains black height of tree, assumes valid RBT
                    parent.blackHeight = 1;
                    grandparent.blackHeight = 0;
                    this.rotate(parent, grandparent);
                }
            }

            // If UNCLE is RED, then we simply recolor and check if the problem has been pushed to an upper part of the tree
            else {
                parent.blackHeight = 1;
                uncle.blackHeight = 1;
                // Must check if the grandparent node is not the root node
                if (!grandparent.equals(root) && grandparent != null) {
                    grandparent.blackHeight = 0;
                }

                // It is possible that there is a red-red conflict now with the recoloring of the grandparent
                // We recursively call this function but with the grandparent considered the newly added red node
                // The recursive call should take care of this red-red conflict
                enforceRBTreePropertiesAfterInsert(grandparent);
            }
        }
        root.blackHeight = 1;
    }


    /**
     * Performs a naive insertion into a binary search tree: adding the input
     * data value to a new node in a leaf position within the tree. After  
     * this insertion, no attempt is made to restructure or balance the tree.
     * This tree will not hold null references, nor duplicate data values.
     * @param data to be added into this binary search tree
     * @return true if the value was inserted, false if not
     * @throws NullPointerException when the provided data argument is null
     * @throws IllegalArgumentException when data is already contained in the tree
     */
    public boolean insert(T data) throws NullPointerException, IllegalArgumentException {
        // null references cannot be stored within this tree
        if(data == null) throw new NullPointerException(
                "This RedBlackTree cannot store null references.");

        Node<T> newNode = new Node<>(data);
        if (this.root == null) {
            // add first node to an empty tree
            root = newNode; size++; enforceRBTreePropertiesAfterInsert(root); return true;
        } else {
            // insert into subtree
            Node<T> current = this.root;
            while (true) {
                int compare = newNode.data.compareTo(current.data);
                if (compare == 0) {
                    throw new IllegalArgumentException("This RedBlackTree already contains value " + data.toString());
                } else if (compare < 0) {
                    // insert in left subtree
                    if (current.context[1] == null) {
                        // empty space to insert into
                        current.context[1] = newNode;
                        newNode.context[0] = current;
                        this.size++;
                        enforceRBTreePropertiesAfterInsert(newNode); // NEW CODE
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.context[1];
                    }
                } else {
                    // insert in right subtree
                    if (current.context[2] == null) {
                        // empty space to insert into
                        current.context[2] = newNode;
                        newNode.context[0] = current;
                        this.size++;
                        enforceRBTreePropertiesAfterInsert(newNode); // NEW CODE
                        return true;
                    } else {
                        // no empty space, keep moving down the tree
                        current = current.context[2]; 
                    }
                }
            }
        }
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a
     * right child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this method
     * will throw an IllegalArgumentException.
     * @param child is the node being rotated from child to parent position
     *      (between these two node arguments)
     * @param parent is the node being rotated from parent to child position
     *      (between these two node arguments)
     * @throws IllegalArgumentException when the provided child and parent
     *      node references are not initially (pre-rotation) related that way
     */
    private void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
        if (child == null) { throw new NullPointerException("Provided child node is null"); }
        if (parent == null) { throw new NullPointerException("Provided parent node is null"); }

        boolean childHasRightChild = (child.context[2] != null);
        boolean childHasLeftChild = (child.context[1] != null);

        // Performing a left rotation:
        if (child.isRightChild() && child.context[0] == parent) {
            // System.out.println("Performing left rotation");
            
            // Handling edge case when the subtree/tree resembles a linked list:
            if (!childHasLeftChild || !childHasRightChild) {
                child.context[1] = parent;
                parent.context[2] = null;
            }
            else {
                parent.context[2] = child.context[1];
                child.context[1].context[0] = parent;
                child.context[1] = parent;
            }

            // If parent is not the root:
            if (parent.context[0] != null) {
                // Checking if the parent node is a right child:
                if (parent.isRightChild()) {
                    parent.context[0].context[2] = child;
                }
                else {
                    parent.context[0].context[1] = child;
                }
                child.context[0] = parent.context[0];
            }

            // If parent is the root:
            else {
                child.context[0] = null;
                root = child;
            }
            parent.context[0] = child;
        }

        // Performing a right rotation:
        else if (parent.context[1] == child && child.context[0] == parent) {
            // System.out.println("Performing a right rotation");
            
            // Handling edge case when the subtree/tree resembles a linked list:
            if (!childHasRightChild || !childHasLeftChild) {
                child.context[2] = parent;
                parent.context[1] = null;
            }
            else {
                parent.context[1] = child.context[2];
                child.context[2].context[0] = parent;
                child.context[2] = parent;
            }
            
            // If parent is not the root:
            if (parent.context[0] != null) {
                // checking if the parent node is a left child
                if (!parent.isRightChild()) {
                    parent.context[0].context[1] = child;
                }
                else {
                    parent.context[0].context[2] = child;
                }
                child.context[0] = parent.context[0];
            }

            // If parent is the root:
            else {
                child.context[0] = null;
                root = child;
            }
            parent.context[0] = child;
        }
        else throw new IllegalArgumentException("The given child and parent nodes are not related");
    }

    /**
     * Get the size of the tree (its number of nodes).
     * @return the number of nodes in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Method to check if the tree is empty (does not contain any node).
     * @return true of this.size() return 0, false if this.size() > 0
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Removes the value data from the tree if the tree contains the value.
     * This method will not attempt to rebalance the tree after the removal and
     * should be updated once the tree uses Red-Black Tree insertion.
     * @return true if the value was remove, false if it didn't exist
     * @throws NullPointerException when the provided data argument is null
     * @throws IllegalArgumentException when data is not stored in the tree
     */
    // public boolean remove(T data) throws NullPointerException, IllegalArgumentException {
    //     // null references will not be stored within this tree
    //     if (data == null) {
    //         throw new NullPointerException("This RedBlackTree cannot store null references.");
    //     } else {
    //         Node<T> nodeWithData = this.findNodeWithData(data);
    //         // throw exception if node with data does not exist
    //         if (nodeWithData == null) {
    //             throw new IllegalArgumentException("The following value is not in the tree and cannot be deleted: " + data.toString());
    //         }  
    //         boolean hasRightChild = (nodeWithData.context[2] != null);
    //         boolean hasLeftChild = (nodeWithData.context[1] != null);
    //         if (hasRightChild && hasLeftChild) {
    //             // has 2 children
    //             Node<T> successorNode = this.findMinOfRightSubtree(nodeWithData);
    //             // replace value of node with value of successor node
    //             nodeWithData.data = successorNode.data;
    //             // remove successor node
    //             if (successorNode.context[2] == null) {
    //                 // successor has no children, replace with null
    //                 this.replaceNode(successorNode, null);
    //             } else {
    //                 // successor has a right child, replace successor with its child
    //                 this.replaceNode(successorNode, successorNode.context[2]);
    //             }
    //         } else if (hasRightChild) {
    //             // only right child, replace with right child
    //             this.replaceNode(nodeWithData, nodeWithData.context[2]);
    //         } else if (hasLeftChild) {
    //             // only left child, replace with left child
    //             this.replaceNode(nodeWithData, nodeWithData.context[1]);
    //         } else {
    //             // no children, replace node with a null node
    //             this.replaceNode(nodeWithData, null);
    //         }
    //         this.size--;
    //         return true;
    //     } 
    // }

    public boolean remove(T data) throws NullPointerException, IllegalArgumentException {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This RedBlackTree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNodeWithData(data);
            // throw exception if node with data does not exist
            if (nodeWithData == null) {
                throw new IllegalArgumentException("The following value is not in the tree and cannot be deleted: " + data.toString());
            }  
            boolean hasRightChild = (nodeWithData.context[2] != null);
            boolean hasLeftChild = (nodeWithData.context[1] != null);
            if (hasRightChild && hasLeftChild) {
                // has 2 children => falls into Case 2
                enforceRBTreePropertiesAfterRemoveCase2(nodeWithData);
                
            } else if ((hasRightChild && !hasLeftChild) || (!hasRightChild && hasLeftChild)) {
                // has either only a left child, or only a right child => falls into case 1
                enforceRBTreePropertiesAfterRemoveCase1(nodeWithData);
            } else {
                // no children => falls into Case 0
                enforceRBTreePropertiesAfterRemoveCase0(nodeWithData);
            }
            this.size--;
            return true;
        } 
    }

    //----------------------------------------------------------------------------------------------------------

    /**
     * This method deals with the case when the node to be removed has no children
     */
    protected void enforceRBTreePropertiesAfterRemoveCase0(Node<T> nodeToBeRemoved) {
        // Two cases: the node is red, or black
        // If the node is red or the root, then a trivial BST remove can occur
        if (nodeToBeRemoved.blackHeight == 0 || nodeToBeRemoved.equals(root)) {
            this.replaceNode(nodeToBeRemoved, null);
        }

        // This is where the number of cases increase; node is black, must account for black deficit with double black nodes
        else if (nodeToBeRemoved.blackHeight == 1 || nodeToBeRemoved.blackHeight == 2) {
            // Make node to be removed double black
            nodeToBeRemoved.blackHeight = 2;

            // Get all relationships needed
            Node<T> doubleBlackParent = nodeToBeRemoved.context[0];
            Node<T> doubleBlackSibling = null;
            Node<T> doubleBlackSiblingRightChild = null;
            Node<T> doubleBlackSiblingLeftChild = null;
            if (nodeToBeRemoved.isRightChild()) {
                doubleBlackSibling = nodeToBeRemoved.context[0].context[1];
            }
            else doubleBlackSibling = nodeToBeRemoved.context[0].context[2];

            if (doubleBlackSibling != null) {
                doubleBlackSiblingRightChild = doubleBlackSibling.context[2];
                doubleBlackSiblingLeftChild = doubleBlackSibling.context[1];
            }

            if (doubleBlackSiblingRightChild == null || doubleBlackSiblingLeftChild == null) {
                if (doubleBlackSiblingRightChild == null) {
                    doubleBlackSiblingRightChild = new Node<T>(null);
                    doubleBlackSiblingRightChild.blackHeight = 1;
                }
                if (doubleBlackSiblingLeftChild == null) {
                    doubleBlackSiblingLeftChild = new Node<T>(null);
                    doubleBlackSiblingLeftChild.blackHeight = 1;
                }
            }

            // There are now 3 (technically 3.5) cases to consider in this section

            // Case 1 and Case 3: Double Black Sibling is Black
            if (doubleBlackSibling == null || doubleBlackSibling.blackHeight == 1) {
                // Case 1 Continued: Sibling has at least 1 red child
                if (doubleBlackSiblingRightChild.blackHeight == 0 || doubleBlackSiblingLeftChild.blackHeight == 0) {
                    // Account for Case 1.5:
                    if (!nodeToBeRemoved.isRightChild()) {
                        if (doubleBlackSiblingLeftChild.blackHeight == 0 && doubleBlackSiblingRightChild.blackHeight == 1) {
                            // adjust colors
                            int leftChildOldHeight = doubleBlackSiblingLeftChild.blackHeight;
                            doubleBlackSiblingLeftChild.blackHeight = doubleBlackSibling.blackHeight;
                            doubleBlackSibling.blackHeight = leftChildOldHeight;
                            this.rotate(doubleBlackSiblingLeftChild, doubleBlackSibling);
                        }

                        // Main code
                        int siblingOldHeight = doubleBlackSibling.blackHeight;
                        doubleBlackSibling.blackHeight = doubleBlackParent.blackHeight;
                        doubleBlackParent.blackHeight = siblingOldHeight;
                        this.rotate(doubleBlackSibling, doubleBlackParent);
                        doubleBlackSiblingRightChild.blackHeight = 1;
                        doubleBlackParent.context[1] = null;
                    }
                    else {
                        if (doubleBlackSiblingRightChild.blackHeight == 0 && doubleBlackSiblingLeftChild.blackHeight == 1) {
                            // adjust colors
                            int rightChildOldHeight = doubleBlackSiblingRightChild.blackHeight;
                            doubleBlackSiblingRightChild.blackHeight = doubleBlackSibling.blackHeight;
                            doubleBlackSibling.blackHeight = rightChildOldHeight;
                            this.rotate(doubleBlackSiblingLeftChild, doubleBlackSibling);
                        }

                        // Main code
                        int siblingOldHeight = doubleBlackSibling.blackHeight;
                        doubleBlackSibling.blackHeight = doubleBlackParent.blackHeight;
                        doubleBlackParent.blackHeight = siblingOldHeight;
                        this.rotate(doubleBlackSibling, doubleBlackParent);
                        doubleBlackSiblingLeftChild.blackHeight = 1;
                        doubleBlackParent.context[2] = null;
                    }
                }

                // Case 3 Continued: Sibling has no red children
                else if (doubleBlackSiblingRightChild.blackHeight == 1 && doubleBlackSiblingLeftChild.blackHeight == 1) {
                    doubleBlackSibling.blackHeight = 0;
                    doubleBlackParent.blackHeight++;
                    if (doubleBlackParent.blackHeight == 1) {
                        if (nodeToBeRemoved.isRightChild()) {
                            doubleBlackParent.context[2] = null;
                        }
                        doubleBlackParent.context[1] = null;
                        return; 
                    }
                    else {
                        if (nodeToBeRemoved.isRightChild()) {
                            doubleBlackParent.context[2] = null;
                        }
                        doubleBlackParent.context[1] = null;
                        if (doubleBlackParent.equals(root)) {
                            doubleBlackParent.blackHeight = 1;
                            return;
                        }
                        // Problem moved up further in the tree, must recurse
                        enforceRBTreePropertiesAfterRemoveCase0(doubleBlackParent);
                    }
                }
            }      
            
            // Case 2: Double Black Sibling is red and its children are black
            else if (doubleBlackSibling.blackHeight == 0 && doubleBlackSiblingRightChild.blackHeight == 1 && doubleBlackSiblingLeftChild.blackHeight == 1) {
                int oldSiblingHeight = doubleBlackSibling.blackHeight;
                doubleBlackSibling.blackHeight = doubleBlackParent.blackHeight;
                doubleBlackParent.blackHeight = oldSiblingHeight;
                this.rotate(doubleBlackSibling, doubleBlackParent);
                // at this point we have pushed ourselves into either case 1 or 3
                enforceRBTreePropertiesAfterRemoveCase0(nodeToBeRemoved);
            }
        }
    }

    /**
     * This method deals with the case when the node to be removed has only one child, either right or left
     */
    protected void enforceRBTreePropertiesAfterRemoveCase1(Node<T> nodeToBeRemoved) {
        boolean hasRightChild = (nodeToBeRemoved.context[2] != null);
        if (hasRightChild) {
            this.replaceNode(nodeToBeRemoved, nodeToBeRemoved.context[2]);
            nodeToBeRemoved.context[2].blackHeight = nodeToBeRemoved.blackHeight;
        }
        else {
            this.replaceNode(nodeToBeRemoved, nodeToBeRemoved.context[1]);
            nodeToBeRemoved.context[1].blackHeight = nodeToBeRemoved.blackHeight;
        }
    }

    /**
     * This method deals with the case when the node to be removed has two children
     */
    protected void enforceRBTreePropertiesAfterRemoveCase2(Node<T> nodeToBeRemoved) {
        Node<T> successorNode = this.findMinOfRightSubtree(nodeToBeRemoved);
        // replace value of node with value of successor node
        nodeToBeRemoved.data = successorNode.data;
  
        // remove successor node
        if (successorNode.context[2] == null) {
            // successor node has no children, call case 0 enforce function
            enforceRBTreePropertiesAfterRemoveCase0(successorNode);
        } 
        else {
            // successor has a right child, call case 1 enforce function
            enforceRBTreePropertiesAfterRemoveCase1(successorNode);
        }
        nodeToBeRemoved = null;
    }


    //----------------------------------------------------------------------------------------------------------

    /**
     * Checks whether the tree contains the value *data*.
     * @param data the data value to test for
     * @return true if *data* is in the tree, false if it is not in the tree
     */
    public boolean contains(T data) {
        // null references will not be stored within this tree
        if (data == null) {
            throw new NullPointerException("This RedBlackTree cannot store null references.");
        } else {
            Node<T> nodeWithData = this.findNodeWithData(data);
            // return false if the node is null, true otherwise
            return (nodeWithData != null);
        }
    }

    /**
     * Helper method that will replace a node with a replacement node. The replacement
     * node may be null to remove the node from the tree.
     * @param nodeToReplace the node to replace
     * @param replacementNode the replacement for the node (may be null)
     */
    protected void replaceNode(Node<T> nodeToReplace, Node<T> replacementNode) {
        if (nodeToReplace == null) {
            throw new NullPointerException("Cannot replace null node.");
        }
        if (nodeToReplace.context[0] == null) {
            // we are replacing the root
            if (replacementNode != null)
                replacementNode.context[0] = null;
            this.root = replacementNode;
        } else {
            // set the parent of the replacement node
            if (replacementNode != null)
                replacementNode.context[0] = nodeToReplace.context[0];
            // do we have to attach a new left or right child to our parent?
            if (nodeToReplace.isRightChild()) {
                nodeToReplace.context[0].context[2] = replacementNode;
            } else {
                nodeToReplace.context[0].context[1] = replacementNode;
            }
        }
    }

    /**
     * Helper method that will return the inorder successor of a node with two children.
     * @param node the node to find the successor for
     * @return the node that is the inorder successor of node
     */
    protected Node<T> findMinOfRightSubtree(Node<T> node) {
        if (node.context[1] == null && node.context[2] == null) {
            throw new IllegalArgumentException("Node must have two children");
        }
        // take a step to the right
        Node<T> current = node.context[2];
        while (true) {
            // then go left as often as possible to find the successor
            if (current.context[1] == null) {
                // we found the successor
                return current;
            } else {
                current = current.context[1];
            }
        }
    }

    /**
     * Helper method that will return the node in the tree that contains a specific
     * value. Returns null if there is no node that contains the value.
     * @return the node that contains the data, or null of no such node exists
     */
    protected Node<T> findNodeWithData(T data) {
        Node<T> current = this.root;
        while (current != null) {
            int compare = data.compareTo(current.data);
            if (compare == 0) {
                // we found our value
                return current;
            } else if (compare < 0) {
                // keep looking in the left subtree
                current = current.context[1];
            } else {
                // keep looking in the right subtree
                current = current.context[2];
            }
        }
        // we're at a null node and did not find data, so it's not in the tree
        return null; 
    }

    /**
     * This method performs an inorder traversal of the tree. The string 
     * representations of each data value within this tree are assembled into a
     * comma separated string within brackets (similar to many implementations 
     * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
     * @return string containing the ordered values of this tree (in-order traversal)
     */
    public String toInOrderString() {
        // generate a string of all values of the tree in (ordered) in-order
        // traversal sequence
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            Stack<Node<T>> nodeStack = new Stack<>();
            Node<T> current = this.root;
            while (!nodeStack.isEmpty() || current != null) {
                if (current == null) {
                    Node<T> popped = nodeStack.pop();
                    sb.append(popped.data.toString());
                    if(!nodeStack.isEmpty() || popped.context[2] != null) sb.append(", ");
                    current = popped.context[2];
                } else {
                    nodeStack.add(current);
                    current = current.context[1];
                }
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * This method performs a level order traversal of the tree. The string
     * representations of each data value
     * within this tree are assembled into a comma separated string within
     * brackets (similar to many implementations of java.util.Collection).
     * This method will be helpful as a helper for the debugging and testing
     * of your rotation implementation.
     * @return string containing the values of this tree in level order
     */
    public String toLevelOrderString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ ");
        if (this.root != null) {
            LinkedList<Node<T>> q = new LinkedList<>();
            q.add(this.root);
            while(!q.isEmpty()) {
                Node<T> next = q.removeFirst();
                if(next.context[1] != null) q.add(next.context[1]);
                if(next.context[2] != null) q.add(next.context[2]);
                sb.append(next.data.toString());
                if(!q.isEmpty()) sb.append(", ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public String toString() {
        return "level order: " + this.toLevelOrderString() +
                "\nin order: " + this.toInOrderString();
    }

// ---------------------------------------------------------------------------

    public T search(T data) throws NullPointerException, IllegalArgumentException {
        if (data == null) {
            throw new NullPointerException("Word input is null.");
        }

        Node<T> current = root;
        while (current != null) {
            int comparison = data.compareTo(current.data);
            if (comparison == 0) {
                return current.data;
            } else if (comparison < 0) {
                current = current.context[1];
            } else {
                current = current.context[2];
            }
        }

        throw new IllegalArgumentException("Word does not exist in dictionary.");
    }

    public ArrayList<T> getSubtree(T data) {
        Node<T> startNode = findNodeWithData(data);
        if (startNode == null) {
            return null;
        }
        else {
            ArrayList<T> subtree = new ArrayList<T>();
            getSubtreeHelper(startNode, subtree);
            return subtree;
        }
    }

    private void getSubtreeHelper(Node<T> startNode, ArrayList<T> subtree) {
        if (startNode != null) {
            subtree.add(startNode.data);
            getSubtreeHelper(startNode.context[1], subtree);
            getSubtreeHelper(startNode.context[2], subtree);
        }
    }


    public static void main(String[] args) {
        // Empty   
    }
}