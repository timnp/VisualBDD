package model;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JComponent;

/**
 * 
 * @author TimNP
 *
 */
public class VisualObdd extends JComponent{
	/**
	 * default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the abstract OBDD layout
	 */
	private AbstractObddLayout abstractObdd;
	/**
	 * a list of the represented OBDD's nodes' IDs
	 */
	private LinkedList<Integer> nodeIds = new LinkedList<Integer>();
	/**
	 * A HashMap for the represented OBDD's edges:
	 * For each decision node's ID a pair of its high child's ID and its low 
	 * child's ID is stored.
	 */
	private HashMap<Integer,Pair<Integer,Integer>> edgeMap = 
			new HashMap<Integer,Pair<Integer,Integer>>();
	/**
	 * a HashMap with each node's top left corner's absolute positions
	 */
	private HashMap<Integer,Pair<Integer,Integer>> positionMap = 
			new HashMap<Integer,Pair<Integer,Integer>>();
	/**
	 * each node's absolute size
	 */
	private int nodeSize;
	/**
	 * the currently selected OBDD node (if there is one)
	 */
	private OBDD selectedNode = null;
	/**
	 * the second selected OBDD node (e.g. for merging equivalent nodes) 
	 */
	private OBDD secondSelectedNode = null;
	/**
	 * list of the other highlighted nodes 
	 * (e.g. from finding a redundant node)
	 */
	private LinkedList<OBDD> highlightedNodes = new LinkedList<OBDD>();
	
	
	
	/**
	 * getter for the abstract OBDD layout
	 * @return
	 */
	public AbstractObddLayout getAbstractObdd() {
		return abstractObdd;
	}
	
	/**
	 * getter for the represented OBDD
	 * @return
	 */
	public OBDD getObdd() {
		return abstractObdd.getObdd();
	}
	
	/**
	 * getter for the node ID list
	 * @return
	 */
	public LinkedList<Integer> getNodeIds() {
		return nodeIds;
	}
	
	/**
	 * getter for the edge HashMap
	 * @return
	 */
	public HashMap<Integer,Pair<Integer,Integer>> getEdgeMap() {
		return edgeMap;
	}
	
	/**
	 * getter for the position map
	 * @return
	 */
	public HashMap<Integer,Pair<Integer,Integer>> getPositionMap() {
		return positionMap;
	}
	
	/**
	 * getter for the node size
	 * @return
	 */
	public int getNodeSize() {
		return nodeSize;
	}
	
	/**
	 * getter for the (first) selected node
	 * @return
	 */
	public OBDD getSelectedNode() {
		return selectedNode;
	}
	
	/**
	 * setter for the selected node
	 * @param selectedNode
	 */
	public void setSelectedNode(OBDD selectedNode) {
		this.selectedNode = selectedNode;
	}
	
	/**
	 * getter for the second selected node
	 * @return
	 */
	public OBDD getSecondSelectedNode() {
		return secondSelectedNode;
	}
	
	/**
	 * setter for the second selected node
	 * @param secondSelectedNode
	 */
	public void setSecondSelectedNode(OBDD secondSelectedNode) {
		// setting the second selected node only if the given node is null or 
		// different from the first selected node
		if (secondSelectedNode.equals(null) || 
				!selectedNode.equals(secondSelectedNode)) 
			this.secondSelectedNode =selectedNode;
	}
	
	/**
	 * getter for the other highlighted nodes
	 * @return
	 */
	public LinkedList<OBDD> getHighlightedNodes() {
		return highlightedNodes;
	}
	
