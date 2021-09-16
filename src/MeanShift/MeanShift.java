package MeanShift;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import readFile.Instance;
import readFile.ReadFile;

/**
 * 1
 *
 * @param a 1
 * @return 1
 */
public class MeanShift {
	// ReadFile readFile=new ReadFile("C:\\Users\\79195\\Desktop\\iris.txt");
	ReadFile readFile;

	double[][] dataAttributeArray;//
	String[] lableArray;

	double[] oldCentroid;
	double[] newCentroid;
	ArrayList<ClastersDataStruct> frequenceArrayList = new ArrayList<ClastersDataStruct>(); // 给所有数据集一个纪录访问频率和当前所属类别的地方
	int[] tempFrequence;

	// List<Double[]> clusters=new LinkedList<Double[]>(); //一个链表从头到尾纪录划分的聚类数
	List<ClasterBlockDataStruct> clusters = new LinkedList<ClasterBlockDataStruct>();
	ArrayList<OutPutDataStruct[]> clustersBlock = new ArrayList<OutPutDataStruct[]>();
	int clusterNum = 1;

	public MeanShift(ReadFile readFile) {
		// TODO Auto-generated constructor stub
		this.readFile = readFile;
	}

	/**
	 * 初始化MeanShift类中的一些变量
	 *
	 * @param a 1
	 * @return 1
	 */
	void GetDataFromFile() {
		dataAttributeArray = new double[readFile.row][readFile.column];
		lableArray = new String[readFile.row];
		oldCentroid = new double[readFile.column];
		newCentroid = new double[readFile.column];
		tempFrequence = new int[readFile.row];
		int i = 0;
		for (Instance ins : readFile.data) {
			for (int j = 0; j < ins.attributeArrayList.size(); j++) {
				if (i < readFile.row && j < readFile.column)
					dataAttributeArray[i][j] = ins.attributeArrayList.get(j);
			}
			lableArray[i] = ins.label;
			i++;
		}
		for (int j = 0; j < readFile.row; j++) {
			ClastersDataStruct a = new ClastersDataStruct(j, 0);// 之后可以测试这样初始化是否会出问题
			frequenceArrayList.add(a);// 之后改为根据传入块大小初始化
		}
		Normalize();

	}
	/**
	 * 数据归一化
	 */
	void Normalize() {
		double min=Double.MAX_VALUE,max=Double.MIN_VALUE;
	/**
		for(int i=0;i<readFile.row;i++) {//获得最大 最小值
			for(int j=0;j<readFile.column;j++) {
				if(dataAttributeArray[i][j]>max) {
					max=dataAttributeArray[i][j];
				}
				else if(dataAttributeArray[i][j]<min) {
					min=dataAttributeArray[i][j];
				}
			}
		}
		double a=max-min;
		
		for(int i=0;i<readFile.row;i++) {//获得最大 最小值
			for(int j=0;j<readFile.column;j++) {
				dataAttributeArray[i][j]=(dataAttributeArray[i][j]-min)/a;
			}
		}
		**/
		for(int i=0;i<readFile.column;i++) {
			min=Double.MAX_VALUE;
			max=Double.MIN_VALUE;
			for(int j=0;j<readFile.row;j++) {
				if(dataAttributeArray[j][i]>max) {
					max=dataAttributeArray[j][i];
				}
				else if(dataAttributeArray[j][i]<min) {
					min=dataAttributeArray[j][i];
				}
			}
			System.out.println("max:"+max+"min:"+min);
			double a=max-min;
			
			//获得最大 最小值
				for(int j=0;j<readFile.row;j++) {
					dataAttributeArray[j][i]=(dataAttributeArray[j][i]-min)/a;
				}
			
		}
		
		for(int i=0;i<readFile.row;i++) {
			for(int j=0;j<readFile.column;j++) {
				System.out.print(dataAttributeArray[i][j]);
			}
		}
	}

	/**
	 * dataArray一个存放下标的数组
	 */
	//void SinglePointMeanShift(Integer[] dataArray, double[] centroid, float radius) {
	void SinglePointMeanShift(OutPutDataStruct[] dataArray, double[] centroid, float radius) {
		// 找到半径内所有点，纪录下来，计算meanshift向量，如果能移动继续，如果不能移动跳出，为什么可以跳出，上一次移动已经更新了后面的
		// 能移动，获取移动后的点为newCluster，与clusters比较是否存在，存在则合并，不存在则加入新cluster
		ArrayList<Integer> pointWithinRadius;
		for (int i = 0; i < readFile.column; i++)
			tempFrequence[i] = 0;

		for (int i = 0; i < readFile.column; i++) {
			oldCentroid[i] = centroid[i];
			newCentroid[i] = centroid[i];
		}

		// 类和频率
		while (true) {
			pointWithinRadius = PointWithinPadius(dataArray, newCentroid, radius);// 频率++

			newCentroid = CalculateNewCentroid(pointWithinRadius);

			if (CompareCentroid(oldCentroid, newCentroid)) {// 迭代找最终点
				// System.out.println(oldCentroid[0]+" "+oldCentroid[1]+" "+oldCentroid[2]+"
				// "+oldCentroid[3]+"-");
				break;
			} else {// 先
			}
			oldCentroid = newCentroid;

		}
		int i = ExistCluster(clusters, newCentroid);
		if (i != -1) {// 该聚类已存在，进行合并
			UpdataFrequencyArrayList(i, 1);
		} else {// 不存在 在clusters中添加新的聚类
			if (UpdataFrequencyArrayList(clusters.size(), 2)) {
				ClasterBlockDataStruct aBlockDataStruct = new ClasterBlockDataStruct(clusters.size(),
						GetDoubleValue(newCentroid));
				clusters.add((aBlockDataStruct));
			}

		}

	}

