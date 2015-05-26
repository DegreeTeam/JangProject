package com.example.audiotcp;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

class CircularQueue {
	private int maxSize;
	private BLEArr[] queueArray;
	public int front;
	public int rear;
	
	public CircularQueue(int size){
		maxSize = size + 1;
		queueArray = new BLEArr[maxSize];
		front = 0;  //공백상태의  Queue Front
		rear = -1;  //공백상태의  Queue Rear
	}
	
	public boolean insert(BLEArr item){
		if(isFull()){
			return false;
		} else {
			if (rear == maxSize - 1){
				rear = -1;
			}
			queueArray[++rear] = item;
	
			return true;
		}

	}
	
	 public Object GetElement(int i) {
           if (isEmpty())  return null;
           else return  queueArray[i];
      }
	 
	public int size(){
		if (isEmpty()){
			return(0);
		} else if (rear >= front){
			return (rear - front + 1);
		} else {
			return ((maxSize - front)+(rear + 1));
		}
	}
	
	public boolean isFull(){
		return ((front-2 == rear)||(front+maxSize-2 == rear));
	}
	
	public boolean isEmpty() {
		return ((front-1 == rear)||(front+maxSize-1 == rear));
	}


	public boolean contains(BluetoothDevice device) {
		// TODO Auto-generated method stub
		for (int i = front; i <= rear; i++)
		{ 
			if (queueArray[i].device.getAddress().equals(device.getAddress()))
				return true;
		}
		return false;
	}
}

