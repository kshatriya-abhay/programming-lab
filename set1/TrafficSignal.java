import com.sun.corba.se.impl.orbutil.graph.Graph;

import javax.swing.*;
import java.util.*;
import java.awt.*;

class Traffic {
    char ID;
    public ArrayList<Car> que;
    Sim s;
    boolean signal;
    public Traffic(char dir, Sim sim){
        que = new ArrayList<Car>();
        ID = dir;
        s = sim;
    }
    public void nextVehicleTo(char dir,int exit_time){
        Car c = new Car(s.vid++,s.getTime(),dir,exit_time);
        que.add(c);
    }
    public void red(){
        signal = false;
    }
    public void green(){
        signal = true;
    }
    public boolean getSignal() {
        return signal;
    }
}
class Car {
    int ID;
    int ts;
    char dest;
    boolean passed;
    int exit_time;
    public int getExit_time() {
        return exit_time;
    }
    public Car(int i, int t, char d,int e){
        ID = i;
        ts = t;
        dest = d;
        passed = false;
        exit_time = e;
    }
    public int getID(){
        return ID;
    }
    public int getTs(){
        return ts;
    }
    public char getDest() {
        return dest;
    }
    public void setPassed(boolean b){
        passed = b;
    }
}
class DisplaySim extends Canvas{
    Traffic tw,ts,te;
    JFrame j;
    int t;
    public void linkObjs(Traffic W, Traffic S, Traffic E){
        tw = W; ts = S; te = E;
    }