	/**
	 * setter for the highlighted nodes
	 * @param highlightedNodes
	 */
	public void setHighlightedNodes(LinkedList<OBDD> highlightedNodes) {
		this.highlightedNodes = highlightedNodes;
	}
	
	
	/**
	 * constructor for a visual OBDD
	 * @param abstractObdd - the AbstractObddLayout representing the OBDD
	 * @param panelSize - the OBDD panel's size
	 */
	public VisualObdd(AbstractObddLayout abstractObdd, Dimension panelSize) {
		// setting the represented OBDD
		this.abstractObdd = abstractObdd;
		// adding the represented OBDD's nodes and edges to the respective 
		// lists
		addNodesAndEdges(this.abstractObdd.getObdd());
		// "resizing" the visual OBDD
		updateSize(panelSize);
	}
	
	
	/**
	 * resizes the visual OBDD to the given size, changing the node size and 
	 * their positions
	 * @param panelSize - the OBDD panel's size
	 */
	public void updateSize(Dimension panelSize) {
		// setting the size to the panel's size
		setSize(panelSize);
		// splitting the panel size into width and height
		int panelWidth = panelSize.width;
		int panelHeight = panelSize.height;
		// calculating the absolute node size
		nodeSize = (int) (abstractObdd.getNodeSizeToHeight() * panelHeight);
		// the relative position HashMap
		HashMap<Integer,Pair<Double,Double>> relPositionMap = 
				abstractObdd.getPositionMap();
		// adding the OBDD's nodes's positions
		for (int i : relPositionMap.keySet()) {
			// adding each node's absolute positions to the position map
			positionMap.put(i, absolutePositions(relPositionMap, panelWidth, 
					panelHeight, nodeSize, i));
		}
	}
	
	
	/**
	 * auxiliary method that adds all nodes of a given OBDD to the node ID list
	 * and all its edges to the edge list
	 * @param currentNode
	 */
	private void addNodesAndEdges(OBDD currentNode) {
		// retrieving the node's ID
		int id = currentNode.getId();
		// adding the node's ID to the node ID list if it hasn't been added 
		// before
		if (!nodeIds.contains(currentNode.getId())) {
			nodeIds.add(id);
			// adding the node's outgoing edges and recursively its children if
			// it isn't a terminal
			if (!currentNode.isTerminal()) {
				// retrieving the (decision) node's children
				OBDD highChild = currentNode.getHighChild();
				OBDD lowChild = currentNode.getLowChild();
				// adding the nodes' outgoing edges to the edge list
				edgeMap.put(id, new Pair<Integer,Integer>(highChild.getId(), 
						lowChild.getId()));
				// recursively adding the node's children
				addNodesAndEdges(highChild);
				addNodesAndEdges(lowChild);
			}
		}
	}
	
	
	/**
	 * auxiliary method to turn relative positions into absolute ones
	 * @param positionMap - HashMap with relative positions
	 * @param panelSize - the size of the panel
	 * @param nodeSize - the absolute node size
	 * @param id - the node's ID
	 * @return the node's absolute positions
	 */
	private Pair<Integer, Integer> absolutePositions(
			HashMap<Integer,Pair<Double,Double>> positionMap, 
			int panelWidth, int panelHeight, int nodeSize, int id) {
		// retrieving the node's relative positions
		Pair<Double,Double> relativePositions = positionMap.get(id);
		// calculating the absolute ones
		Integer horizontalPosition = (int) (relativePositions.getFirst() * 
				panelWidth - nodeSize / 2);
		Integer verticalPosition = (int) (relativePositions.getSecond() * 
				panelHeight - nodeSize / 2);
		return new Pair<Integer,Integer>(horizontalPosition,verticalPosition);
	}
	
	
	/**
	 * overriding the paintComponent method
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		// drawing all of the OBDD's nodes
		for (int id : nodeIds) {
			// getting the node's positions
			int horizontalPosition = positionMap.get(id).getFirst();
			int verticalPosition = positionMap.get(id).getSecond();
			if (id > 1) {
				// If the node isn't a terminal, it's represented by a circle.
				g.drawOval(horizontalPosition, verticalPosition, 
						nodeSize, nodeSize);
				// retrieving the node's children
				Pair<Integer,Integer> children = edgeMap.get(id);
				// retrieving the decision node's children's positions
				Pair<Integer,Integer> highChildPosition = 
						positionMap.get(children.getFirst());
				Pair<Integer,Integer> lowChildPosition = 
						positionMap.get(children.getSecond());
				// drawing the node's outgoing edges
				g.drawLine(
						// starting the line at the node's lower left "corner"
						horizontalPosition, 
						verticalPosition + nodeSize, 
						// ending the line at the high child's upper left 
						// "corner"
						highChildPosition.getFirst(), 
						highChildPosition.getSecond());
				g.drawLine(
						// starting the line at the node's lower right "corner"
						horizontalPosition + nodeSize, 
						verticalPosition + nodeSize, 
						// ending the line at the low child's upper right 
						// "corner"
						lowChildPosition.getFirst() + nodeSize, 
						lowChildPosition.getSecond());
			}
			else {
				// If the node is a terminal, it's represented by a square.
				g.drawRect(horizontalPosition, verticalPosition, 
						nodeSize, nodeSize);
			}
		}
	}
	
	
	/**
	 * "unselects" all nodes
	 */
	public void unselect() {
		selectedNode = null;
		secondSelectedNode = null;
	}
	
	
	/**
	 * "unhighlights" all nodes
	 */
	public void unhighlight() {
		// "unselecting" all nodes
		unselect();
		// "unhighlighting" all other nodes
		highlightedNodes.clear();
	}
	
}
