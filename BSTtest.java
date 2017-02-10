package edu.iastate.cs228.hw4;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

/**
 * Some comprehensive tests for node and binary search tree.
 * Please don't just rely on these tests, because I may have missed some special cases
 * Also, lemme know on Blackboard if I missed any special cases, or if there are
 * any issues with the tests.
 * @author Peter Keppeler
 *
 */
public class BSTtest
{
	//some instance variables used
	private BinarySearchTree<Integer> tree1;
	private BinarySearchTree<Integer> tree2;
	private Node<Integer> root;
	private Node<Integer> left;
	private Node<Integer> right;
	private Node<Integer> leftleft;
	private Node<Integer> leftright;
	private Node<Integer> rightleft;
	private Node<Integer> rightright;
	private Iterator<Integer> iter;
	
	//runs before every test
	@Before
	public void setup()
	{
		root = new Node<Integer>((Integer) 90);
		left = new Node<Integer>((Integer) 50);
		right = new Node<Integer>((Integer) 150);
		leftleft = new Node<Integer>((Integer) 20);
		leftright = new Node<Integer>((Integer) 75);
		rightleft = new Node<Integer>((Integer) 95);
		rightright = new Node<Integer>((Integer) 175);
		
		root.setLeft(left);
		left.setParent(root);
		root.setRight(right);
		right.setParent(root);
		
		left.setLeft(leftleft);
		leftleft.setParent(left);
		left.setRight(leftright);
		leftright.setParent(left);
		
		right.setLeft(rightleft);
		rightleft.setParent(right);
		right.setRight(rightright);
		rightright.setParent(right);
		
		
		tree1 = new BinarySearchTree<Integer>();
		
		int[] toAdd = {90,50,150,20,75,95,175};

		for(int i = 0; i < toAdd.length; i++)
		{
			tree1.add(toAdd[i]);
		}
		
		tree2 = new BinarySearchTree<Integer>();
		iter = tree1.iterator();
	}
	
	//normal constructor
	@Test
	public void testConstructor()
	{
		BinarySearchTree<Integer> temp = new BinarySearchTree<Integer>();
		assertEquals(0,temp.size());
	}
	
	//constructor given a root and size
	@Test
	public void testConstructor2()
	{
		BinarySearchTree<Integer> temp = new BinarySearchTree<Integer>(root,7);
		assertEquals(tree1.getInorderTraversal(),temp.getInorderTraversal());
	}
	
	//adding an item already in the tree
	@Test
	public void testAdd1()
	{
		boolean res = tree1.add(75);
		assertEquals(false,res);
	}
	
	//adding a new item to the tree
	@Test
	public void testAdd2()
	{
		tree1.add(69);
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,50,69,75,90,95,150,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//adding a null item should throw IllegalArgumentException
	@Test (expected = IllegalArgumentException.class)
	public void testAdd3()
	{
		tree1.add(null);
	}
	
	//testing inOrderTraversal
	@Test
	public void testInOrder()
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,50,75,90,95,150,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing preOrderTraversal
	@Test
	public void testPreOrder()
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {90,50,20,75,150,95,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getPreorderTraversal());
	}
	
