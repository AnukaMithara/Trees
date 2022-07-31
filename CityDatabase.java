import java.util.*;
import java.lang.*;


/*Class for node */
class Node 								
{
	String cityName;					
	double latitude;
	double longitude;	

	Node leftChild;
	Node rightChild;
	

	Node(String cityName,double latitude,double longitude ) 
	{
 		this.cityName = cityName;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String toString()
	{
		return (cityName + " : "+ latitude + "\u00B0 N , " + longitude + "\u00B0 E ") ;
	}
}

			/* Class for Binary Search Tree */
class BinarySearchTree 
{		

	Node rootNode;
	Node prev;
	LinkedList<Node> desOrderList = new LinkedList<Node>();

					/* Method for inser new node */
	void insertNode(String cityName,double latitude,double longitude ) 
	{

		Node newNode = new Node(cityName,latitude,longitude );	//Create a new Node

		if (rootNode == null) 									/*If not rootNode assigned before,
																New Node as the Root Node */		
		{
			rootNode = newNode;
		}
		
		else 
		{
			Node currentNode = rootNode;
			Node parent;
			
			while (true) 
			{
				parent = currentNode;
				
				int compare = cityName.compareTo(currentNode.cityName);		//compare the alphabatical order
				
				if (compare < 0) 								//If new node alphabaticaly minimum than parent Node
				{
					currentNode = currentNode.leftChild;		//Go to the left child
					
					if (currentNode == null) 					//If left child has no children,
					{				
						parent.leftChild = newNode;				//new node assign as the left child	
						return;
					}
				}
				else if(compare > 0	)							//If new node alphabaticaly maximum than parent Node				
				{ 
					currentNode = currentNode.rightChild;		//Go to the right child
					
					if (currentNode == null)					//If right child has no children,
					{
						parent.rightChild = newNode;			//new node assign as the right child	
						return; 
					}
				}				
				else						/*If new node city name equal to current node city name
											not make a new node */
				{
					break;
				}
			}
		}
	}
	
		/* Method for search a Node */
	Node search(String cityName) 
	{
		Node currentNode = rootNode;
		
		while (currentNode.cityName != cityName)	//Compare with input value and searching node 
		{
			int compare = cityName.compareTo(currentNode.cityName);  //compare the alphabatical order
			
			if (compare < 0) 
			{
				currentNode = currentNode.leftChild;
			} 
			else 
			{
				currentNode = currentNode.rightChild;
			}
			if (currentNode == null)
			{
				
					return null;				//exit
			}
		}
		return currentNode;
	}

	

	/* Method for remove a Node */
	void remove(String cityName)
	{
		Node currentNode = rootNode;					// Start at the top of the tree
		Node parent = rootNode;

		boolean isItALeftChild = true;

		/* Search for the node to remove */
		while (currentNode.cityName != cityName) 
		{
			parent = currentNode;

			int compare = cityName.compareTo(currentNode.cityName);  //compare the alphabatical order

			if (compare < 0) {					//Go to the left child
				isItALeftChild = true;
				currentNode = currentNode.leftChild;
			} 
			else if(compare > 0) {				//Go to the right child

				isItALeftChild = false;	
				currentNode = currentNode.rightChild;
			}
			else
			{
				break;
			}

			if (currentNode == null)				// If there is no node to remove, exit and print a message
			{
			System.out.println("The node was not found.");
			return;
			}

		}
	/*----------------------------------------------------------------------------------*/
		/* If node haven't childrens, then delete it */

		if (currentNode.leftChild == null && currentNode.rightChild == null) {

			/*If node is assigned before a root node */
			if (currentNode == rootNode)
				rootNode = null;

			/*If node is assigned before a parent child */
			else if (isItALeftChild)
				parent.leftChild = null;

			else
				parent.rightChild = null;
		}

		/*If only left childs*/
		else if (currentNode.rightChild == null) {

			if (currentNode == rootNode)
				rootNode = currentNode.leftChild;

			else if (isItALeftChild)
				parent.leftChild = currentNode.leftChild;	

			else
				parent.rightChild = currentNode.leftChild;

		}

		/*If only right childs*/
		else if (currentNode.leftChild == null) {

			if (currentNode == rootNode)
				rootNode = currentNode.rightChild;

			else if (isItALeftChild)
				parent.leftChild = currentNode.rightChild;	

			else
				parent.rightChild = currentNode.rightChild;

		}

	/*----------------------------------------------------------------------------------*/

		/* After deleting node, replace new nodes*/
		else {
			Node replacementParent = currentNode;
			Node replacement = currentNode;
	
			Node focusNode = currentNode.rightChild;
	
			// While there are no more left children	
			while (focusNode != null) {
	
				replacementParent = replacement;	
				replacement = focusNode;	
				focusNode = focusNode.leftChild;	
			}

			if (replacement != currentNode.rightChild) {
	
				replacementParent.leftChild = replacement.rightChild;
				replacement.rightChild = currentNode.rightChild;	
			}		

			if (currentNode == rootNode) //If replacing node is root node
			{
				rootNode = replacement;
			}

			else if (isItALeftChild)	//If replacing node is left child
				parent.leftChild = replacement;

			else						//If replacing node is right child
				parent.rightChild = replacement;

			replacement.leftChild = currentNode.leftChild;
		}
		/*If successfully complete the steps and remove thea node, print a message*/
		System.out.println("The Node was successfully removed.");
	}

