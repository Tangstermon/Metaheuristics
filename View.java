import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Main GUI
 * @author Anthony
 *
 */

public class View extends JFrame{
	

    
	/**
	 * Constructor 
	 */
	public View(){
		initialise();


	}
	
	/**
	 * Initialises the components for the GUI
	 */
	public void initialise(){
		//Fields and labels declaration
		GraphDraw frame = new GraphDraw("Test Window");

		frame.setSize(400,300);
	
		frame.setVisible(true);

		frame.addNode("a", 50,50);
		frame.addNode("b", 100,100);
		frame.addNode("c", 50, 200);
		frame.addNode("longNode", 200,200);
		frame.addEdge(0,1);
		frame.addEdge(0,2);
		frame.addEdge(2,3);

    

		TabuSearch ts = new TabuSearch();
		GeneticAlg ga = new GeneticAlg();
		GeneticOperators go = new GeneticOperators();
		MemeticAlg ma = new MemeticAlg();
		JPanel panel = new JPanel();
		JLabel graphSize = new JLabel("Graph size: ");
		JTextField gSize = new JTextField(10);
		JLabel tabu = new JLabel("Tabu Search");
		JButton tbRun = new JButton("Run");
		JLabel edges = new JLabel("Edges:");
		JLabel iterations = new JLabel("Iterations: ");
		JTextField iterationsInput = new JTextField(10);
		JTextField edgesInput = new JTextField(10);
		JTextArea tbResults = new JTextArea(2,10);
		tbResults.append("Results: ");
		
		//An action listener to run the algorithm when pressed
		tbRun.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e){
					tbResults.setText("");
					String numberEdges = edgesInput.getText();
					int nE = Integer.parseInt(numberEdges);
					String i = edgesInput.getText();
					int nI = Integer.parseInt(i);

					String graphString = gSize.getText();
					System.out.println(graphString + " aaaa");
					int graphInt = Integer.parseInt(graphString);
					GraphGenerate graph = new GraphGenerate(graphInt);
					graph.setGraph(graph.getGraph());
					

					ArrayList<Point> b = ts.TS(graph, nE, nI);
					tbResults.append("Tabu Search Solution " + b.toString() + " cost: " + ts.getCost(b, graph));
					
				}
		});
		
		
		JLabel genetic = new JLabel("Genetic algrothim");
		JButton gaRun = new JButton("Run");
		JLabel gaEdges = new JLabel("Edges:");
		JTextField gaEdgesInput = new JTextField(10);
		JLabel gen = new JLabel("Gen: ");
		JTextField genInput = new JTextField(10);
		JLabel pop = new JLabel("Pop: ");
		JTextField popInput = new JTextField(10);
		JLabel mutatProb = new JLabel("Mutate rate 1/x: ");
		JTextArea gaResults = new JTextArea(2,10);
		gaResults.append("Results: ");
		JTextField mutateProbInput = new JTextField(10);
		gaRun.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				gaResults.setText("");
				String numberEdges = gaEdgesInput.getText();
				int nE = Integer.parseInt(numberEdges);
				System.out.println(nE);
				String gen = genInput.getText();
				int nG = Integer.parseInt(gen);
				String pop = popInput.getText();
				int nP = Integer.parseInt(pop);
				String mutate = mutateProbInput.getText();
				int nM = Integer.parseInt(mutate);
				String graphString = gSize.getText();
				int graphInt = Integer.parseInt(graphString);
				GraphGenerate graph = new GraphGenerate(graphInt);
				graph.setGraph(graph.getGraph());
				
				ArrayList<Point> b = ga.genetic(nP ,nG, nM ,graph, nE, go);
				gaResults.append("Genetic algorithm Solution " + b.toString() + " cost: " + go.computeFitness(b, graph));
			}
	});
	
		
		JLabel memetic = new JLabel("Memetic algrothim");
		JButton maRun = new JButton("Run");
		JLabel maEdges = new JLabel("Edges:");
		JTextField maEdgesInput = new JTextField(10);
		JLabel maGen = new JLabel("Gen: ");
		JTextField maGenInput = new JTextField(10);
		JLabel maPop = new JLabel("Pop: ");
		JTextField maPopInput = new JTextField(10);
		JLabel maMutate = new JLabel("Mutate rate 1/x: ");
		JTextField maMutateInput = new JTextField(10);
		JTextArea maResults = new JTextArea(2,10);
		maResults.append("Results: ");
		maRun.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				//new NewFrame();
				maResults.setText("");
				String numberEdges = maEdgesInput.getText();
				int nE = Integer.parseInt(numberEdges);
				String gen = maGenInput.getText();
				int nG = Integer.parseInt(gen);
				String pop = maPopInput.getText();
				int nP = Integer.parseInt(pop);
				String mutate = maMutateInput.getText();
				int nM = Integer.parseInt(mutate);
				String graphString = gSize.getText();
				int graphInt = Integer.parseInt(graphString);
				GraphGenerate graph = new GraphGenerate(graphInt);
				graph.setGraph(graph.getGraph());
				//aa.append(Arrays.toString(graph.getGraph()));
				ArrayList<Point> b = ma.memetic(nP ,nG, nM ,graph, nE, go);
				maResults.append("Memetic algorithm Solution " + b.toString() + " cost: " + go.computeFitness(b, graph));

			}
	});
	
		//Laying out the labels and text fields
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 6;
		panel.add(tbResults, c);
		
		c.gridx = 6;
		c.gridy = 3;
		panel.add(gaResults, c);
		
		c.gridx = 6;
		c.gridy = 7;
		panel.add(maResults, c);
		
		c.gridx = 1;
		c.gridy = 0;
		panel.add(tabu, c);
		
		//tabu search
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(10,0,0,0);
		panel.add(tbRun, c);
		
		c.gridx = 0;
		c.gridy = 1;
		panel.add(edges, c);
		
		c.gridx = 1;
		c.gridy = 1;
		panel.add(edgesInput, c);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(iterations, c);
		
		c.gridx = 1;
		c.gridy = 2;
		panel.add(iterationsInput, c);
		
		//genetic
		c.gridx = 1;
		c.gridy = 3;
		panel.add(genetic, c);
		
		c.gridx = 2;
		c.gridy = 3;
		panel.add(gaRun, c);
		
		c.gridx = 0;
		c.gridy = 4;
		panel.add(gaEdges, c);
		
		c.gridx = 1;
		c.gridy = 4;
		panel.add(gaEdgesInput, c);
		
		c.gridx = 0;
		c.gridy = 5;
		panel.add(gen, c);
		
		c.gridx = 1;
		c.gridy = 5;
		panel.add(genInput, c);
		
		c.gridx = 2;
		c.gridy = 5;
		panel.add(pop, c);
		
		c.gridx = 3;
		c.gridy = 5;
		panel.add(popInput, c);
		
		c.gridx = 2;
		c.gridy = 4;
		panel.add(mutatProb, c);
		
		c.gridx = 3;
		c.gridy = 4;
		panel.add(mutateProbInput, c);
		
		//memetic
		c.gridx = 1;
		c.gridy = 7;
		panel.add(memetic, c);
		
		c.gridx = 2;
		c.gridy = 7;
		panel.add(maRun, c);
		
		c.gridx = 0;
		c.gridy = 8;
		panel.add(maEdges, c);
		
		c.gridx = 1;
		c.gridy = 8;
		panel.add(maEdgesInput, c);
		
		c.gridx = 2;
		c.gridy = 8;
		panel.add(maGen, c);
		
		c.gridx = 3; 
		c.gridy = 8;
		panel.add(maGenInput, c);
		
		c.gridx = 0;
		c.gridy = 10;
		panel.add(maPop, c);
		
		c.gridx = 1;
		c.gridy = 10;
		panel.add(maPopInput, c);
		
		c.gridx = 2;
		c.gridy = 10;
		panel.add(maMutate, c);
		
		c.gridx = 3;
		c.gridy = 10;
		panel.add(maMutateInput, c);
	
		c.gridx = 0;
		c.gridy = 11;
		panel.add(graphSize, c);
		
		c.gridx = 1;
		c.gridy = 11;
		panel.add(gSize, c);
				
        
        add(panel);
        
        setTitle("Metaheuristic");
		setSize(800,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	/**
	 * Main method which runs the frame making it visible
	 * @param args
	 */
	public static void main(String[] args){
		
		SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                View view = new View();
                view.setVisible(true);
            }
        });
	}
	
	
}

