import java.lang.*; 
import java.util.Scanner;
import java.util.*;
/*
Any case where B1 or B2 label information needs to be sent, it was done so with 1 and 2
*/
class Counters{				//class that keeps count of all the required values
	int b1_packaged = 0;	//final count as to how many b1 are packaged
	int b2_packaged = 0;	//similarly other definitions follow
	int b1_sealed = 0;
	int b2_sealed = 0;
	synchronized public void incrementCounter(int a, int b){	//synchronized function to change count values
		if (a == 0  && b == 1){									//synchronization needed since both threads may access at the same time
			b1_packaged++;
		}
		else if(a == 0 && b == 2){								//a defines whether packaging or sealing counter, b defines bottle label
			b2_packaged++;
		}
		if (a == 1  && b == 1){
			b1_sealed++;
		}
		else if(a == 1 && b == 2){
			b2_sealed++;
		}
	}
}
class Bottle{		//not inefficient to have this
	int type = 0; 			//status of the Bottle, whether B1 or B2, 1 implies B1 and 2 implies B2
	boolean packaged = false;
	boolean sealed = false;
	int package_rtime = 0;  //time remaining for the packaging to be done
	int seal_rtime = 0;		//time remaining for the sealing to be done
	boolean counted = false;
	Bottle(int val){
		type = val;
		package_rtime = 2;	//defined as 2 seconds
		seal_rtime = 3;		//defined as 3 seconds
	}
}
class Godown{		//godown class
	int b1_count = 0;		//count of B1 that are in godown
	int b2_count = 0;		//count of B2 in godown
	synchronized public void increment(int val){		//incrementing needs to be synchronized
		if (val == 1){
			b1_count = b1_count + 1;
		}
		else if (val == 2){
			b2_count = b2_count + 1;
		}
	}
}
								//deletion of non running objects (class instances) is done automatically by garbage collector
