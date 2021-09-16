package MeanShift;
/**
 * 用于存放聚类划分完成之后的数据结构
 *
 * @param dataIndex 当前数据的下标
 * @param fatherIndex 当前数据的父节点
 */
public class OutPutDataStruct {
	int dataIndex=0;
	int fatherIndex=0;
	public OutPutDataStruct(int data,int father) {
		dataIndex=data;
		fatherIndex=father;
	}
	

}
