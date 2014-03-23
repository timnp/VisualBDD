package controller;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

import model.AbstractObddLayout;
import model.OBDD;
import model.Pair;

public class LayoutController {
	private static int HASKELL = 1;
	private static int TIKZ = 2;
	private static int GRAPHVIZ = 3;
	private String tikzStart = "\\documentclass{article}\n\n"
			+ "\\usepackage{amsmath,amssymb}\n"
			+ "\\usepackage{tikz}\n\n"
			+ "\\usepackage[active,tightpage]{preview}\n"
			+ "\\PreviewEnvironment{tikzpicture}\n"
			+ "%\\setlength\\PreviewBorder{2mm}\n\n"
			+ "%%%%\n"
			+ "% TikZ styles.\n"
			+ "\\tikzstyle{BddNode}="
			+ "[circle,draw,inner sep=0pt,minimum size=6mm]\n"
			+ "\\tikzstyle{InvisibleBddNode}="
			+ "[circle,inner sep=0pt,minimum size=10mm]\n"
			+ "\\tikzstyle{TerminalNode}="
			+ "[rectangle,draw,inner sep=0pt,minimum size=6mm]\n"
			+ "\\tikzstyle{yes}=[]\n"
			+ "\\tikzstyle{no}=[dashed]\n\n"
			+ "\\tikzstyle{SmallBddNode}="
			+ "[circle,draw,inner sep=0pt,minimum size=5mm]\n"
			+ "\\tikzstyle{SmallInvisibleBddNode}="
			+ "[circle,inner sep=0pt,minimum size=10mm]\n"
			+ "\\tikzstyle{SmallTerminalNode}="
			+ "[rectangle,draw,inner sep=0pt,minimum size=5mm]\n\n"
			+ "\\begin{document}\n"
			+ "\\pagestyle{empty}\n"
			+ "\\begin{tikzpicture}[scale = 1.0]";
	private String tikzEnd = "\\end{tikzpicture}\n\n"
			+ "\\end{document}";
	
	public void saveAsHaskell(AbstractObddLayout abstractObdd) {
		
	}
	
	
	public void saveAsTikz(AbstractObddLayout abstractObdd) {
		
	}
	
	
	public void saveAsGraphviz(AbstractObddLayout abstractObdd) {
		
	}
	
	
	private void toLayout(AbstractObddLayout abstractObdd, int layout) {
		
	}
	
	
	private Pair<Double,Double> tikzPosition(
			Pair<Double,Double> relativePosition, 
			Pair<Double,Double> totalUnits) {
		return new Pair<Double,Double>(
				relativePosition.getFirst() * totalUnits.getFirst(), 
				-relativePosition.getSecond() * totalUnits.getSecond());
	}
	
	
//	/**
//	 * constructor for a visual OBDD
//	 * @param abstractObdd - the AbstractObddLayout representing the OBDD
//	 * @param panelSize - the OBDD panel's size
//	 * @param drawIsolatedTerminals - states whether the isolated terminal 
//	 * 								  (if there is one) should be drawn
//	 */
//	public VisualObdd(AbstractObddLayout abstractObdd, Dimension panelSize, 
//			boolean drawIsolatedTerminal) {
//		// setting the represented OBDD
//		this.abstractObdd = abstractObdd;
//		// separately adding the terminals' IDs to the node ID list if an 
//		// isolated terminal should be drawn
//		if (drawIsolatedTerminal) {
//			nodeIds.add(0);
//			nodeIds.add(1);
//		}
//		// adding the represented OBDD's nodes and edges to the respective 
//		// lists
//		addNodesAndEdges(this.abstractObdd.getObdd());
//		// "resizing" the visual OBDD
//		updateSize(panelSize);
//	}
//	
//	
//	/**
//	 * resizes the visual OBDD to the given size, changing the node size and 
//	 * their positions
//	 * @param panelSize - the OBDD panel's size
//	 */
//	public void updateSize(Dimension panelSize) {
//		// setting the size to the panel's size
//		setSize(panelSize);
//		// splitting the panel size into width and height
//		int panelWidth = panelSize.width;
//		int panelHeight = panelSize.height;
//		// calculating the absolute node size
//		nodeSize = (int) (abstractObdd.getNodeSizeToHeight() * panelHeight);
//		// the relative position HashMap
//		HashMap<Integer,Pair<Double,Double>> relPositionMap = 
//				abstractObdd.getPositionMap();
//		// adding the OBDD's nodes's positions
//		for (int i : relPositionMap.keySet()) {
//			// adding each node's absolute positions to the position map
//			positionMap.put(i, absolutePositions(relPositionMap, panelWidth, 
//					panelHeight, i));
//		}
//	}
//	
//	
//	/**
//	 * auxiliary method that adds all nodes of a given OBDD to the node ID list
//	 * and all its edges to the edge list
//	 * @param currentNode
//	 */
//	private void addNodesAndEdges(OBDD currentNode) {
//		// retrieving the node's ID
//		int id = currentNode.getId();
//		// adding the node's ID to the node ID list if it hasn't been added 
//		// before
//		if (!nodeIds.contains(id)) {
//			nodeIds.add(id);
//			// adding the node's outgoing edges and recursively its children if
//			// it isn't a terminal
//			if (!currentNode.isTerminal()) {
//				// retrieving the (decision) node's children
//				OBDD highChild = currentNode.getHighChild();
//				OBDD lowChild = currentNode.getLowChild();
//				// adding the nodes' outgoing edges to the edge list
//				edgeMap.put(id, new Pair<Integer,Integer>(highChild.getId(), 
//						lowChild.getId()));
//				// recursively adding the node's children
//				addNodesAndEdges(highChild);
//				addNodesAndEdges(lowChild);
//			}
//		}
//	}
//	
//	
//	/**
//	 * auxiliary method to turn relative positions into absolute ones
//	 * @param positionMap - HashMap with relative positions
//	 * @param panelSize - the size of the panel
//	 * @param id - the node's ID
//	 * @return the node's absolute positions
//	 */
//	private Point absolutePositions(
//			HashMap<Integer,Pair<Double,Double>> positionMap, 
//			int panelWidth, int panelHeight, int id) {
//		// retrieving the node's relative positions
//		Pair<Double,Double> relativePositions = positionMap.get(id);
//		// calculating the absolute ones
//		Integer horizontalPosition = (int) (relativePositions.getFirst() * 
//				panelWidth);
//		Integer verticalPosition = (int) (relativePositions.getSecond() * 
//				panelHeight);
//		return new Point(horizontalPosition,verticalPosition);
//	}
}
