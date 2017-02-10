package edu.iastate.cs228.hw4;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Extension of the AbstractCollection class based on a Binary Search Tree.
 * Efficiencies may vary by implementation, but all methods should have at
 * least the worst case runtimes of a standard Tree.
 * 
 * @author Jia Han Tan
 */
public class BinarySearchTree<E extends Comparable<? super E>> extends AbstractCollection<E> {

    /**
     * Member variables to support the tree: - A Node referencing the root of
     * the tree - An int specifying the element count
     */
	private Node<E> root;
	private int size;
	/**
     * Constructs an empty BinarySearchTree
     */
    public BinarySearchTree() {
        // TODO
    	root=null;
    	size=0;
    }

    /**
     * Constructs a new BinarySearchTree whose root is exactly the given Node.
     * (For testing purposes, set the root to the given Node, do not clone it)
     * 
     * @param root
     *            - The root of the new tree
     * @param size
     *            - The number of elements already contained in the new tree
     */
    public BinarySearchTree(Node<E> root, int size) {
        // TODO
    	this.root = root;
    	this.root = deepClone().root;
    	this.size = size;
    }

    /**
     * Adds the given item to the tree if it is not already there.
     * 
     * @return false if item already exists in the tree and true otherwise.
     * @param item
     *            - Item to be added to the tree
     * @throws IllegalArgumentException
     *             - If item is null
     */
    @Override
    public boolean add(E item) throws IllegalArgumentException {
        // TODO
    	return item!=null && add(new Node<E>(item));
    }
    
    // additional methods for add methods
    private boolean add(Node<E> toAdd) throws IllegalArgumentException {
		if (toAdd.getData() == null)
			throw new IllegalArgumentException("item cannot be null");

		boolean added = true;

		if (root == null)
			root = toAdd;
		else
			added = add(toAdd, root);

		if (added)
			size++;

		return added;
	}
    
	private boolean add(Node<E> toAdd, Node<E> node) {
		int comparison = toAdd.getData().compareTo(node.getData());

		if (comparison > 0) {
			if (node.getRight() == null) {
				node.setRight(toAdd);
				balance(node);
				return true;
			}
			else
				return add(toAdd, node.getRight());
		}
		else if (comparison < 0) {
			if (node.getLeft() == null) {
				node.setLeft(toAdd);
				balance(node);
				return true;
			}
			else
				return add(toAdd, node.getLeft());
		}

		return false;
	}
	
	private void balance(Node<E> node) {
		int balance = node.getBalance();

		if (balance < -1) {
			if (node.getRight().getBalance() > 0)
				rotateRight(node.getRight().getLeft());
			rotateLeft(node.getRight());
		}
		else if (balance > 1) {
			if (node.getLeft().getBalance() < 0)
				rotateLeft(node.getLeft().getRight());
			rotateRight(node.getLeft());
		}
		else if (node.getBalance() != 0 && node.getParent() != null)
			balance(node.getParent());
	}
	
	private void rotateLeft(Node<E> newRoot) {
		Node<E> parent = newRoot.getParent();
		Node<E> parent_parent = parent.getParent();

		if (parent_parent != null)
			parent_parent.replace(parent, newRoot);
		else {
			newRoot.setParent(null);
			root = newRoot;
		}

		parent.setRight(newRoot.getLeft());
		newRoot.setLeft(parent);
	}

	private void rotateRight(Node<E> newRoot) {
		Node<E> parent = newRoot.getParent();
		Node<E> parent_parent = parent.getParent();

		if (parent_parent != null)
			parent_parent.replace(parent, newRoot);
		else {
			newRoot.setParent(null);
			root = newRoot;
		}

		parent.setLeft(newRoot.getRight());
		newRoot.setRight(parent);
	}
	
    
    /**
     * Removes the given item from the tree if it is there. Because the item 
     * is an Object it will need to be cast to an E type. To verify that this
     * is a safe cast, compare its class to the class of the root Node's data.
     * 
     * @return false if the list is empty or item does not exist in the tree,
     *         true otherwise
     * @param item
     *            - The item to be removed from the tree
     */
	//@SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object item) {
        // TODO
    	if (root == null || item.getClass() != root.getData().getClass())
			return false;

