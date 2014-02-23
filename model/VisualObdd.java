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
//	/**
//	 * the represented OBDD
//	 */
//	private OBDD obdd;
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
//	/**
//	 * list of nodes that have already been painted for the paintComponent 
//	 * method 
//	 */
//	private LinkedList<Integer> paintedNodes;
	
	
	
//	/**
//	 * getter for the represented OBDD
//	 * @return
//	 */
//	public OBDD getObdd() {
//		return obdd;
//	}
	
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
	 * constructor for a visual OBDD
	 * @param layout - the AbstractObddLayout representing the OBDD
	 * @param panelSize - the OBDD panel's size
	 */
	public VisualObdd(AbstractObddLayout layout, Dimension panelSize) {
		// setting the preferred size to the panel's size
		setPreferredSize(panelSize);
		// adding the represented OBDD's nodes and edges to the respective 
		// lists
		addNodesAndEdges(layout.getObdd());
		// splitting the panel size into width and height
		int panelWidth = panelSize.width;
		int panelHeight = panelSize.height;
		// calculating the absolute node size
		nodeSize = (int) (layout.getNodeSizeToHeight() * panelHeight);
		// the relative position HashMap
		HashMap<Integer,Pair<Double,Double>> relPositionMap = 
				layout.getPositionMap();
		// adding the OBDD's nodes
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
		// adding the node's ID to the node ID list
		nodeIds.add(id);
		// going further if the node isn't a terminal and it hasn't been added 
		// already
		if (!(currentNode.isTerminal() || nodeIds.contains(currentNode))) {
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
//		// clearing the list of painted nodes
//		paintedNodes.clear();
//		// calling the recursive algorithm
//		paintComponentRec(g, obdd);
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
						// ending the line at the high child's upper right 
						// "corner"
						highChildPosition.getFirst() + nodeSize, 
						highChildPosition.getSecond());
				g.drawLine(
						// starting the line at the node's lower right "corner"
						horizontalPosition + nodeSize, 
						verticalPosition + nodeSize, 
						// ending the line at the low child's upper left 
						// "corner"
						lowChildPosition.getFirst(), 
						lowChildPosition.getSecond());
			}
			else {
				// If the node is a terminal, it's represented by a square.
				g.drawRect(horizontalPosition, verticalPosition, 
						nodeSize, nodeSize);
			}
		}
	}
	
	
//	/**
//	 * method that paints an OBDD from the given node down
//	 * @param currentNode
//	 */
//	private void paintComponentRec(Graphics g, OBDD currentNode) {
//		// retrieving the node's ID
//		int id = currentNode.getId();
//		// The node only gets painted, if it hasn't been painted already.
//		if (!paintedNodes.contains(id)) {
//			// getting the node's positions
//			int horizontalPosition = positionMap.get(id).getFirst();
//			int verticalPosition = positionMap.get(id).getSecond();
//			if (currentNode.isTerminal()) {
//				// If the node is a terminal, it's represented by a square.
//				g.drawRect(horizontalPosition, verticalPosition, 
//						nodeSize, nodeSize);
//			}
//			else {
//				// If the node isn't a terminal, it's represented by a circle.
//				g.drawOval(horizontalPosition, verticalPosition, 
//						nodeSize, nodeSize);
//				// retrieving the node's children
//				OBDD highChild = currentNode.getHighChild();
//				OBDD lowChild = currentNode.getLowChild();
//				// retrieving their positions
//				Pair<Integer,Integer> highChildPositions = 
//						positionMap.get(highChild.getId());
//				Pair<Integer,Integer> lowChildPositions = 
//						positionMap.get(lowChild.getId());
//				// drawing the edge towards the high child
//				g.drawLine(horizontalPosition, 
//						verticalPosition + nodeSize, 
//						highChildPositions.getFirst() + nodeSize, 
//						highChildPositions.getSecond());
//				// drawing the edge towards the low child
//				// TODO
//				// recursively painting the node's children
//				paintComponentRec(g, highChild);
//				paintComponentRec(g, lowChild);
//			}
//		}
//	}
}
