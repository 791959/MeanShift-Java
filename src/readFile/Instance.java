package readFile;

import java.util.ArrayList;

public class Instance {
	public ArrayList<Double> attributeArrayList=new ArrayList<Double>();
	public String label;

	public Instance(String line) {
		// TODO Auto-generated constructor stub
		String[] splitLine=line.split(",");
		for(int i=0;i<splitLine.length;i++) {
			
			
			//if(i!=splitLine.length-1&&i!=0) {//去掉首列
			int flag=0;
			
			if(flag==0) {//标签在第一列
				if(i==0) {
					//System.out.println(splitLine[i]);
					label=splitLine[i];
				}
				else {
					
					attributeArrayList.add(Double.valueOf(splitLine[i].trim()));
				}
			}
			else {
				if(i!=splitLine.length-1) {
					//System.out.println(splitLine[i]);
					attributeArrayList.add(Double.valueOf(splitLine[i].trim()));
					
				}
				else {
					label=splitLine[i];
					
				}
				
			}
			
			
			
		}
	}

}
