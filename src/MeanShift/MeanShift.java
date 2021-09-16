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
	ArrayList<ClastersDataStruct> frequenceArrayList = new ArrayList<ClastersDataStruct>(); // ���������ݼ�һ����¼����Ƶ�ʺ͵�ǰ�������ĵط�
	int[] tempFrequence;

	// List<Double[]> clusters=new LinkedList<Double[]>(); //һ�������ͷ��β��¼���ֵľ�����
	List<ClasterBlockDataStruct> clusters = new LinkedList<ClasterBlockDataStruct>();
	ArrayList<OutPutDataStruct[]> clustersBlock = new ArrayList<OutPutDataStruct[]>();
	int clusterNum = 1;

	public MeanShift(ReadFile readFile) {
		// TODO Auto-generated constructor stub
		this.readFile = readFile;
	}

	/**
	 * ��ʼ��MeanShift���е�һЩ����
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
			ClastersDataStruct a = new ClastersDataStruct(j, 0);// ֮����Բ���������ʼ���Ƿ�������
			frequenceArrayList.add(a);// ֮���Ϊ���ݴ�����С��ʼ��
		}
		Normalize();

	}
	/**
	 * ���ݹ�һ��
	 */
	void Normalize() {
		double min=Double.MAX_VALUE,max=Double.MIN_VALUE;
	/**
		for(int i=0;i<readFile.row;i++) {//������ ��Сֵ
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
		
		for(int i=0;i<readFile.row;i++) {//������ ��Сֵ
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
			
			//������ ��Сֵ
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
	 * dataArrayһ������±������
	 */
	//void SinglePointMeanShift(Integer[] dataArray, double[] centroid, float radius) {
	void SinglePointMeanShift(OutPutDataStruct[] dataArray, double[] centroid, float radius) {
		// �ҵ��뾶�����е㣬��¼����������meanshift������������ƶ���������������ƶ�������Ϊʲô������������һ���ƶ��Ѿ������˺����
		// ���ƶ�����ȡ�ƶ���ĵ�ΪnewCluster����clusters�Ƚ��Ƿ���ڣ�������ϲ����������������cluster
		ArrayList<Integer> pointWithinRadius;
		for (int i = 0; i < readFile.column; i++)
			tempFrequence[i] = 0;

		for (int i = 0; i < readFile.column; i++) {
			oldCentroid[i] = centroid[i];
			newCentroid[i] = centroid[i];
		}

		// ���Ƶ��
		while (true) {
			pointWithinRadius = PointWithinPadius(dataArray, newCentroid, radius);// Ƶ��++

			newCentroid = CalculateNewCentroid(pointWithinRadius);

			if (CompareCentroid(oldCentroid, newCentroid)) {// ���������յ�
				// System.out.println(oldCentroid[0]+" "+oldCentroid[1]+" "+oldCentroid[2]+"
				// "+oldCentroid[3]+"-");
				break;
			} else {// ��
			}
			oldCentroid = newCentroid;

		}
		int i = ExistCluster(clusters, newCentroid);
		if (i != -1) {// �þ����Ѵ��ڣ����кϲ�
			UpdataFrequencyArrayList(i, 1);
		} else {// ������ ��clusters������µľ���
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

		// ���е�MeanShift֮�� �õ����ֺõľ���Ŀ� ������Ҫȥ���յĿ�

		/*
		 * int[] tempIndexofEachBlock=new int[clusters.size()];//�����ݴ�ÿ������ʵ������ for(int
		 * i=0;i<frequenceArrayList.size();i++) {
		 * tempIndexofEachBlock[frequenceArrayList.get(i).cluster]++; } for(int
		 * i=0;i<clusters.size();i++) { Integer[] a=new
		 * Integer[tempIndexofEachBlock[i]]; int k=0; for(int
		 * j=0;j<frequenceArrayList.size();j++) {
		 * if(frequenceArrayList.get(j).cluster==i) { a[k]=j; k++; } }
		 * clustersBlock.add(a); }
		 */
		// ��д
		DeleteEmptyBlock();

		int[] tempIndexofEachBlock = new int[clusters.size()];// �����ݴ�ÿ������ʵ������
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
	 * �ҵ�һ��centroid����һ�� frequenceArrayList״̬������1Ϊ�ϲ���2Ϊ����¾���
	 * frequencyArrayList�и����򷵻�true��û�и��·���false
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
		} else if (n == 1) {// ��κϲ� ����ͬfrequencyֱ����� ������
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
	 * �Ƿ��Ѵ��ھ��࣬�����򷵻��±�λ�ã����㿪ʼ�����������ڷ���-1
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
	 * ��ȡ�������ݼ��а뾶��Χ�ڵĵ㣬�洢 ���������ݼ�double[][]�е����±�
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
	 * ����newCentroid
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
	 * ������� û�п�����
	 */
	double Distance(double[] a, double[] b) {
		double sum = 0;
		for (int i = 0; i < readFile.column; i++) {
			sum += (a[i] - b[i]) * (a[i] - b[i]);
		}

		return sum;
	}

	/**
	 * �Ƚ�centroid
	 * 
	 * @param old �� newOne ��
	 * @return �����ͬ����true��
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
		System.out.println("���ֵĿ�����" + meanShift.clustersBlock.size());
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
	 * �ݹ麯�������������϶Ի��ֺõľ������MeanShift����
	 *
	 * @param readFile ���ض�ȡ�����ݴ洢��
	 * @param a        ������һ�ξ���Ŀ��С
	 * @param radius   �뾶
	 * @param first    ����
	 * @return ������ֺõĽ��
	 * @throws IndexOutOfBoundsException {@inheritDoc}
	 */
	static void ReadGo(ReadFile readFile, OutPutDataStruct[] a, float radius, boolean first, int count,int fatherIndex) {
		MeanShift meanShift = new MeanShift(readFile);
		meanShift.GetDataFromFile();
		meanShift.AllPointMeanShift(a, radius,fatherIndex);

		meanShift.OutPutResult(meanShift);
		
		if (meanShift.clustersBlock.size() > 1)
			for (OutPutDataStruct[] integer : meanShift.clustersBlock) {
				
				//�ݹ���ж�����
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