	//testing postOrderTraversal
	@Test
	public void testPostOrder()
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,75,50,95,175,150,90};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getPostOrderTraversal());
	}
	
	//testing size after a series of add methods (in @Before)
	@Test
	public void testSize()
	{
		assertEquals(7,tree1.size());
	}
	
	//testing remove of leaf 1
	@Test
	public void testRemove1()
	{
		tree1.remove(20);
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {50,75,90,95,150,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing remove of leaf 2
	@Test
	public void testRemove2()
	{
		tree1.remove(75);
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,50,90,95,150,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing remove of leaf 3
	@Test
	public void testRemove3()
	{
		tree1.remove(95);
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,50,75,90,150,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing remove of leaf 4
	@Test
	public void testRemove4()
	{
		tree1.remove(175);
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,50,75,90,95,150};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing remove of something with 2 children 1
	@Test
	public void testRemove5()
	{
		tree1.remove(50);
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,75,90,95,150,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing remove of something with 2 children 2
	@Test
	public void testRemove6()
	{
		tree1.remove(150);
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,50,75,90,95,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing remove of root
	@Test
	public void testRemove7()
	{
		tree1.remove(90);
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,50,75,95,150,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing a series of removes
	@Test
	public void testRemove8()
	{
		tree1.remove(150);
		tree1.remove(175);
		tree1.remove(75);
		tree1.remove(50);
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,90,95};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing a remove of an item not in the tree
	@Test
	public void testRemove9()
	{
		assertEquals(false,tree1.remove(69));
	}
	
	//testing for false in remove given a null argument
	@Test
	public void testRemove10()
	{
		assertEquals(false,tree1.remove(null));
	}
	
	//testing remove for item of wrong type
	@Test
	public void testRemove11()
	{
		assertEquals(false,tree1.remove("HELLO"));
	}
	
	//testing size after a series of removes
	@Test
	public void testSize2()
	{
		tree1.remove(150);
		tree1.remove(175);
		tree1.remove(75);
		tree1.remove(50);
		
		assertEquals(3,tree1.size());
	}
	
	//testing get on an item in the tree
	@Test
	public void testGet1()
	{
		Integer res = tree1.get(90);
		
		assertEquals((Integer)90,res);
	}
	
	//testing get on an item not in tree
	@Test
	public void testGet2()
	{
		Integer res = tree1.get(69);
		boolean hats = false;
		
		if(res == null)
		{
			hats = true;
		}
		
		assertEquals(true,hats);
	}
	
	//testing get for NullPointerException
	@Test (expected = NullPointerException.class)
	public void testGet3()
	{
		tree1.get(null);
	}
	
	//testing contains for an item in the tree
	@Test
	public void testContains1()
	{
		assertEquals(true,tree1.contains(75));
	}
	
	//testing contains for an item not in the tree
	@Test
	public void testContains2()
	{
		assertEquals(false,tree1.contains(69));
	}
	
	//testing contains for returning false
	@Test
	public void testContains3()
	{
		assertEquals(false,tree1.contains(null));
	}
	
	//testing contains for an item of wrong type
	@Test
	public void testContains4()
	{
		assertEquals(false,tree1.contains("HELLO"));
	}
	
	//testing clear
	@Test
	public void testClear1()
	{
		tree1.clear();
		assertEquals(0,tree1.size());
	}
	
	//testing isEmpty on non-empty tree
	@Test
	public void testisEmpty1()
	{
		assertEquals(false,tree1.isEmpty());
	}
	
	//testing isEmpty on empty tree
	@Test
	public void testisEmpty2()
	{
		assertEquals(true,tree2.isEmpty());
	}
	
	//testing construction or iterator at right spot
	@Test
	public void testIterConstruct1()
	{
		assertEquals((Integer) 20,iter.next());
	}
	
	//testing hasNext when it is true
	@Test
	public void testIterHasNext1()
	{
		assertEquals(true,iter.hasNext());
	}
	
	//testing hasNext when it is on last element in tree
	@Test
	public void testIterHasNext2()
	{
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		
		assertEquals(false,iter.hasNext());
	}
	
	//testing next on item in tree
	@Test
	public void testIterNext1()
	{
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		assertEquals((Integer)95,iter.next());
	}
	
	//testing next when next was already called on last item
	@Test (expected = IllegalStateException.class)
	public void testIterNext2()
	{
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
	}
	
	//testing remove before a call to next should throw IllegalStateException
	@Test (expected = IllegalStateException.class)
	public void testIterRemove1()
	{
		iter.remove();
	}
	
	//testing remove on a non-leaf node
	@Test
	public void testIterRemove2()
	{
		//removes 50
		iter.next();
		iter.next();
		iter.remove();
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,75,90,95,150,175};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing remove on last item in tree
	@Test
	public void testIterRemove3()
	{
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.next();
		iter.remove();
		
		ArrayList<Integer> res = new ArrayList<Integer>();
		int[] arr = {20,50,75,90,95,150};
		for(int i = 0; i < arr.length; i++)
		{
			res.add(arr[i]);
		}
		
		assertEquals(res,tree1.getInorderTraversal());
	}
	
	//testing getPredecessor for a node at beginning of InOrderTraverse should return null
	@Test
	public void testNodeGetPred1()
	{
		Node<Integer> hats = leftleft.getPredecessor();
		boolean res = false;
		
		if(hats == null)
		{
			res = true;
		}
		
		assertEquals(true,res);
	}
	
	//testing getPredecessor for a node at end of InOrderTraverse
	@Test
	public void testNodeGetPred2()
	{
		Node<Integer> hats = rightright.getPredecessor();
		assertEquals(right,hats);
	}
	
	//testing getPredecessor for root
	@Test
	public void testNodeGetPred3()
	{
		Node<Integer> hats = root.getPredecessor();
		assertEquals(leftright,hats);
	}
	
	//testing getPredecessor for a node at another node
	@Test
	public void testNodeGetPred4()
	{
		Node<Integer> hats = rightleft.getPredecessor();
		assertEquals(root,hats);
	}
	
	//testing getSuccessor for a node at beginning of InOrderTraverse
	@Test
	public void testNodeGetSuc1()
	{
		Node<Integer> hats = leftleft.getSuccessor();
		assertEquals(left,hats);
	}
	
	//testing getSuccessor for a node at end of InOrderTraverse should return null
	@Test
	public void testNodeGetSuc2()
	{
		Node<Integer> hats = rightright.getSuccessor();

		boolean res = false;
		
		if(hats == null)
		{
			res = true;
		}
		
		assertEquals(true,res);
	}
	
	//testing getSuccessor for root
	@Test
	public void testNodeGetSuc3()
	{
		Node<Integer> hats = root.getSuccessor();
		assertEquals(rightleft,hats);
	}
	
	//testing getSuccessor for a node at another node
	@Test
	public void testNodeGetSuc4()
	{
		Node<Integer> hats = rightleft.getSuccessor();
		assertEquals(right,hats);
	}
}
