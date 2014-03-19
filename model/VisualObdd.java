package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
	 * a HashMap with each node's center's absolute positions
	 */
	private HashMap<Integer,Point> positionMap = new HashMap<Integer,Point>();
	/**
	 * each node's absolute size
	 */
	private int nodeSize;
	/**
	 * the currently dragged node (if there is one)
	 */
	private OBDD draggedNode = null;
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
	 * colors for the selected and highlighted nodes
	 */
	private Color selectedColor = Color.RED;
	private Color secondSelectedColor = Color.ORANGE;
	private Color highlightedColor = Color.YELLOW;
	
	
	
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
	public HashMap<Integer,Point> getPositionMap() {
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
					panelHeight, i));
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
	 * @param id - the node's ID
	 * @return the node's absolute positions
	 */
	private Point absolutePositions(
			HashMap<Integer,Pair<Double,Double>> positionMap, 
			int panelWidth, int panelHeight, int id) {
		// retrieving the node's relative positions
		Pair<Double,Double> relativePositions = positionMap.get(id);
		// calculating the absolute ones
		Integer horizontalPosition = (int) (relativePositions.getFirst() * 
				panelWidth);
		Integer verticalPosition = (int) (relativePositions.getSecond() * 
				panelHeight);
		return new Point(horizontalPosition,verticalPosition);
	}
	
	
	/**
	 * overriding the paintComponent method
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		// casting the graphics object to graphics2D
		// (Stroking isn't possible for graphics objects.)
		Graphics2D g2 = (Graphics2D) g;
		// retrieving the graphics2D object's half font size
		int halfFontSize = g2.getFont().getSize()/2;
		// drawing all of the OBDD's nodes
		for (int id : nodeIds) {
			// getting the node's positions
			int horizontalPosition = positionMap.get(id).x;
			int verticalPosition = positionMap.get(id).y;
			if (id > 1) {
				// If the node isn't a terminal, it's represented by a circle.
				g2.drawOval(horizontalPosition - nodeSize/2, 
						verticalPosition - nodeSize/2, nodeSize, nodeSize);
				// coloring the node if it's selected or highlighted
				if (isHighlighted(id)) {
					g2.setColor(highlightedColor);
					g2.fillOval(horizontalPosition - nodeSize/2, 
						verticalPosition - nodeSize/2, nodeSize, nodeSize);
					g2.setColor(Color.BLACK);
				}
				if ((secondSelectedNode != null) && 
						(secondSelectedNode.getId() == id)) {
					g2.setColor(secondSelectedColor);
					g2.fillOval(horizontalPosition - nodeSize/2, 
						verticalPosition - nodeSize/2, nodeSize, nodeSize);
					g2.setColor(Color.BLACK);
				}
				if (isSelected(id)) {
					g2.setColor(selectedColor);
					g2.fillOval(horizontalPosition - nodeSize / 2,
							verticalPosition - nodeSize / 2, nodeSize, nodeSize);
					g2.setColor(Color.BLACK);
				}
				// writing the node's variable into the circle
				g2.drawString("X" + this.getObdd().getNode(id).getVar(), 
						horizontalPosition - halfFontSize, 
						verticalPosition + halfFontSize);
				// retrieving the node's children
				Pair<Integer,Integer> children = edgeMap.get(id);
				// retrieving the decision node's children's positions
				Point highChildPosition = 
						positionMap.get(children.getFirst());
				Point lowChildPosition = 
						positionMap.get(children.getSecond());
				// drawing the node's outgoing edges
				g2.drawLine(
						// starting the line at the node's lower left "corner"
						horizontalPosition - nodeSize/2, 
						verticalPosition + nodeSize/2, 
						// ending the line at the high child's upper middle
						highChildPosition.x, 
						highChildPosition.y - nodeSize/2);
				// setting the stroke for the edge to the low child
				g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, 
						BasicStroke.JOIN_MITER, 10.0f, new float[] {10.0f}, 
						0.0f));
				g2.drawLine(
						// starting the line at the node's lower right "corner"
						horizontalPosition + nodeSize/2, 
						verticalPosition + nodeSize/2, 
						// ending the line at the low child's upper middle
						lowChildPosition.x, 
						lowChildPosition.y - nodeSize/2);
				// resetting the stroke
				g2.setStroke(new BasicStroke());
			}
			else {
				// If the node is a terminal, it's represented by a square.
				g2.drawRect(horizontalPosition - nodeSize/2, 
						verticalPosition - nodeSize/2, nodeSize, nodeSize);
				// writing the terminal's ID (value) into the square 
				g2.drawString(Integer.toString(id), 
						horizontalPosition - halfFontSize, 
						verticalPosition + halfFontSize);
			}
		}
	}
	
	
	/**
	 * auxiliary method that states whether a node given by its ID is the 
	 * (first) selected node
	 * @param id
	 * @return
	 */
	private boolean isSelected(int id) {
		return (selectedNode != null) && (selectedNode.getId() == id);
	}
	
	
	/**
	 * auxiliary method that states whether a node given by its ID is the 
	 * second selected node
	 * @param id
	 * @return
	 */
	private boolean isSecondSelected(int id) {
		return (secondSelectedNode != null) && (secondSelectedNode.getId() == id);
	}
	
	
	/**
	 * auxiliary method that states whether a node given by its ID is highlighted
	 * @param id - the node's ID
	 * @return
	 */
	private boolean isHighlighted(int id) {
		// checking whether a highlighted node has the given ID
		for (OBDD obdd : highlightedNodes) if (obdd.getId() == id) return true;
		// returning false if no highlighted node has the given ID
		return false;
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
	
	
	/**
	 * If the given point is in the visualization of a node, nodes becomes 
	 * selected and/or unselected, determined by the current situation of 
	 * selected nodes.
	 * @param p - the point
	 */
	public void clickAtPoint(Point p) {
		// retrieving the ID of a node clicked at
		// (Terminals can't be selected.)
		int clickedNodeId = pressedNodeId(p, false);
		if (clickedNodeId > 1) {
			if (isSelected(clickedNodeId)) {
				// If the clicked node is the first selected node and there
				// is no second selected node, the clicked node is 
				// unselected.
				if (secondSelectedNode == null) selectedNode = null;
				else {
					// If the clicked node is the first selected node and 
					// there is a second selected node, the second selected
					// node becomes the first one.
					selectedNode = secondSelectedNode;
					secondSelectedNode = null;
				}
			}
			else {
				// retrieving the clicked node
				OBDD clickedNode = getObdd().getNode(clickedNodeId);
				// If the clicked node is the second selected node, the 
				// first selected node becomes the second one and the 
				// clicked node becomes the first one.
				if (isSecondSelected(clickedNodeId)) {
					secondSelectedNode = selectedNode;
					selectedNode = clickedNode;
				}
				// If the clicked node isn't selected at all and there is a
				// second selected node, the second selected node becomes 
				// the first one and the clicked node becomes the second 
				// one.
				else if (secondSelectedNode != null) {
					selectedNode = secondSelectedNode;
					secondSelectedNode = clickedNode;
				}
				// If the clicked node isn't selected at all and there is a
				// first selected node, the clicked node becomes the second
				// one.
				else if(selectedNode != null)
					secondSelectedNode = clickedNode;
				// If there are no selected nodes, the clicked node becomes
				// the first one.
				else selectedNode = clickedNode;
			}
		}
		// If no decision node was clicked, all nodes are unhighlighted.
		else unhighlight(); 
	}
	
	
	/**
	 * If the given point is in the visualization of a node, that node becomes 
	 * the dragged node.
	 * @param p
	 */
	public void pressAtPoint(Point p) {
		// retrieving the ID of a pressed node
		int pressedNodeId = pressedNodeId(p, true);
		// If a node was pressed, it becomes the dragged node.
		if (pressedNodeId >= 0) 
			draggedNode = getObdd().getNode(pressedNodeId);
	}
	
	
	/**
	 * If there is a dragged node, its horizontal position gets changed to the 
	 * given point's one. Afterwards the dragged node gets reset.
	 * @param p
	 */
	public void releaseAtPoint(Point p) {
		// checking whether there is a dragged node
		if (draggedNode != null) {
			// retrieving the dragged node's ID
			int draggedNodeId = draggedNode.getId();
			// setting the dragged node's position, updating its x coordinate
			positionMap.put(draggedNodeId, 
					new Point(p.x, positionMap.get(draggedNodeId).y));
			// resetting the dragged node
			draggedNode = null;
		}
	}
	
	
	/** 
	 * @param p - the point
	 * @param allowTerminals - a boolean that states whether terminals are 
	 * 						   allowed to be returned here
	 * @return the ID of a node in which's visualization the given point is in;
	 * 		   -1 if no node fulfilling the criteria was found
	 */
	private int pressedNodeId(Point p, boolean allowTerminals) {
		// searching for a node in which's visualization the point is
		for (int id : nodeIds) {
			// retrieving the node's center's position
			Point center = positionMap.get(id);
			// checking whether the node is a decision node and the given point
			// is in its visualization and returning the node's id in that case
			// (if terminals are allowed or the node isn't a terminal)
			if ((allowTerminals || id > 1) && 
					(p.distance(center) <= nodeSize/2.0)) return id;
		}
		// returning -1 if no node was pressed
		return -1;
	}
}
