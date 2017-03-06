package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Account implements Serializable{
	
	public Employee employee;
	
	
	public Account(){
		this.employee = null;
	}
	
	public void LoadAccount(){
		try {
	         FileInputStream fileIn = new FileInputStream("AccountData/account.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         this.employee = (Employee) in.readObject();
	         in.close();
	         fileIn.close();
	      }catch(IOException i) {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c) {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	         return;
	      }
		
	}
	
	public void SaveAccount(Account account){
		try {
			File oldFile = new File("AccountData/account.ser");
			oldFile.delete();
			File newFile = new File("AccountData/account.ser");
			
	        FileOutputStream fileOut =
	        new FileOutputStream(newFile);
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(account.employee);
	        out.close();
	        fileOut.close();
	        System.out.println("Serialized data is saved in AccountData/account.ser");
	      }catch(IOException i) {
	         i.printStackTrace();
	      }
	}
	

}
