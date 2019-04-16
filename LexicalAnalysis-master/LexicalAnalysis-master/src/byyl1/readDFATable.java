package byyl1;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class readDFATable {
   // public static void main(String[] args) throws Exception {}

    /**

     * ��ȡExcel�����ݣ���һά����洢����һ���и��е�ֵ����ά����洢���Ƕ��ٸ���

     * @param file ��ȡ���ݵ�ԴExcel

     * @param ignoreRows ��ȡ���ݺ��Ե�������������ͷ����Ҫ���� ���Ե�����Ϊ1

     * @return ������Excel�����ݵ�����

     * @throws FileNotFoundException

     * @throws IOException

     */

    public static String[][] getData(File file, int ignoreRows)

           throws FileNotFoundException, IOException {

       List<String[]> result = new ArrayList<String[]>();

       int rowSize = 0;

       BufferedInputStream in = new BufferedInputStream(new FileInputStream(

              file));

       // ��HSSFWorkbook

       POIFSFileSystem fs = new POIFSFileSystem(in);

       HSSFWorkbook wb = new HSSFWorkbook(fs);

       HSSFCell cell = null;

       for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {

           HSSFSheet st = wb.getSheetAt(sheetIndex);

           // ��һ��Ϊ���⣬��ȡ

           for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {

              HSSFRow row = st.getRow(rowIndex);

              if (row == null) {

                  continue;

              }

              int tempRowSize = row.getLastCellNum() + 1;

              if (tempRowSize > rowSize) {

                  rowSize = tempRowSize;

              }

              String[] values = new String[rowSize];

              Arrays.fill(values, "");

              boolean hasValue = false;

              for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {

                  String value = "";

                  cell = row.getCell(columnIndex);

                  if (cell != null) {

                     // ע�⣺һ��Ҫ��������������ܻ��������

                     cell.setEncoding(HSSFCell.ENCODING_UTF_16);

                     switch (cell.getCellType()) {

                     case HSSFCell.CELL_TYPE_STRING:

                         value = cell.getStringCellValue();

                         break;

                     case HSSFCell.CELL_TYPE_NUMERIC:

                         if (HSSFDateUtil.isCellDateFormatted(cell)) {

                            Date date = cell.getDateCellValue();

                            if (date != null) {

                                value = new SimpleDateFormat("yyyy-MM-dd")

                                       .format(date);

                            } else {

                                value = "";

                            }

                         } else {

                            value = new DecimalFormat("0").format(cell

                                   .getNumericCellValue());

                         }

                         break;

                     case HSSFCell.CELL_TYPE_FORMULA:

                         // ����ʱ���Ϊ��ʽ���ɵ���������ֵ

                         if (!cell.getStringCellValue().equals("")) {

                            value = cell.getStringCellValue();

                         } else {

                            value = cell.getNumericCellValue() + "";

                         }

                         break;

                     case HSSFCell.CELL_TYPE_BLANK:

                         break;

                     case HSSFCell.CELL_TYPE_ERROR:

                         value = "";

                         break;

                     case HSSFCell.CELL_TYPE_BOOLEAN:

                         value = (cell.getBooleanCellValue() == true ? "Y"

                                : "N");

                         break;

                     default:

                         value = "";

                     }

                  }

                  if (columnIndex == 0 && value.trim().equals("")) {

                     break;

                  }

                  values[columnIndex] = rightTrim(value);

                  hasValue = true;

              }

 

              if (hasValue) {

                  result.add(values);

              }

           }

       }

       in.close();

       String[][] returnArray = new String[result.size()][rowSize];

       for (int i = 0; i < returnArray.length; i++) {

           returnArray[i] = (String[]) result.get(i);

       }

       return returnArray;

    }

   

    /**

     * ȥ���ַ����ұߵĿո�

     * @param str Ҫ������ַ���

     * @return �������ַ���

     */

     public static String rightTrim(String str) {

       if (str == null) {

           return "";

       }

       int length = str.length();

       for (int i = length - 1; i >= 0; i--) {

           if (str.charAt(i) != 0x20) {

              break;

           }

           length--;

       }

       return str.substring(0, length);

    }
     public DFATable[] addDFA() throws Exception {
 		
    	 File file = new File("1.xls");

         String[][] result = getData(file, 0);

         int rowLength = result.length;
         DFATable dfa[] =new DFATable[663];
         int x=0;
         for(int i=1;i<rowLength;i++) {
             for(int j=1;j<result[i].length-2;j++) {
  	    	  dfa[x]=new DFATable();
  	    	   dfa[x].setState(Integer.parseInt(result[i][0]));
  	    	   String[] strArray = null;  
  	           strArray = result[0][j].split(" ");  
  	    	   dfa[x].setInput(strArray);
  	    	   dfa[x].setNextState(Integer.parseInt(result[i][j]));
          	  // System.out.print(result[i][j]+" ");
          	   x=x+1;
             }

           //  System.out.println();

         }
         for(int i=0;i<dfa.length;i++)
         {
        	 if(dfa[i].getState()==1)
 			{
 				 dfa[i].setType("��ʶ��");
 			}
        	 if(dfa[i].getState()>=2&&dfa[i].getState()<=7)
  			{
  				 dfa[i].setType("����");
  			}
        	 if(dfa[i].getState()>=8&&dfa[i].getState()<=11)
  			{
  				 dfa[i].setType("ע��");
  			}
        	 if(dfa[i].getState()>=12&&dfa[i].getState()<=18||dfa[i].getState()>=20&&dfa[i].getState()<=28)
   			{
   				 dfa[i].setType("�����");
   			}
        	 if(dfa[i].getState()==29||dfa[i].getState()==19)
    			{
    				 dfa[i].setType("���");
    			}
         }
         
 		return dfa;
 	}
     public String[][] showDFA() throws Exception{
    	 File file = new File("1.xls");

         String[][] result = getData(file, 0);

         /*int rowLength = result.length;
         for(int i=1;i<rowLength;i++) {
             for(int j=1;j<result[i].length-2;j++) {
          	   System.out.print(result[i][j]+" ");
             }
             System.out.println();
         }*/
         return result;
     }
     public DFATableState[] showDFAState() throws Exception{
    	 File file = new File("2.xls");
         String[][] result = getData(file, 0);
         int rowLength = result.length;
         
         DFATableState state[] =new DFATableState[39];
         int x=0;
         for(int i=0;i<rowLength;i++) {
        	     state[i]=new DFATableState();
            	 state[i].setState(Integer.parseInt(result[i][0]));
            	 state[i].setFinish(result[i][1].equals("1")?true:false);
            	 state[i].setType(result[i][2]);
            	 //System.out.println(state[i].getState()+"  "+state[i].getType()+"  "+state[i].isFinish());
         }
         return state;
     }

}