	/**
	 * 
	 */
	void AllPointMeanShift(OutPutDataStruct[] dataArray, float radius,int fatherIndex) {
		for (int i = 0; i < dataArray.length; i++) {
			 //if (frequenceArrayList.get(dataArray[i].dataIndex).frequence == 0)
			{
				 System.out.println("Node:"+i+"MeanShift!!!!!");
				SinglePointMeanShift(dataArray, dataAttributeArray[dataArray[i].dataIndex], radius);
			}

		}

		// 所有点MeanShift之后 得到划分好的聚类的块 其中需要去掉空的块

		/*
		 * int[] tempIndexofEachBlock=new int[clusters.size()];//用于暂存每个类中实例个数 for(int
		 * i=0;i<frequenceArrayList.size();i++) {
		 * tempIndexofEachBlock[frequenceArrayList.get(i).cluster]++; } for(int
		 * i=0;i<clusters.size();i++) { Integer[] a=new
		 * Integer[tempIndexofEachBlock[i]]; int k=0; for(int
		 * j=0;j<frequenceArrayList.size();j++) {
		 * if(frequenceArrayList.get(j).cluster==i) { a[k]=j; k++; } }
		 * clustersBlock.add(a); }
		 */
		// 重写
		DeleteEmptyBlock();

		int[] tempIndexofEachBlock = new int[clusters.size()];// 用于暂存每个类中实例个数
		for (int i = 0; i < dataArray.length; i++) {
			tempIndexofEachBlock[frequenceArrayList.get(dataArray[i].dataIndex).cluster]++;
		}
		for (int i = 0; i < clusters.size(); i++) {
			if (tempIndexofEachBlock[i] != 0) {
				OutPutDataStruct[] a = MeanShift.GetInitilizeTempBlock(tempIndexofEachBlock[i]);
				for (int j = 0, k = 0; j < dataArray.length; j++) {
					// if (frequenceArrayList.get(dataArray[j]).cluster == i) {
					if (frequenceArrayList.get(dataArray[j].dataIndex).cluster == clusters.get(i).clusterIndex) {
						a[k].dataIndex = dataArray[j].dataIndex;
						a[k].fatherIndex=fatherIndex;
						k++;
					}
				}
				clustersBlock.add(a);
			}
		}

	}

	/**
	 * 找到一个centroid更新一次 frequenceArrayList状态，参数1为合并，2为添加新聚类
	 * frequencyArrayList有跟新则返回true，没有跟新返回false
	 */
	boolean UpdataFrequencyArrayList(int index, int n) {
		boolean flag = false;
		if (n == 2) {
			for (int i = 0; i < frequenceArrayList.size(); i++) {
				if (tempFrequence[i] != 0 && tempFrequence[i] > frequenceArrayList.get(i).frequence) {
					frequenceArrayList.get(i).frequence = tempFrequence[i];
					frequenceArrayList.get(i).cluster = index;
					if (flag == false)
						flag = true;
				}

			}
		} else if (n == 1) {// 如何合并 类相同frequency直接相加 并更新
			for (int i = 0; i < frequenceArrayList.size(); i++) {
				if (tempFrequence[i] != 0) {
					if (frequenceArrayList.get(i).cluster == index) {
						frequenceArrayList.get(i).frequence += tempFrequence[i];
						if (flag == false)
							flag = true;
					} else {
						if (tempFrequence[i] > frequenceArrayList.get(i).frequence) {
							frequenceArrayList.get(i).frequence = tempFrequence[i];
							frequenceArrayList.get(i).cluster = index;
							if (flag == false)
								flag = true;
						}
					}
				}
			}
		}
		return flag;
	}

	void DeleteEmptyBlock() {
		/*
		 * for (int j=0;j<clusters.size();j++) { boolean flag=false; for(int
		 * i=0;i<frequenceArrayList.size();i++) {
		 * if(j==frequenceArrayList.get(i).cluster) flag=true; } if(!flag)
		 * clusters.remove(j); }
		 */
		for (int i = 0; i < clustersBlock.size(); i++) {
			// if( clustersBlock.get(i).)
		}
	}

	/**
	 * 是否已存在聚类，存在则返回下标位置，从零开始计数，不存在返回-1
	 */
	int ExistCluster(List<ClasterBlockDataStruct> clusters, double[] a) {
		int j = 0;
		for (ClasterBlockDataStruct d : clusters) {
			if (CompareCentroid(GetdoubleValue(d.clustersCentral), a)) {
				return j;
			}
			j++;
		}

		return -1;
	}

