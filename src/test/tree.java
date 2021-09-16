package test;

public class tree {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 BST<Integer, String> bst = new BST<>();
	        bst.insert(10, "a");
	        bst.insert(12, "b");
	        bst.insert(3, "d");
	        bst.insert(9, "cdd");
	        bst.insert(8, "a");
	     
	        bst.insert(1, "dddd");
	        
	        //从根开始打印
	        TreePrintUtil.pirnt(bst.getRoot());

	}

}