class GraphDraw extends JFrame {
    int width;
    int height;

    ArrayList<Node> nodes;
    ArrayList<edge> edges;

 public GraphDraw(String name) { //Construct with label
	this.setTitle(name);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	nodes = new ArrayList<Node>();
	edges = new ArrayList<edge>();
	width = 30;
	height = 30;
    }

   class Node {
	int x, y;
	String name;
	
	public Node(String myName, int myX, int myY) {
	    x = myX;
	    y = myY;
	    name = myName;
	}
    }
    
    class edge {
	int i,j;
	
	public edge(int ii, int jj) {
	    i = ii;
	    j = jj;	    
	}
    }

    public void addNode(String name, int x, int y) { 
	//add a node at pixel (x,y)
	nodes.add(new Node(name,x,y));
	this.repaint();
    }
    public void addEdge(int i, int j) {
	//add an edge between nodes i and j
	edges.add(new edge(i,j));
	this.repaint();
    }
    
    public void paint(Graphics g) { // draw the nodes and edges
	FontMetrics f = g.getFontMetrics();
	int nodeHeight = Math.max(height, f.getHeight());

	g.setColor(Color.black);
	for (edge e : edges) {
	    g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
		     nodes.get(e.j).x, nodes.get(e.j).y);
	}

	for (Node n : nodes) {
	    int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
	    g.setColor(Color.white);
	    g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
		       nodeWidth, nodeHeight);
	    g.setColor(Color.black);
	    g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2, 
		       nodeWidth, nodeHeight);
	    
	    g.drawString(n.name, n.x-f.stringWidth(n.name)/2,
			 n.y+f.getHeight()/2);
	}
    }

   }