    public void paint(Graphics g){
        setBackground(Color.WHITE);
        g.drawString("Time: "+t,20,20);
        g.setColor(Color.BLACK);
        //Road borders
        g.drawLine(0,100,800,100);
        g.drawLine(0,300,300,300);
        g.drawLine(300,300,300,600);
        g.drawLine(500,300,500,600);
        g.drawLine(500,300,800,300);
        //North pointer
        g.drawString("N",96,440);
        g.drawLine(50,500,150,500);
        g.drawLine(100,450,100,550);
        paintSignal(g);
        paintSouth(g);
        paintEast(g);
        paintWest(g);
    }
    void paintSignal(Graphics g){
        if(ts.getSignal()) g.setColor(Color.GREEN);
        else g.setColor(Color.RED);
        g.fillOval(380,40,40,40); //South
        if(te.getSignal()) g.setColor(Color.GREEN);
        else g.setColor(Color.RED);
        g.fillOval(200,320,40,40); //East
        if(tw.getSignal()) g.setColor(Color.GREEN);
        else g.setColor(Color.RED);
        g.fillOval(560,320,40,40); //West
    }
    void paintEast(Graphics g){
        g.setColor(Color.BLACK);
        int x1 = 500,y1 = 205;
        for(int i=0;i<te.que.size();i++){
            Car temp = te.que.get(i);
            if(!temp.passed){
                g.drawRect(x1,y1,60,40);
                g.drawString(""+temp.getID(),x1+30,y1+15);
                x1 += 70;
            }
        }
    }
    void paintSouth(Graphics g){
        g.setColor(Color.BLACK);
        int x1 = 305,y1 = 300;
        for(int i=0;i<ts.que.size();i++){
            Car temp = ts.que.get(i);
            if(!temp.passed){
                g.drawRect(x1,y1,40,60);
                g.drawString(""+temp.getID(),x1+15,y1+30);
                y1 += 70;
            }
        }
    }
    void paintWest(Graphics g){
        g.setColor(Color.BLACK);
        int x1 = 240,y1 = 105;
        for(int i=0;i<tw.que.size();i++){
            Car temp = tw.que.get(i);
            if(!temp.passed){
                g.drawRect(x1,y1,60,40);
                g.drawString(""+temp.getID(),x1+30,y1+15);
                x1 -= 70;
            }
        }
    }
    public DisplaySim(){
        j = new JFrame("TrafficSignal");
        j.setSize(800,600);
        j.add(this);
        j.setVisible(true);
        t=0;
    }
    public void refresh(){
        t++;
        j.add(this);
        j.revalidate();
        j.repaint();
    }
}
class Sim extends Thread {
    int time;
    public int vid;
    Traffic tw,ts,te;
    DisplaySim d;
    public Sim() {
        time = 0;
        vid = 1;
    }
    public void setTraffic(Traffic w,Traffic s, Traffic e){
        tw = w; ts = s; te = e;
    }
    public int getTime(){
        return time;
    }
    private void switchLights(int i){
        switch(i % 3){
            case 0:
                te.green(); tw.red(); ts.red();
                break;
            case 1:
                te.red(); tw.green(); ts.red();
                break;
            case 2:
                te.red(); tw.red(); ts.green();
                break;
        }
    }
    public void startDisplay() {

    }
    public void run(){
        try{
            DisplaySim d = new DisplaySim();
            d.linkObjs(tw, ts, te);
            for(;time<3600;time++){
                if(time%60 == 0){
                    switchLights((int) time/60);
                }
                int wait = 6;
                for(int i=0;i<tw.que.size();i++){
                    Car temp = tw.que.get(i);
                    if(!temp.passed){
                        if(time == temp.exit_time)
                            temp.setPassed(true);
                    }
                }
                for(int i=0;i<ts.que.size();i++){
                    Car temp = ts.que.get(i);
                    if(!temp.passed){
                        if(time == temp.exit_time)
                            temp.setPassed(true);
                    }
                }
                for(int i=0;i<te.que.size();i++){
                    Car temp = te.que.get(i);
                    if(!temp.passed){
                        if(time == temp.exit_time)
                            temp.setPassed(true);
                    }
                }
                d.refresh();
                Sim.sleep(1000);
            }
        }
        catch(Exception e){
            System.out.println("End of Simulation");
        }
    }
}
class Console extends Thread {
    Traffic tw,ts,te;
    Sim s;
    public Console(Traffic t1,Traffic t2,Traffic t3, Sim sm) {
        tw = t1;
        ts = t2;
        te = t3;
        s = sm;
    }
    public void run(){
        try{
            Scanner in = new Scanner(System.in);
            String nextInput = "";
            do{
                nextInput = in.nextLine();
                if(nextInput.equals("status")){
                    System.out.println("Traffic Light - Status - Time");
                    System.out.println("T1 - "+ (ts.getSignal() ? ("Green - "+ s.getTime()%60 ):"Red - --"));
                    System.out.println("T2 - "+ (tw.getSignal() ? ("Green - "+ s.getTime()%60 ):"Red - --"));
                    System.out.println("T3 - "+ (te.getSignal() ? ("Green - "+ s.getTime()%60 ):"Red - --"));
                    System.out.println("Vehicle - Src - Dest - Status - Wait time");
                    for(int i=0;i<ts.que.size();i++){
                        Car temp = ts.que.get(i);
                        int wait_time = temp.getExit_time() - s.getTime();
                        System.out.println(temp.getID()+" - S - "+temp.getDest()+" - "+(temp.passed?"Passed - --":"Wait - "+wait_time));
                    }
                    for(int i=0;i<tw.que.size();i++){
                        Car temp = tw.que.get(i);
                        int wait_time = temp.getExit_time() - s.getTime();
                        System.out.println(temp.getID()+" - W - "+temp.getDest()+" - "+(temp.passed?"Passed - --":"Wait - "+wait_time));
                    }
                    for(int i=0;i<te.que.size();i++){
                        Car temp = te.que.get(i);
                        int wait_time = temp.getExit_time() - s.getTime();
                        System.out.println(temp.getID()+" - E - "+temp.getDest()+" - "+(temp.passed?"Passed - --":"Wait - "+wait_time));
                    }
                }
                else{
                    if(((nextInput.charAt(0) == 'E')||(nextInput.charAt(0) == 'W')||(nextInput.charAt(0) == 'S')) && (((nextInput.charAt(2) == 'E')||(nextInput.charAt(2) == 'W')||(nextInput.charAt(2) == 'S')))){
                        int count = 1;
                        int exit_time = 0;
                        switch(nextInput.charAt(0)){
                            case 'E':
                                for(int i=0;i<te.que.size();i++){
                                    Car temp = te.que.get(i);
                                    if(!temp.passed){
                                        if(temp.getDest() == nextInput.charAt(2)){
                                            count++;
                                        }
                                    }
                                }
                                if(nextInput.charAt(2) == 'S'){
                                    exit_time = s.getTime() + count*6;
                                }
                                else{
                                    if(tw.getSignal()){
                                        exit_time = s.getTime() - (s.getTime()%60) + 120 + count*6;
                                    }
                                    else if(ts.getSignal()){
                                        exit_time = s.getTime() - (s.getTime()%60) + 60 + count*6;
                                    }
                                    else{
                                        exit_time = s.getTime() + count*6;
//                                        if((s.getTime() + count*6)%60 != (s.getTime()%60)+count*6){
//                                            exit_time = s.getTime() - (s.getTime()%60) + 180 + count*6;
//                                        }
//                                        else{
//                                            exit_time = s.getTime() - (s.getTime()%60) + count*6;
//                                        }
                                    }
                                }
                                te.nextVehicleTo(nextInput.charAt(2),exit_time);
                                break;
                            case 'W':
                                for(int i=0;i<tw.que.size();i++){
                                    Car temp = tw.que.get(i);
                                    if(!temp.passed){
                                        if(temp.getDest() == nextInput.charAt(2)){
                                            count++;
                                        }
                                    }
                                }
                                if(nextInput.charAt(2) == 'E'){
                                    exit_time = s.getTime()+ count*6;
                                }
                                else{
                                    if(ts.getSignal()){
                                        exit_time = s.getTime() - (s.getTime()%60) + 120 + count*6;
                                    }
                                    else if(te.getSignal()){
                                        exit_time = s.getTime() - (s.getTime()%60) + 60 + count*6;
                                    }
                                    else{
                                        exit_time = s.getTime() + count*6;
//                                        if((s.getTime() + count*6)%60 != (s.getTime()%60)+count*6){
//                                            exit_time = s.getTime() - (s.getTime()%60) + 180 + count*6;
//                                        }
//                                        else{
//                                            exit_time = s.getTime() - (s.getTime()%60) + count*6;
//                                        }
                                    }
                                }
                                tw.nextVehicleTo(nextInput.charAt(2),exit_time);
                                break;
                            case 'S':
                                for(int i=0;i<ts.que.size();i++){
                                    Car temp = ts.que.get(i);
                                    if(!temp.passed){
                                        if(temp.getDest() == nextInput.charAt(2)){
                                            count++;
                                        }
                                    }
                                }
                                if(nextInput.charAt(2) == 'W'){
                                    exit_time = s.getTime()+ count*6;
                                }
                                else{
                                    if(te.getSignal()){
                                        exit_time = s.getTime() - (s.getTime()%60) + 120 + count*6;
                                    }
                                    else if(tw.getSignal()){
                                        exit_time = s.getTime() - (s.getTime()%60) + 60 + count*6;
                                    }
                                    else{
                                        exit_time = s.getTime() + count*6;
//                                        if((s.getTime() + count*6)%60 != (s.getTime()%60)+count*6){
//                                            exit_time = s.getTime() - (s.getTime()%60) + 180 + count*6;
//                                        }
//                                        else{
//                                            exit_time = s.getTime() - (s.getTime()%60) + count*6;
//                                        }
                                    }
                                }
                                ts.nextVehicleTo(nextInput.charAt(2),exit_time);
                                break;
                        }
                    }
                }
            }while(!nextInput.equals("quit"));
            System.out.println("End of Console");

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
public class TrafficSignal {

    public static void main(String[] args) {
        System.out.println("Commands:\nE W, S E, etc\nquit\nstatus");
        Sim s = new Sim();
        Traffic tw = new Traffic('W',s);
        Traffic ts = new Traffic('S',s);
        Traffic te = new Traffic('E',s);
        s.setTraffic(tw,ts,te);
        Console c = new Console(tw,ts,te,s);
        c.start();
        s.start();
    }
}