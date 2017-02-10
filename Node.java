package edu.iastate.cs228.hw4;

/**
 * Node class which is used by the BinarySearchTree class to store data.
 * Normally this class would be a private class inside the BST class, it is
 * public for testing purposes. Because of this, most methods and constructors
 * have been implemented. You may add new protected methods, but DO NOT modify
 * already implemented or change their behavior or you risk losing points in
 * tests.
 * 
 * @author Jia Han Tan
 */
public class Node<T> {

    /**
     * Instance variables of the Nodes which are connected to this Node and the
     * data it contains.
     */
    private Node<T> left, right, parent;
    private T data;
    private int level;
    /**
     * Creates a new Node that is disconnected from all others which stores the
     * given data.
     * 
     * @param d
     */
    public Node(T d) {
        this(null, null, null, d);
    }

    /**
     * Creates a new Node that is connected to all the given Nodes and stores
     * the given data.
     * 
     * @param d
     */
    public Node(Node<T> left, Node<T> right, Node<T> parent, T d) {
        this.data = d;
        this.left = left;
        this.right = right;
        this.parent = parent;
        
        level=1;
    }

    public Node<T> getLeft() {
        return this.left;
    }

    public void setLeft(Node<T> newLeft) {
        this.left = newLeft;
        
        if (newLeft != null)
			newLeft.parent = this;
		updateLevel();
    }

    public Node<T> getRight() {
        return this.right;
    }

    public void setRight(Node<T> newRight) {
        this.right = newRight;
        
		if (newRight != null)
			newRight.parent = this;
		updateLevel();
    }

    public Node<T> getParent() {
        return this.parent;
    }
    
    // additional method
    private void updateLevel() {
		int levelCopy = level;

		level = 0;
		if (left != null)
			level = left.level;
		if (right != null && right.level > level)
			level = right.level;

		level++;

		if (levelCopy != level && parent != null)
			parent.updateLevel();
	}

    public void setParent(Node<T> newParent) {
        this.parent = newParent;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T newData) {
        this.data = newData;
    }
    
    // additional 2 method
	protected void replace(Node<T> toReplace, Node<T> replacement) {
		if (left == toReplace)
			setLeft(replacement);
		else
			setRight(replacement);
	}

	protected int getBalance() {
		int left = this.left == null ? 0 : this.left.level;
		int right = this.right == null ? 0 : this.right.level;

		return left - right;
	}


    /**
     * Returns the next Node in an inorder traversal of the BST which contains
     * this Node.
     * 
     * @return the next Node in the traversal.
     */
    public Node<T> getSuccessor() {
        // TODO
    	Node <T> node = null;
    	
    	if(getRight()!=null){
    		node = getRight();
    		while(node.getLeft() != null )
    			node = node.getLeft();
    	}
    	else if (getParent() != null){
    		node = getParent();
    		Node<T> last = this;
    		
    		while (node.getRight() == last){
    			last = node;
    			node = node.getParent();
    			if (node == null)
    				return null;
    		}
    	}
    	return node;
    }

    /**
     * Returns the previous Node in an inorder traversal of the BST which
     * contains this Node.
     * 
     * @return the previous Node in the traversal.
     */
    public Node<T> getPredecessor() {
        // TODO
    	
    	Node<T> node = null;

		if (getLeft() != null){
			node = getLeft();
			while (node.getRight() != null)
				node = node.getRight();
		}
		else if (getParent() != null){
			node = getParent();
			Node<T> last = this;

			while (node.getLeft() == last){
				last = node;
				node = node.getParent();
				if (node == null)
					return null;
			}
		}
		return node;
    }
    
}
