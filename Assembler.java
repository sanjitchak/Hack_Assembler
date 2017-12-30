	import java.util.*;
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileNotFoundException;
	public class Assembler {
		Scanner scan;
		Scanner scan2;
		Formatter x;
		Map<String,String> comp;
		Map<String,String> dest;
		Map<String,String> jump;
		Map<String,Integer> symbol;
		int startRAMAddress=16;
		public Assembler(String filename){
	
			try {
				scan = new Scanner(new File(filename));	
			} 
			catch(Exception e)
			{
				System.out.println("Doesn't exist");
			}
			try {
				scan2 = new Scanner(new File(filename));	
			}
			catch(Exception e)
			{
				System.out.println("Doesn't exist");
			}
	
		}
		public void hashMap()
		{  symbol = new HashMap<>();
		symbol.put("SP", 0);
		symbol.put("LCL", 1);
		symbol.put("ARG", 2);
		symbol.put("THIS", 3);
		symbol.put("THAT", 4);
		symbol.put("R0", 0);
		symbol.put("R1", 1);
		symbol.put("R2", 2);
		symbol.put("R3", 3);
		symbol.put("R4", 4);
		symbol.put("R5", 5);
		symbol.put("R6", 6);
		symbol.put("R7", 7);
		symbol.put("R8", 8);
		symbol.put("R9", 9);
		symbol.put("R10", 10);
		symbol.put("R11", 11);
		symbol.put("R12", 12);
		symbol.put("R13", 13);
		symbol.put("R14", 14);
		symbol.put("R15", 15);
		symbol.put("SCREEN", 16384);
		symbol.put("KBD", 24576);
		comp = new HashMap<String, String>();
		comp.put("0", "0101010");
		comp.put("1","0111111");
		comp.put("-1","0111010");
		comp.put("D","0001100");
		comp.put("A","0110000");
		comp.put("!D","0001101");
		comp.put("!A","0110001");
		comp.put("-D","0001111");
		comp.put("-A","0110011");
		comp.put("D+1","0011111");
		comp.put("A+1","0110111");
		comp.put("D-1","0001110");
		comp.put("A-1","0110010");
		comp.put("D+A","0000010");
		comp.put("D-A","0010011");
		comp.put("A-D","0000111");
		comp.put("D&A","0000000");
		comp.put("D|A","0010101");
		comp.put("M","1110000");
		comp.put("!M","1110001");
		comp.put("-M","1110011");
		comp.put("M+1","1110111");
		comp.put("M-1","1110010");
		comp.put("D+M","1000010");
		comp.put("D-M","1010011");
		comp.put("M-D","1000111");
		comp.put("D&M","1000000");
		comp.put("D|M","1010101");
		dest = new HashMap<String, String>();
		dest.put("M","001");
		dest.put("D","010");
		dest.put("MD","011");
		dest.put("A","100");
		dest.put("AM","101");
		dest.put("AD","110");
		dest.put("AMD","111");
		dest.put("0","000");
		jump = new HashMap<String, String>();
		jump.put("0","000");
		jump.put("JGT","001");
		jump.put("JEQ","010");
		jump.put("JGE","011");
		jump.put("JLT","100");
		jump.put("JNE","101");
		jump.put("JLE","110");
		jump.put("JMP","111");
		return;
		}
		public void readWrite(String hack)
		{   try {
			this.x = new Formatter(hack+".hack");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hashMap();
		writeToFile();
	
		}
		public void addressToBinary(String a)
		{   int number;
		if(scan.hasNextInt()) //if only number address
			number=scan.nextInt();
	
		else
		{
			String next=scan.next();
			
			if(!symbol.containsKey(next))
			{symbol.put(next, startRAMAddress);
			startRAMAddress++;}
			number= symbol.get(next); }
	
		a=Integer.toBinaryString(number);
		char arr [] = a.toCharArray();
		int length= 16-arr.length;
		for(int i=0;i<length;i++)
			x.format("0");
		x.format("%s\n", a);
		return;
		}
	
	
	
		public void writeToFile( )
		{ int count=-1; 
		while(scan2.hasNextLine()) //first pass
		{
			String a=null;
			if(scan2.hasNext("//"))  //for ignoring comments  
			{
				scan2.nextLine();
				continue;
			}
	
			if(scan2.hasNext("[(].+[)]"))
			{  scan2.findInLine("[(]");
			scan2.useDelimiter("[)]");
			a=scan2.next();//to get variable within bracket
			symbol.put(a, count+1); //count+1 for next line
	
	
			scan2.reset();
			scan2.nextLine(); //to get all the inputs
			continue;
	
			}
	
			a = scan2.nextLine();
			//
			if(a.isEmpty())
			{
				continue;
			}
			count++;
	
		}
	
		while(scan.hasNextLine()) //second pass
		{  
			String a=null;
	
	
			if(scan.hasNext("//") || scan.hasNext("[(].+[)]"))  //for ignoring comments  & ()
			{
				scan.nextLine();
	
				continue;
			}
	
	
	
	
			a=scan.findInLine("@"); //for address to binary
			if(a!=null)                    //if a variable contains the @
			{  
				addressToBinary(a);
	
				continue;
			}
	
			
			computationIns();
		
	
	
	
		} 
		}
		public void computationIns()
		{ 
			if(scan.hasNext("[A-Z]+[=].+"))
			{ scan.useDelimiter("=");
			String desti = scan.next().trim();
			scan.reset();
			scan.findInLine("=");
			String compu=scan.next();
	      desti = dest.get(desti);
	 
	      compu = comp.get(compu);
			x.format("111%s%s000\n",compu,desti);
	   
			}
			
			if(scan.hasNext(".+[;].+"))
			{
				scan.useDelimiter(";");
	
	
				String compu=scan.next().trim();
				scan.reset();
				scan.findInLine(";");
				//System.out.println(compu);
				String jumpi = scan.next();
				jumpi = jump.get(jumpi);
	
				compu = comp.get(compu);
			x.format("111%s000%s\n",compu,jumpi);

			}
	
			scan.nextLine();
		
			return;
		}
	
		public void close()
		{
			scan.close();
			x.close();
		}
	
		public static void main(String args[])
		{
	
			Assembler hack = new Assembler(args[0]);
			
			hack.readWrite("Hello");
			hack.close();
	
	
		}
	}