		Node<E> node = getNode((E) item);

		if (node == null)
			return false;

		if (node == root) {
			if (node.getLeft() == null && node.getRight() == null)
				root = null;
			else if (node.getLeft() == null)
				root = node.getRight();
			else if (node.getRight() == null)
				root = node.getLeft();
			else {
				Node<E> successor = getSuccessor(node);

				successor.getParent().replace(successor, successor.getLeft());
				successor.setLeft(node.getLeft());
				successor.setRight(node.getRight());
				root = successor;
			}

			if (root != null)
				root.setParent(null);
		}
		else {
			if (node.getLeft() == null && node.getRight() == null)
				node.getParent().replace(node, null);
			else if (node.getLeft() == null)
				node.getParent().replace(node, node.getRight());
			else if (node.getRight() == null)
				node.getParent().replace(node, node.getLeft());
			else {
				Node<E> successor = getSuccessor(node);

				successor.getParent().replace(successor, successor.getLeft());
				successor.setLeft(node.getLeft());
				successor.setRight(node.getRight());
				node.getParent().replace(node, successor);
			}
		}

		size--;
		return true;
    }
	// additional method
	private Node<E> getSuccessor(Node<E> node) {
		node = node.getLeft();

		while (node.getRight() != null)
			node = node.getRight();

		return node;
	}

    /**
     * Retrieves data of the Node in the tree that contains item. i.e. the data
     * such that Node.data.equals(item) is true
     * 
     * @return null if item does not exist in the tree, otherwise the data
     *         stored at the Node that meets the condition above.
     * @param item
     *            - The item to be retrieved
     */
    public E get(E item) {
        // TODO
        return item == null ? null : get(item, root);
    }
    // additional method
	private E get(E item, Node<E> node) {
		if (node == null)
			return null;

		int comparison = item.compareTo(node.getData());

		if (comparison > 0)
			return get(item, node.getRight());
		else if (comparison < 0)
			return get(item, node.getLeft());
		else
			return node.getData();
	}

    /**
     * Tests whether or not item exists in the tree. i.e. this should only
     * return true if a Node exists in the tree such that Node.data.equals(item)
     * is true
     * 
     * @return false if item does not exist in the tree, otherwise true
     * @param item
     *            - The item check
     */
	//@SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object item) {
        // TODO
		return root != null && item != null && item.getClass() == (root.getData().getClass()) && get((E) item) != null;
    }

    /**
     * Removes all elements from the tree
     */
    @Override
    public void clear() {
        // TODO
    	root = null;
    	size = 0;
    }

    /**
     * Tests whether or not the tree contains any elements.
     * 
     * @return false if the tree contains at least one element, true otherwise.
     */
    @Override
    public boolean isEmpty() {
        // TODO
        return size == 0;
    }

    /**
     * Retrieves the number of elements in the tree.
     */
    @Override
    public int size() {
        // TODO
        return size;
    }

    /**
     * Returns a new BSTIterator instance.
     */
    @Override
    public Iterator<E> iterator() {
        // TODO
        return new BSTIterator();
    }

    /**
     * Returns an ArrayList containing all elements in the tree in the order
     * given by a preorder traversal of the tree.
     * 
     * @return an ArrayList of elements from the traversal.
     */
    public ArrayList<E> getPreorderTraversal() {
        // TODO
		ArrayList<E> traversal = new ArrayList<E>(size);
		traversePreorder(traversal, root);
		return traversal;    
	}
    // additional method
    private void traversePreorder(ArrayList<E> traversal, Node<E> node) {
		traversal.add(node.getData());

		if (node.getLeft() != null)
			traversePreorder(traversal, node.getLeft());

		if (node.getRight() != null)
			traversePreorder(traversal, node.getRight());
	}
    /**
     * Returns an ArrayList containing all elements in the tree in the order
     * given by a postorder traversal of the tree.
     * 
     * @return an ArrayList of elements from the traversal.
     */
    public ArrayList<E> getPostOrderTraversal() {
        // TODO
    	ArrayList<E> traversal = new ArrayList<E>(size);
		traversePostorder(traversal, root);
		return traversal;
    }
    // additional method
    private void traversePostorder(ArrayList<E> traversal, Node<E> node) {
		if (node.getLeft() != null)
			traversePostorder(traversal, node.getLeft());

		if (node.getRight() != null)
			traversePostorder(traversal, node.getRight());

		traversal.add(node.getData());
	}

    /**
     * Returns an ArrayList containing all elements in the tree in the order
     * given by a inorder traversal of the tree.
     * 
     * @return an ArrayList of elements from the traversal.
     */
    public ArrayList<E> getInorderTraversal() {
        // TODO
    	ArrayList<E> traversal = new ArrayList<E>(size);
		traverseInorder(traversal, root);
		return traversal;
	}
    // additional method
    private void traverseInorder(ArrayList<E> traversal, Node<E> node) {
		if (node.getLeft() != null)
			traverseInorder(traversal, node.getLeft());

		traversal.add(node.getData());

		if (node.getRight() != null)
			traverseInorder(traversal, node.getRight());
	}
    
    // additional 2 methods (getNode)
    private Node<E> getNode(E item) {
		return getNode(item, root);
	}
	private Node<E> getNode(E item, Node<E> node) {
		int comparison = item.compareTo(node.getData());

		if (comparison > 0) {
			if (node.getRight() != null)
				return getNode(item, node.getRight());
			else
				return null;
		}
		else {
			if (item.equals(node.getData()))
				return node;

			if (node.getLeft() != null)
				return getNode(item, node.getLeft());
			else
				return null;
		}
	}


    /**
     * Implementation of the Iterator interface which returns elements in the
     * order of an inorder traversal using Nodes predecessor and successor.
     * 
     * @author Jia Han Tan
     */
    private class BSTIterator implements Iterator<E> {
    	E lastReturned;
    	int index;
    	Node<E> node;
        public BSTIterator() {
            // TODO
        	index=0;
        }

        /**
         * Returns true if more elements exist in the inorder traversal, false
         * otherwise.
         */
        @Override
        public boolean hasNext() {
            // TODO
            return index < size;
        }

        /**
         * Returns the next item in the inorder traversal.
         * 
         * @return the next item in the traversal.
         * @throws IllegalStateException
         *             - if no more elements exist in the traversal.
         */
        @Override
        public E next() throws IllegalStateException {
            // TODO
        	if (!hasNext())
				throw new IllegalStateException("No more items left!");

			if (node == null) {
				node = root;

				while (node.getLeft() != null)
					node = node.getLeft();
			}
			else
				node = node.getSuccessor();

			index++;
			lastReturned = node.getData();
			return lastReturned;
		}

        /**
         * Removes the last item that was returned by calling next().
         * 
         * @throws IllegalStateException
         *             - if next() has not been called yet or remove() is called
         *             multiple times in a row.
         */
        @Override
        public void remove() throws IllegalStateException {
            // TODO
        	if (lastReturned == null)
				throw new IllegalStateException("remove() called twice or next() has not been called");

			Node<E> safe = node.getPredecessor();
			if (safe != null) {
				BinarySearchTree.this.remove(node.getData());
				node = safe.getSuccessor();
			}
			else {
				safe = node.getSuccessor();
				BinarySearchTree.this.remove(node.getData());
				node = safe.getPredecessor();
			}

			lastReturned = null;
        }

    }// BSTIterator
    
    // additional 2 methods
    public BinarySearchTree<E> deepClone() {
		BinarySearchTree<E> clone = new BinarySearchTree<E>();

		clone.root = root;

		if (root != null)
			cloneTraversal(root, clone.root);

		clone.size = size;

		return clone;
	}
	private void cloneTraversal(Node<E> node, Node<E> cloneNode) {
		if (node.getLeft() != null) {
			cloneNode.setLeft(new Node<E>(node.getLeft().getData()));
			cloneTraversal(node.getLeft(), cloneNode.getLeft());
		}

		if (node.getRight() != null) {
			cloneNode.setRight(new Node<E>(node.getRight().getData()));
			cloneTraversal(node.getRight(), cloneNode.getRight());
		}
	}
	
}