class UnfinishedTray{
	int b1_count = 0;		//count of how many elements are in tray (B1 elements)
	int b2_count = 0;
	int packager_flag = 1;  /*defines which type of Bottle to give to the Packager next*/
	int sealer_flag = 2;	//defines which type of bottle to be given to Sealer next
	UnfinishedTray(int val1, int val2){
		b1_count = val1;
		b2_count = val2;
		this.packager_flag = 1;
		this.sealer_flag = 2;
	}
	synchronized public int give(boolean packager){
		if (packager){					//Packager requests for unit from unfinishedtray
			if (packager_flag == 2){ 	//flag defines priority, however, in the case requested type doesn't exist, gives other type(if available)
				if (b2_count == 0){
					if (b1_count > 0){
						packager_flag = 2;
						b1_count--;
						return 1;	//no need to modify the Packager flags, since its guarenteed to all be zero
					}
					else return -3;		//case that no unfinished bottles are there, returns negative values.  3 selected for debugging purposes
				}
				b2_count--;
				packager_flag = 1;
				return 2;
			}
			else if (packager_flag == 1){ //Packager flag 1 implies it is requesting for the B1 type
				if (b1_count == 0){
					if (b2_count > 0){
						b2_count--;
						packager_flag = 1;
						return 1;	
					}
					else return -3;
				}
				b1_count--;
				packager_flag = 2;		
				return 1;
			}
			else{
				return -1234;		//implies faulty packager_flag values
			}
		}
		else{						//Sealer requests the data
			if (sealer_flag == 2){  //sends B2 to the sealer, if B2 count exists
				if (b2_count == 0){
					if (b1_count != 0){
						sealer_flag = 2;
						b1_count--;
						return 1;	
					}
					else return -3;
				}
				b2_count--;
				sealer_flag = 1;
				return 2;
			}
			else if (sealer_flag == 1){
				if (b1_count == 0){
					if (b2_count != 0){
						b2_count--;
						sealer_flag = 1;
						return 1;	
					}
					else return -3;
				}
				b1_count--;
				sealer_flag = 2;
				return 1;
			}
			else{
				return -1234;				//denotes faulty sealer_flag value, for debugging
			}
		}
	}
}
class PackagerInputQueue{					//queue that feeds into packager
	int b1_count = 0;						//count of number of b1 and b2 in the queue
	int b2_count = 0;
	int flag = 1; 							//1 implies B1, 2 implies B2 to be the next entity to give the packager
	synchronized public int giveGet(int val, int data){		//function to synchronize access to queue counts (of B1 and B2)
		if (val == 0){ //i.e. a give request
			return give();
		}
		else if (val == 1){
			get(data);
			return -1;
		}
		return -1234;
	}
	public int give(){	//give will be called only if there exists an bottle of that type, gives bottle to packager
		if (flag == 1){
			b1_count--;
			flag = 2;
			return 1;
		}
		else if (flag == 2){
			b2_count--;
			flag = 1;
			return 2;
		}
		else{
			return -1234;
		}
	}
	public void get(int val){  //get implies that the queue is recieving a bottle(from sealer)
		if (val == 1){					
			b1_count++;
		}
		else if(val == 2){
			b2_count++;
		}
	}

}
class SealerInputQueue{		//the Sealer input queue
	int b1_count = 0; 
	int b2_count = 0;
	synchronized public int giveGet(int val, int data){  //synchronized giveget function, to maintain counter access
		if (val == 0){
			int tempor = give();
			return tempor;
		}
		else if(val == 1){
			get(data);
			return -1;
		}
		return -1234;
	}
	synchronized public int give(){ //called only if (b1_count + b2_count) > 0
		if (b1_count > 0){//higher priority given to B1 though not a problem in our case
			b1_count--;
			return 1;
		}
		else{
			b2_count--;
			return 2;
		}
	}
	synchronized public void get(int data){ //function as to what happens if something is added to this queue
		if (data == 1){
			b1_count++;
		}
		else if (data == 2){
			b2_count++;
		}
	}
}
class Sealer extends Thread{		//thread Sealer, handles the sealer operations
	boolean bottle_exist = false;  //not necessary, added for sake of completeness
	Bottle sealing_bottle = null;
	public Bottle request(SealerInputQueue s, UnfinishedTray u_tray){  //function for sealer to get a bottle from its queue or unfinishedtray 
		Bottle temp = null;
		if (s.b1_count > 0 || s.b2_count > 0){
			int bottle_label = s.giveGet(0,0);
			temp = new Bottle(bottle_label); 
			temp.packaged = true;
		}
		else{
			int uval = u_tray.give(false); //false implies give request acted on by sealer
			if (uval == 1){
				temp = new Bottle(1);
			}
			else if (uval == 2){
				temp = new Bottle(2);
			}
			else if (uval < 0){
				//dead end, can only give these kinds of bottles but nothing will happen.
				
			}
		}
		return temp;
	}
	//run function defines sealer behaviour, defining when to push into queue and when to godown, as well as when to simply wait
	//has additional code to maintain counts in Counters object properly
	public void run(SealerInputQueue s, PackagerInputQueue pq, UnfinishedTray u_tray, Godown g, Counters cou){
		if (bottle_exist == true){		//case where sealer is currently operating on a bottle
			sealing_bottle.seal_rtime = sealing_bottle.seal_rtime - 1;
			if (sealing_bottle.seal_rtime == 0){	//case where to bottle needs to leave the sealer
				if (sealing_bottle.packaged == true){//send to godown
					g.increment(sealing_bottle.type);
					cou.incrementCounter(1, sealing_bottle.type);
					//bottle_exist = false;//actually should request for another bottle..., i.e. the end fo the second
					sealing_bottle = request(s, u_tray);
					if (sealing_bottle == null){
						bottle_exist = false;
					}
				}
				else{//to be pushed into PackagerInputQueue
					int sealing_bottle_type = sealing_bottle.type;
					if (sealing_bottle_type == 1){					//bottle type implies which queue to push into
						if (pq.b1_count < 2){
							//only counts if it leaves the system
							if (sealing_bottle.counted == false){
								cou.incrementCounter(1, sealing_bottle.type);
								sealing_bottle.counted = true;
							}							
							pq.giveGet(1, sealing_bottle_type);
							sealing_bottle = request(s, u_tray);	//immediately gets a new bottle after expelling completed one
							if (sealing_bottle == null){
								bottle_exist = false;
							}
						}
						else{
							if (sealing_bottle.counted == false){
								cou.incrementCounter(1, sealing_bottle.type);
								sealing_bottle.counted = true;
							}
							//do nothing, we wait
						}
					}
					if (sealing_bottle_type == 2){
						if (pq.b2_count < 3){	//pushes into queue (B2 of packaging queue) only if space exists
							pq.giveGet(1, sealing_bottle_type);
							if (sealing_bottle.counted == false){
								cou.incrementCounter(1, sealing_bottle.type);
								sealing_bottle.counted = true;
							}		
							sealing_bottle = request(s, u_tray);
							if (sealing_bottle == null){
								bottle_exist = false;
							}
						}
						else{
							//do nothing, wait for queue to be cleared.
							if (sealing_bottle.counted == false){
								cou.incrementCounter(1, sealing_bottle.type);
								sealing_bottle.counted = true;
							}		
						}
					}

				}
			}

		}
		else{
			//bottle doesnt exist
			sealing_bottle = request(s, u_tray);
			if (sealing_bottle == null){
				bottle_exist = false;
			}
			else{
				sealing_bottle.seal_rtime = sealing_bottle.seal_rtime - 1;
				bottle_exist = true;
			}
		}
	}

}
class Packager extends Thread{		//packager thread, handles packaging functions
	boolean bottle_exist = false; //keeps track if currently operating on a bottl or not
	Bottle packaging_bottle = null; //keeps track of current bottle, if any, else null
	public Bottle request(PackagerInputQueue pq, UnfinishedTray u_tray){ //asks for an entity from the queues or the unfinishedtray
		//should be called only if bottle is false
		int pq_fail_flag = 0;
		Bottle temp = null;
		if (pq.flag == 1){
			if (pq.b1_count > 0){
				pq.giveGet(0,0);
				temp = new Bottle(1);
				temp.sealed = true;
				pq.flag = 2;
			}
			else{
				if (pq.b2_count > 0){
					pq.flag = 2;
					pq.giveGet(0,0);
					temp = new Bottle(2);
					temp.sealed = true;
					pq.flag = 1;
				}
				else{
					pq_fail_flag = 1;
				}
			}

		}
		else if (pq.flag == 2){
			if (pq.b2_count > 0){
				temp = new Bottle(2);
				pq.giveGet(0,0);
				temp.sealed = true;
				pq.flag = 1;
			}
			else{
				if (pq.b1_count > 0){
					pq.flag = 1;
					pq.giveGet(0,0);
					temp = new Bottle(1);
					temp.sealed = true;
					pq.flag = 2;
				}
				else{
					pq_fail_flag = 1;
				}
			}

		}
		if (pq_fail_flag == 1){
			//request from unfinished tray
			pq_fail_flag = 0;
			int uval = u_tray.give(true); 
			if (uval == 1){
				//make instance of new bottle B1
				temp = new Bottle(1);
			}
			else if (uval == 2){
				//make instance of new bottle B2
				temp = new Bottle(2);
			}
			else if (uval < 0){
				//this case is special, what happens is that the Packager will have no bottle even though the request exists
				//essentially temp will be null in this case, the request fails, and we simple wait for the bottle to handle the queue
			}
		}
		return temp;
	}
	//defines how the packager should behave, with the requests and when to push into godown and when into other queue (sealer)
	public void run(SealerInputQueue s, PackagerInputQueue pq, UnfinishedTray u_tray, Godown g, Counters cou){//the override function
		if (bottle_exist == true){	//similar structure to sealer class run function
			packaging_bottle.package_rtime = packaging_bottle.package_rtime - 1;
			if (packaging_bottle.package_rtime == 0){
				if (packaging_bottle.sealed == true){ //bottle is packaged and sealed
					g.increment(packaging_bottle.type);
					cou.incrementCounter(0, packaging_bottle.type);
					packaging_bottle = request(pq, u_tray); //need to clear the class though, how? garbage colleciton is taken care automatically
					if (packaging_bottle == null){
						bottle_exist = false;
					}
				}
				else if (packaging_bottle.sealed == false){ //send to the seal queue
					if ((s.b1_count + s.b2_count) == 2){
						//we need to do nothing and wait until it becomes empty
						if (packaging_bottle.counted == false){
							cou.incrementCounter(0, packaging_bottle.type);
							packaging_bottle.counted = true;
						}
					}
					else if ((s.b1_count + s.b2_count) < 2){
						s.giveGet(1, packaging_bottle.type);
						if (packaging_bottle.counted == false){
							cou.incrementCounter(0, packaging_bottle.type);
							packaging_bottle.counted = true;
						}
						packaging_bottle = request(pq,u_tray);
						if (packaging_bottle == null){
							bottle_exist = false;
						}
					}
				}
			}
		}
		else if (bottle_exist == false){ //the only other case
			packaging_bottle = this.request(pq, u_tray);
			if (packaging_bottle == null){
				//to do something, nothing
				bottle_exist = false;  //no change in this system.
			}
			else{
				packaging_bottle.package_rtime = packaging_bottle.package_rtime - 1;
				bottle_exist = true;
			}
		}
	}
}
public class Set1_2{			//main class
	public static void main(String []args) {		//driver program
		System.out.println("Enter 3 values, b1_count, b2_count and timestamp required");
		Scanner scanunit = new Scanner(System.in);
		int b1_count = scanunit.nextInt();
		int b2_count = scanunit.nextInt();
		int timestamp = scanunit.nextInt();
		Godown g = new Godown();
		UnfinishedTray u_tray = new UnfinishedTray(b1_count, b2_count);
		PackagerInputQueue pq = new PackagerInputQueue();
		SealerInputQueue sq = new SealerInputQueue();
		Packager p = new Packager();
		Sealer s = new Sealer();
		Counters cou = new Counters();
		for (int i = 0;i<timestamp;i++){		//prints out all instances at all times until the requested timestamp
			p.run(sq, pq, u_tray, g, cou);
			s.run(sq, pq, u_tray, g, cou);
			System.out.println(i);
			System.out.println("	B1 packaged" + cou.b1_packaged);
			System.out.println("	B1 sealing" + cou.b1_sealed);
			System.out.println("	B1 in godown" + g.b1_count);
			// System.out.println("	B1 packaged" + (sq.b1_count));
			// System.out.println("	B1 sealed" + pq.b1_count);
			// System.out.println("	B2 packaged" + sq.b2_count);
			// System.out.println("	B2 sealed" + pq.b2_count);
			// System.out.println("	utray 1 count" + u_tray.b1_count);
			// System.out.println("	utray 2 count" + u_tray.b2_count);
			System.out.println("	B2 packaged" + cou.b2_packaged);

			System.out.println("	B2 sealed" + cou.b2_sealed);
			System.out.println("	B2 in godown" + g.b2_count);
		}
	}
}