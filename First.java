
import java.io.*; 
import java.util.*; 
 
class Merch
{
	int stock[];			//	0 - s, 1 - m, 2 - l, 3 - c

	public Merch(){
		stock = new int[4];
		stock[0] = 20; stock[1] = 20; stock[2] = 20; stock[3] = 20;
	}
	public void printstock()
	{
		System.out.println("Current stock:");
		System.out.println("Tshirt - Small = "+stock[0]);
		System.out.println("Tshirt - Medium = "+stock[1]);
		System.out.println("Tshirt - Large = "+stock[2]);
		System.out.println("Caps = "+stock[3]+"\n");
	}
}
class Order extends Thread
{
	int id, num, type;
	Merch m;
	public Order(int i,int ord, int t, Merch mch){
		id = i;
		num = ord;
		type = t;
		m = mch;
	}
	public void run(){
		try{
			synchronized(m){
				if(m.stock[type] < num){
					System.out.println("Not enough stock available");
					System.out.println("Order "+id+" FAILED.");
				}
				else {
					m.stock[type]-=num;
					// this.sleep(2000);
					System.out.println("Order "+id+" SUCCESS.");
				}
				m.printstock();
			}
		}
		catch(Exception e){
			System.out.println("Unable to perform transaction.");
		}
	}
}
class First{
	public static void main(String args[]){
		
		Merch m = new Merch();
		System.out.print("Enter the number of transactions to perform (initial stock 20 each): ");
		Scanner in = new Scanner(System.in);
		int x = in.nextInt();
		System.out.print("Enter the transactions: ");
		for(int i=0;i<x;i++){
			int id,n; char ch;
			id = in.nextInt();
			ch = in.next().charAt(0);
			n = in.nextInt();
			int type = 0;
			switch(ch){
				case 'S':
				case 's':
				type = 0; break;
				case 'M':
				case 'm':
				type = 1; break;
				case 'L':
				case 'l':
				type = 2; break;
				case 'C':
				case 'c':
				type = 3; break;
			}
			Order o = new Order(id, n, type, m);
			o.start();
		}
	}
}