	/*----------------------------------------------------------------------------------*/

	/* Method for get deccending order of BST into linked list */
	void decInorder(Node currentNode)
		{
			if(currentNode != null)
			{
				decInorder(currentNode.rightChild);
				desOrderList.add(currentNode);				
				decInorder(currentNode.leftChild);
			}
		}

	/* Method for print the deccending order Linked list */
	void printDecOrder()
	{
		int i=-1;			
		do	
		{	
			i++;
			System.out.print(desOrderList.get(i).cityName + "  " );			
			
		}while(desOrderList.get(i) != desOrderList.getLast());	
		System.out.println();		
	}

	/*Method for get all cities within a given distance of a specified point */
	void distance(String cityName, int dis) 
	{
		Node pointNode = search(cityName);	//find the point node

		int i=-1;	
		
		do	
		{	
			i++;

			double earthRadius = 6378.1370D;		//Earth radius in kilometers
			double degToRad = (Math.PI/180D);		//Convert degree to radian

			//Calculate the difference between the latitude of the point and the latitude of the checking city
			double difLat =  (pointNode.latitude - desOrderList.get(i).latitude) * degToRad; 	
			//Calculate the difference between the longitude of the point and the longitude of the checking city
			double difLon =  (pointNode.longitude - desOrderList.get(i).longitude) * degToRad;

			//Calculate the angle between the two points
			double a = Math.pow(Math.sin(difLat / 2D), 2D) + Math.cos(pointNode.latitude * degToRad) * Math.cos(desOrderList.get(i).latitude * degToRad) * Math.pow(Math.sin(difLon / 2D), 2D);
                
			double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
			
			//Calculate the arclenght between the two points
			double distance = earthRadius * c;
			
			//If the distance between the two points is less than the given distance, print the city name
			if((distance < dis) && (desOrderList.get(i).cityName != cityName))
				{
					System.out.print(desOrderList.get(i).cityName + "  " );
				}		
			
		}while(desOrderList.get(i) != desOrderList.getLast());			

	}		
}

public class CityDatabase 
{	
	public static void main(String args[] )
	{		   
		BinarySearchTree bst = new BinarySearchTree();
		
		/* Insert Elements to BST */
		bst.insertNode("Colombo", 6.927079, 79.861244);
		bst.insertNode("Chicago", 41.881832, -87.623177);
		bst.insertNode("Sydney", -33.865143, 151.209900);
		bst.insertNode("Killinochchi", 9.385708, 80.406108);
		bst.insertNode("Puttalam", 8.098283, 80.008775);
		bst.insertNode("Chongqing", 29.439325, 106.887703);
		bst.insertNode("Chennai", 13.082680, 80.270718);
		bst.insertNode("Kolkata", 22.572645, 88.363892);
		bst.insertNode("Tokyo", 35.6176, 139.7766);
		
		/*Delete element by name*/
		System.out.print("\n Remove \"Chongqing\" : ");		
		bst.remove("Chongqing");

		/*Search element by name*/
		System.out.print("\nSearched element : ");
		System.out.println(bst.search("Killinochchi"));				

		/*Print city records in descending order by their city name */
		System.out.print("\n Descending order by their city name : \n\t");
		bst.decInorder(bst.rootNode);
		bst.printDecOrder();

		/*Print all cities within a given distance of a specified point */
		System.out.print("\n All cities within 5000km from Killinochchi : \n\t");
		bst.distance("Killinochchi", 5000) ;
	
	}
}