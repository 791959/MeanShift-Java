package readFile;


import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 *  创建后需要调用一次ReadFileToList
 * 这个类根据传入的文件路径，在List<Instance>链表中存放文本文件中的数据
 * column row
 * 
 * Instance类 由一个ArrayList<Double> 链表存放属性，String 存放标签
 * @author pang
 *
 *
 *!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!改成了可读可写，读取函数调用Instance类来实例化每一行，在instance中更改读取格式
 */
public class ReadFile {
	String filePathString="";
	public List<Instance> data=new ArrayList<Instance>();
	public int column=0;
	public int row;

	/**
	 * 构造函数
	 * @param a 要打开的文件路径
	 */
	public ReadFile(String a){
		super();
		filePathString=a;
	}
/*
 * 该函数按行读取文件，然后把行string传入Instance类构造单个的行数据
 *输出：在ReadFile类的data数据中放入所有行数据
 */
	
	public void ReadFileToList() throws IOException {
		
		//File file=new File(filePathString);
		FileReader fileReader=new FileReader(filePathString);
		BufferedReader bR=new BufferedReader(fileReader);
		
		String lineString;
		while((lineString=bR.readLine())!=null) {
			if(!lineString.startsWith("%")&&!lineString.isEmpty()&&!lineString.startsWith("@")) {
			Instance lineDataInstance=new Instance(lineString);
			data.add( lineDataInstance);
			}
		}
		bR.close();
		fileReader.close();
		
		
		row=data.size();
		System.out.println(data.get(0).attributeArrayList);
		column=data.get(0).attributeArrayList.size();
		
		/*
		 * 
		 * 后来加的，防止列数不一样
		 */
		for(int i=0;i<row-1;i++) {
			
			if(data.get(i+1).attributeArrayList.size()<data.get(i).attributeArrayList.size()){
				column=data.get(i+1).attributeArrayList.size();
			}
		}
		//column=data.get(0).attributeArrayList.size();
		
	}
	public void WriteFile(String path,String fileName) throws IOException {
		FileWriter fileWriter=new FileWriter(path+fileName);
		
		String newLineString = null;
		for(Instance a:this.data) {
			newLineString="";
			for(Double b:a.attributeArrayList) {
				newLineString=newLineString+b.toString()+",";
			}
			newLineString+=a.label+"\n";
			fileWriter.write(newLineString);
		}
		
		
		fileWriter.close();
		
	}
	
public void Test() throws IOException {
		
		//File file=new File(filePathString);
		FileReader fileReader=new FileReader(filePathString);
		BufferedReader bR=new BufferedReader(fileReader);
		
		while((bR.readLine())!=null) {
			//System.out.println(lineString);
		}
		bR.close();
		fileReader.close();
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//System.out.println();
		
		ReadFile readFile=new ReadFile("C:\\Users\\79195\\Desktop\\text.txt");
		 
		readFile.ReadFileToList();
		for (Instance instance : readFile.data) {
			System.out.println(instance.attributeArrayList.toString()+instance.label);
			
		}
		System.out.println(readFile.row+" ="+readFile.column);
		
		//readFile.WriteFile("C:\\Users\\79195\\Desktop\\", "abalone1.txt");
		
		
	}
	
		///ReadFile readFile=new ReadFile("C:\\Users\\79195\\Desktop\\iris.csv");
		 
		//readFile.Test();;
	

}
