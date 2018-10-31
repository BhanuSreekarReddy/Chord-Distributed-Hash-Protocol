import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class ChordProtocol {

	Map<Integer,Node> chordRing;
	ArrayList<Integer> nodes;
	int n=0;
	int powOf2ToN = 0;
	ChordProtocol(int n)
	{
		this.n = n;
		chordRing = new HashMap<Integer,Node>((int)Math.pow(2,n));
		nodes = new ArrayList<>();
		this.powOf2ToN = (int)Math.pow(2, n);
		
	}
	public Node findSuccessor(int k)
	{
		// we first add or delete the node and then call this function
		
		Collections.sort(nodes);
		// if no nodes in chord ring
		if(chordRing.isEmpty())
		{
			return null;
		}
		
		
		// if there is one node in chord ring
		if(chordRing.size()==1)
		{
			return chordRing.get(nodes.get(0));
		}
		
		// if there are more than one nodes in chord ring
		int index = nodes.indexOf(k);
		if(index==(nodes.size()-1))
			return chordRing.get(nodes.get(0));
		else
			return chordRing.get(nodes.get(index+1));
		
	}
	public Node findPredessor(int k)
	{
		// we first add or delete element and call this function
		
		Collections.sort(nodes);
		// if the chord ring has 0 nodes
		if(chordRing.isEmpty())
		{
			return null;
		}
		// if the chord ring has one node
		if(chordRing.size()==1)
		{
			return null;
		}
		
		
		//if the chord ring has more than one node
		int index = nodes.indexOf(k);
		if(index == 0)
			return chordRing.get(nodes.get(nodes.size()-1));
		else
			return chordRing.get(nodes.get(index-1));
	}
	
	public void join(int i)
	{
		// called when a node joins the ring; initialize successor and finger table
		if(chordRing.isEmpty())
		{
			Node n1 = new Node(i,n);
			chordRing.put(i, n1);
			nodes.add(i);
			n1.successor = n1;
			n1.predessor = null;
			fixFingers();
		}
		
		// chord ring contains only one node
		else if(chordRing.size()==1)
		{
			Node n1 = new Node(i,n);
			chordRing.put(i, n1);
			nodes.add(i);
			n1.successor = findSuccessor(i);
			n1.predessor = findPredessor(i);
			n1.predessor.successor = n1;
			n1.successor.predessor = n1;
			fixFingers();
		}
		else
		{
			// more than one nodes in chord ring
			Node n1 = new Node(i,n);
			chordRing.put(i, n1);
			nodes.add(i);
			n1.successor = findSuccessor(i);
			n1.predessor = findPredessor(i);
			n1.predessor.successor = n1;
			n1.successor.predessor = n1;
			fixFingers();
		}
		
		
	}
	
	public void leave(int i)
	{
		// if no node in chord ring 
		if(chordRing.isEmpty())
		{
			System.out.println("The Chord Ring is Empty");
			return;
		}
		// if the given node is not present in chord ring
		if(!chordRing.containsKey(i))
		{
			System.out.println("Given Node is not present in ring");
			return;
		}
		
		
		//if the node is present and is the only node in ring
		if(chordRing.containsKey(i)&& chordRing.size()==1)
		{
			chordRing.remove(i);
			nodes.remove(nodes.indexOf(i));
			fixFingers();
		}
		
		
		// if the node is present has more than one node in ring
		if(chordRing.containsKey(i) && chordRing.size()>1)
		{
			Node n1 = chordRing.get(i);
			chordRing.remove(i);
			nodes.remove(nodes.indexOf(i));
			n1.predessor.successor = n1.successor;
			n1.successor.predessor = n1.predessor;
			fixFingers();
		}
		
		
	}
	
	public void stabilize()
	{
		// called periodically ..node updates who it thinks its successor is
		fixFingers();
		
	}
		
	public void show(int i)
	{
		System.out.print("Node "+i+": ");
		Node node = chordRing.get(i);
		if(node == null)
		{
			// node doesn't exist
		}
		else
		{
			if(node.successor!=null)
				System.out.print("suc "+node.successor.node_num+", ");
			if(node.predessor==null)
				System.out.print("pre "+"None"+" :finger :");
			else
				System.out.print("pre "+node.predessor.node_num+" :finger :");
			for(int j=0;j<n-1;j++)
			{
				System.out.print(node.fingerTable[j].key_node.node_num+",");
			}
			System.out.print(node.fingerTable[n-1].key_node.node_num);
		}
		System.out.println();
	}
	
	public void notifySuccessor()
	{
		// a node tells its successor that it exists so the successor can update its predessor
		
	}
	
	public void fixFingers()
	{
		Collections.sort(nodes);
		// updates its finger table
		//traverse through all nodes and update corresponding finger table
		for(Map.Entry<Integer, Node> e : chordRing.entrySet())
		{
			
			for(int i=0;i<n;i++)
			{
				int low = (int)Math.pow(2, i);
				int high = (int)Math.pow(2, i+1);
				e.getValue().fingerTable[i].finger = i+1;
				e.getValue().fingerTable[i].lowerBound = ((e.getKey()+low)% powOf2ToN);
				for(int j=0;j<nodes.size();j++)
				{
					if(nodes.get(j)>e.getValue().fingerTable[i].lowerBound)
					{
						e.getValue().fingerTable[i].key_node = chordRing.get(nodes.get(j));
						break;
					}
				}
				if(e.getValue().fingerTable[i].key_node==null || !nodes.contains(e.getValue().fingerTable[i].key_node.node_num))
					e.getValue().fingerTable[i].key_node = chordRing.get(nodes.get(0));
				
				for(int j=0;j<low;j++)
				{
					e.getValue().fingerTable[i].keys[j] = ((e.getKey()+low+j)% powOf2ToN);
				}
				e.getValue().fingerTable[i].upperbound = ((e.getKey()+high-1)% powOf2ToN);
			}
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		if(args.length==2)
		{
			if(Integer.parseInt(args[1])<0)
			{
				System.out.println("ERROR: invalid integer <n>");
				System.exit(0);
			}
			ChordProtocol cp = new ChordProtocol(Integer.parseInt(args[1]));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String st;
			try {
				while((st = br.readLine())!=null)
				{
					String[] command = st.split("\\s+");
					if(command[0].equals("add"))
					{
						// validate input
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: add expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						if(command.length==2 && Integer.parseInt(command[1])>=0 && Integer.parseInt(command[1])<cp.powOf2ToN)
						{
							if(cp.nodes.contains(Integer.parseInt(command[1])))
								System.out.println("ERROR: Node "+command[1]+" exists");
							else
							{
								// call join function
								cp.join(Integer.parseInt(command[1]));
								System.out.println("Added node "+command[1]);
							}
						}
						
					}
					else if(command[0].equals("drop"))
					{
						//validate input
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: drop expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						if(command.length==2 && Integer.parseInt(command[1])>=0 && Integer.parseInt(command[1])<cp.powOf2ToN)
						{
							if(cp.nodes.contains(Integer.parseInt(command[1])))
							{
								// call leave
								cp.leave(Integer.parseInt(command[1]));
								System.out.println("Dropped node "+command[1]);
								
							}
							else
							{
								System.out.println("ERROR: Node "+command[1]+" does not exist");
							}
						}
					}
					else if(command[0].equals("join"))
					{
						if(command.length!=3)
							System.out.println("SYNTAX ERROR: join expects 3 parameters not "+command.length);
						if(command.length==3 && ((Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0) || (Integer.parseInt(command[2])>=cp.powOf2ToN || Integer.parseInt(command[2])<0)))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						else if(command.length==3)
							{
								if(!cp.nodes.contains(Integer.parseInt(command[1])))
									System.out.println("ERROR: Node "+command[1]+" does not exist");
								if(!cp.nodes.contains(Integer.parseInt(command[2])))
									System.out.println("ERROR: Node "+command[2]+" does not exist");
							}
						// no output
					}
					else if(command[0].equals("fix"))
					{
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: fix expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						else if(command.length==2 && !cp.nodes.contains(Integer.parseInt(command[1])))
							System.out.println("ERROR: Node "+command[1]+" does not exist");
						// no output
					}
					else if(command[0].equals("stab"))
					{
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: stab expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						else if(command.length==2 && !cp.nodes.contains(Integer.parseInt(command[1])))
							System.out.println("ERROR: Node "+command[1]+" does not exist");
						
						// no output
						
					}
					else if(command[0].equals("list"))
					{
						if(command.length!=1)
							System.out.println("SYNTAX ERROR: list expects 1 parameters not "+command.length);
						if(command.length==1)
						{
						// printing nodes
						System.out.print("Nodes: ");
						if(!cp.nodes.isEmpty())
						{
						for(int i=0;i<cp.nodes.size()-1;i++)
						{
							System.out.print(cp.nodes.get(i)+", ");
						}
						System.out.print(cp.nodes.get(cp.nodes.size()-1));
						}
						System.out.println();
						}
					}
					else if(command[0].equals("show"))
					{
						//validate input
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: show expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						if(command.length==2 && Integer.parseInt(command[1])>=0 && Integer.parseInt(command[1])<cp.powOf2ToN)
						{
							if(cp.nodes.contains(Integer.parseInt(command[1])))
							{
								// print node details
								cp.show(Integer.parseInt(command[1]));
							}
							else
							{
								System.out.println("ERROR: Node "+command[1]+" does not exist");
							}
						}
					}
					else if(command[0].equals("end"))
					{
						if(command.length!=1)
							System.out.println("SYNTAX ERROR: end expects 1 parameter not "+command.length);
						System.gc();
						System.exit(0);
					}
					else
					{
						
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("File I/O not properly happening");
			}
		
			
		}
		
		
		
		if(args.length == 4)
		{
		if(Integer.parseInt(args[3])<0)
		{
			System.out.println("ERROR: invalid integer <n>");
			System.exit(0);
			
		}
		ChordProtocol cp =new ChordProtocol(Integer.parseInt(args[3]));
		// when input is a file 
		File file = new File(args[2]); // change the argument according to the requirement
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String st;
			try {
				while((st = br.readLine())!=null)
				{
					String[] command = st.split("\\s+");
					if(command[0].equals("add"))
					{
						// validate input
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: add expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						if(command.length==2 && Integer.parseInt(command[1])>=0 && Integer.parseInt(command[1])<cp.powOf2ToN)
						{
							if(cp.nodes.contains(Integer.parseInt(command[1])))
								System.out.println("ERROR: Node "+command[1]+" exists");
							else
							{
								// call join function
								cp.join(Integer.parseInt(command[1]));
								System.out.println("Added node "+command[1]);
							}
						}
						
					}
					else if(command[0].equals("drop"))
					{
						//validate input
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: drop expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						if(command.length==2 && Integer.parseInt(command[1])>=0 && Integer.parseInt(command[1])<cp.powOf2ToN)
						{
							if(cp.nodes.contains(Integer.parseInt(command[1])))
							{
								// call leave
								cp.leave(Integer.parseInt(command[1]));
								System.out.println("Dropped node "+command[1]);
								
							}
							else
							{
								System.out.println("ERROR: Node "+command[1]+" does not exist");
							}
						}
					}
					else if(command[0].equals("join"))
					{
						if(command.length!=3)
							System.out.println("SYNTAX ERROR: join expects 3 parameters not "+command.length);
						if(command.length==3 && ((Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0) || (Integer.parseInt(command[2])>=cp.powOf2ToN || Integer.parseInt(command[2])<0)))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						else if(command.length==3)
						{
							if(!cp.nodes.contains(Integer.parseInt(command[1])))
								System.out.println("ERROR: Node "+command[1]+" does not exist");
							if(!cp.nodes.contains(Integer.parseInt(command[2])))
								System.out.println("ERROR: Node "+command[2]+" does not exist");
						}
						// no output
					}
					else if(command[0].equals("fix"))
					{
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: fix expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						
						if(command.length==2 && !cp.nodes.contains(Integer.parseInt(command[1])))
							System.out.println("ERROR: Node "+command[1]+" does not exist");
						// no output
					}
					else if(command[0].equals("stab"))
					{
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: stab expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						else if(command.length==2 && !cp.nodes.contains(Integer.parseInt(command[1])))
							System.out.println("ERROR: Node "+command[1]+" does not exist");
						// no output
						
					}
					else if(command[0].equals("list"))
					{
						if(command.length!=1)
							System.out.println("SYNTAX ERROR: list expects 1 parameters not "+command.length);
						if(command.length==1)
						{
						// printing nodes
						System.out.print("Nodes: ");
						if(!cp.nodes.isEmpty())
						{	
						for(int i=0;i<cp.nodes.size()-1;i++)
						{
							System.out.print(cp.nodes.get(i)+",");
						}
						System.out.print(cp.nodes.get(cp.nodes.size()-1));
						}
						System.out.println();
						}
					}
					else if(command[0].equals("show"))
					{
						//validate input
						if(command.length!=2)
							System.out.println("SYNTAX ERROR: show expects 2 parameters not "+command.length);
						if(command.length==2 && (Integer.parseInt(command[1])>=cp.powOf2ToN || Integer.parseInt(command[1])<0))
							System.out.println("ERROR: node id must be in [0,"+cp.powOf2ToN+")");
						if(command.length==2 && Integer.parseInt(command[1])>=0 && Integer.parseInt(command[1])<cp.powOf2ToN)
						{
							if(cp.nodes.contains(Integer.parseInt(command[1])))
							{
								// print node details
								cp.show(Integer.parseInt(command[1]));
							}
							else
							{
								System.out.println("ERROR: Node "+command[1]+" does not exist");
							}
						}
					}
					else if(command[0].equals("end"))
					{
						if(command.length!=1)
							System.out.println("SYNTAX ERROR: end expects 1 parameter not "+command.length);
						System.gc();
						System.exit(0);
					}
					else
					{
						
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("File I/O not properly happening");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Given input file not found");
		}
		
		
		}
		// when input is not a file
		
		
	}

}

class Node
{
	int node_num;
	Node successor;
	Node predessor;
	// data structure for finger table
	FingerEntry[] fingerTable;
	Node(int num,int n)
	{
		node_num = num;
		fingerTable = new FingerEntry[n];
		for(int i=0;i<n;i++)
		{
			fingerTable[i] = new FingerEntry((int)Math.pow(2, n));
		}
	}
	Node(int num,Node successor,Node predessor,int n)
	{
		node_num = num;
		this.successor = successor;
		this.predessor = predessor;
		fingerTable = new FingerEntry[n];
		for(int i=0;i<n;i++)
		{
			fingerTable[i] = new FingerEntry((int)Math.pow(2, n));
		}
	}
}
class FingerEntry
{
	int finger;
	int lowerBound,upperbound;
	int[] keys;
	Node key_node;
	FingerEntry(int powOf2ToN)
	{
		keys = new int[powOf2ToN];
		key_node = null;
	}
}