	/**
	 * 获取给定数据集中半径范围内的点，存储 在整个数据集double[][]中的行下标
	 */
	//ArrayList<Integer> PointWithinPadius(Integer[] dataArray, double[] centroid, double radius) {
	ArrayList<Integer> PointWithinPadius(OutPutDataStruct[] dataArray, double[] centroid, double radius) {
		ArrayList<Integer> dataArrayList = new ArrayList<Integer>();
		for (int i = 0; i < dataArray.length; i++) {
			if (Distance(dataAttributeArray[dataArray[i].dataIndex], centroid) <= radius) {
				dataArrayList.add(dataArray[i].dataIndex);
				tempFrequence[dataArray[i].dataIndex]++;
			}
		}

		return dataArrayList;

	}

	/**
	 * 计算newCentroid
	 */
	double[] CalculateNewCentroid(ArrayList<Integer> arrayList) {
		double[] centroid = new double[readFile.column];
		for (int i = 0; i < arrayList.size(); i++) {
			for (int j = 0; j < readFile.column; j++) {
				centroid[j] += dataAttributeArray[arrayList.get(i)][j];
			}

		}
		for (int j = 0; j < readFile.column; j++) {
			centroid[j] = centroid[j] / arrayList.size();
		}

		return centroid;
	}

	/**
	 * double[] To Double[]
	 */
	Double[] GetDoubleValue(double[] a) {
		Double[] D = new Double[a.length];
		for (int i = 0; i < a.length; i++) {
			D[i] = a[i];
		}
		return D;
	}

	/**
	 * Double[] To double[]
	 */
	double[] GetdoubleValue(Double[] a) {
		double[] D = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			D[i] = a[i];
		}
		return D;
	}
	/**
	 * 
	 */
	static OutPutDataStruct[] GetInitilizeTempBlock(int lenth) {
		
		OutPutDataStruct[] a = new OutPutDataStruct[lenth];
		for (int m = 0; m < lenth; m++) {
			a[m]=new OutPutDataStruct(m, 0);
		}
		return a;
	}
	static OutPutDataStruct[] GetInitilizeTempBlockTest(int lenth,int head) {
		
		OutPutDataStruct[] a = new OutPutDataStruct[lenth];
		for (int m = 0; m < lenth; m++) {
			a[m]=new OutPutDataStruct(m+head, 0);
		}
		return a;
	}
	/**
	 * 计算距离 没有开根号
	 */
	double Distance(double[] a, double[] b) {
		double sum = 0;
		for (int i = 0; i < readFile.column; i++) {
			sum += (a[i] - b[i]) * (a[i] - b[i]);
		}

		return sum;
	}

	/**
	 * 比较centroid
	 * 
	 * @param old 旧 newOne 新
	 * @return 如果相同返回true，
	 */
	boolean CompareCentroid(double[] old, double[] newOne) {
		boolean a = false;
		for (int i = 0; i < readFile.column; i++) {
			if (Double.compare(old[i], newOne[i]) == 0) {
				a = true;
			} else {
				return false;
			}
		}
		return a;
	}

	void OutPutResult(MeanShift meanShift) {
		System.out.println("划分的块数：" + meanShift.clustersBlock.size());
		for (OutPutDataStruct[] i : meanShift.clustersBlock) {
			System.out.print("size:" + i.length+" father:"+i[0].fatherIndex+" ");
			//
			
			System.out.print("[");
			for (int j=0;j< i.length;j++) {
				System.out.print(i[j].dataIndex + " ");
			}
			System.out.println("]");
			

		}
	}

	/**
	 * 递归函数，按条件不断对划分好的聚类进行MeanShift操作
	 *
	 * @param readFile 本地读取的数据存储类
	 * @param a        进行下一次聚类的块大小
	 * @param radius   半径
	 * @param first    待定
	 * @return 输出划分好的结果
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	static void ReadGo(ReadFile readFile, OutPutDataStruct[] a, float radius, boolean first, int count,int fatherIndex) {
		MeanShift meanShift = new MeanShift(readFile);
		meanShift.GetDataFromFile();
		meanShift.AllPointMeanShift(a, radius,fatherIndex);

		meanShift.OutPutResult(meanShift);
		
		if (meanShift.clustersBlock.size() > 1)
			for (OutPutDataStruct[] integer : meanShift.clustersBlock) {
				
				//递归的判断条件
				if (integer.length>50&&count<0 )
					ReadGo(readFile, integer, 0.4f, false, ++count,integer[0].dataIndex);
			}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ReadFile readFile = new ReadFile("src\\data\\iris.txt");
		//ReadFile readFile = new ReadFile("C:\\Users\\79195\\Desktop\\iristest.txt");
		readFile.ReadFileToList();
		
		
		float radius = 0.077f;
		OutPutDataStruct[] a = MeanShift.GetInitilizeTempBlock(readFile.row);
		//OutPutDataStruct[] a = MeanShift.GetInitilizeTempBlockTest(100, 50);
		
		
		
		int count = 0;
		ReadGo(readFile, a, radius, true, count,a[0].dataIndex);
	}

}
