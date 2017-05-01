package cn.meteor.module.util.office;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.meteor.module.util.lang.StringExtUtils;

public class ExcelUtils {
	
	private final static String excel2003L =".xls";    //2003- 版本的excel  
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel
    

    public  static Workbook getWorkbook(InputStream inputStream,String fileName) throws Exception{  
		Workbook workbook = null;  
        String fileType = fileName.substring(fileName.lastIndexOf("."));  
        if(excel2003L.equals(fileType)){  
            workbook = new HSSFWorkbook(inputStream);  //2003-  
        }else if(excel2007U.equals(fileType)){  
            workbook = new XSSFWorkbook(inputStream);  //2007+  
        }else{  
            throw new Exception("解析的文件格式有误！");  
        }  
        return workbook;  
    }
    
    public static List<List<Object>> getRowListFromWorkbook(Workbook workbook, Map<Short, String> headerMapper) throws Exception {
    	return getRowListFromWorkbook(workbook, headerMapper, null);
    }

	
	public static List<List<Object>> getRowListFromWorkbook(Workbook workbook, Map<Short, String> headerMapper, Integer fetchRowSize) throws Exception {
		List<List<Object>> rowList = new ArrayList<List<Object>>();
		if (null == workbook) {
			throw new Exception("创建Excel工作薄为空！");
		}
		// 遍历Excel中所有的sheet
		for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
			Sheet sheet = workbook.getSheetAt(sheetIndex);
			if (sheet == null) {
				continue;
			}

			int fetchRowMaxIndex = sheet.getLastRowNum();
			if(fetchRowSize!=null) {
				fetchRowMaxIndex = fetchRowSize-1;
			}
			// 遍历当前sheet中的所有行
			for (int rowNum = sheet.getFirstRowNum(); rowNum <=sheet.getLastRowNum() && rowNum<=fetchRowMaxIndex; rowNum++) {//下标从0开始
				Row row = sheet.getRow(rowNum);
				if (row == null) {// 如果是null行，则跳过
					continue;
				}
				
				if (sheetIndex == 0 && rowNum == row.getFirstCellNum()) {// 如果是第一个sheet的第一行，添加到map中
					// 遍历所有的列
					for (short colIndex = row.getFirstCellNum(); colIndex <row.getLastCellNum(); colIndex++) {//下标从0开始，但是getLastCellNum等于列数，很奇怪，所有这里只能<不能<=
						Cell cell = row.getCell(colIndex);
						if(cell == null) {
					     continue;
					   }
						String headCellValue = getCellStringValue(cell);
//						String cellValue = StringExtUtils.translateForSnakeCase(headCellValue);
						headerMapper.put(colIndex, headCellValue);//map中保留原始表头字段名称
					}
					
				} else {// 否则，添加到集合中
					if(rowNum != row.getFirstCellNum()) {//如果不是第一行,多个sheet第一行都不作为数据录入
						// 遍历所有的列
						List<Object> oneRow = new ArrayList<Object>();
						for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
							Cell cell = row.getCell(cellNum);
							oneRow.add(getCellValue(cell));
						}
						rowList.add(oneRow);
					}
					
				}

			}
		}
		workbook.close();
		return rowList;
	}	

	public static <T_T> List<T_T> readBean(Workbook workbook, Class<T_T> tClass) throws Exception {
		List<T_T> beans = new ArrayList<T_T>();
		
		Map<Short, String> headerMapper = new HashMap<Short, String>();
		List<List<Object>> rowList = getRowListFromWorkbook(workbook, headerMapper);
		
		for (int rowIndex = 0; rowIndex < rowList.size(); rowIndex++) {//遍历所有行
			List<Object> oneRow = rowList.get(rowIndex);
			T_T bean = tClass.newInstance();
			for (short colIndex = 0; colIndex < oneRow.size(); colIndex++) {//遍历一行所有单元格
				String headCellValue = headerMapper.get(colIndex);
//				String fieldName = DzdzFpxxYdkExcelRequest.translateFieldName(headCellValue);
				Method translateFieldNameMethod = tClass.getSuperclass().getDeclaredMethod("translateFieldName", String.class);
				String fieldName = (String) translateFieldNameMethod.invoke(bean, headCellValue);//调用该类的字段名称转换方法得到字段名称
				Object value = oneRow.get(colIndex);
				if(StringUtils.isNotEmpty(String.valueOf(value))) {//如果字段值非空，则设置对象属性值
					BeanUtils.setProperty(bean, fieldName, value);
				}
			}
			beans.add(rowIndex, bean);
		}
		return beans;
	}
	
	public static <T_T> List<T_T> readBean(InputStream inputStream,String fileName, Class<T_T> tClass) throws Exception {
		Workbook workbook = getWorkbook(inputStream, fileName);
		List<T_T> beans = ExcelUtils.readBean(workbook, tClass);
		return beans;
	}
	
	public static List<List<Object>> getExcelHeader(InputStream inputStream,String fileName, Map<Short, String> headerMapper) throws Exception {
		Workbook workbook = getWorkbook(inputStream, fileName);
//		Map<Short, String> headerMapper = new HashMap<Short, String>();
		List<List<Object>> rowList = getRowListFromWorkbook(workbook, headerMapper, 2);
		return rowList;
	}
	
	public static String getExcelClassFieldDefine(InputStream inputStream,String fileName) throws Exception {
		Map<Short, String> headerMapper = new HashMap<Short, String>();
		List<List<Object>> rowList = ExcelUtils.getExcelHeader(inputStream,fileName, headerMapper);
		List<Object> firstDataRow = rowList.get(0);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < firstDataRow.size(); i++) {
			Short key = Short.valueOf("" + i);
			String excelOriginHeader = headerMapper.get(key);
			if(StringUtils.isNotBlank(excelOriginHeader)) {
				String fieldName = StringExtUtils.translateForSnakeCase(excelOriginHeader);
				Object filedValue = firstDataRow.get(i);
				String filedType = filedValue.getClass().getSimpleName();
				sb.append("private ").append(filedType).append(" ").append(fieldName).append(";\n");
			}
		}
//		StringBuilder sb = new StringBuilder();
//		for (Short key : headerMapper.keySet()) {
//			String excelOriginHeader = headerMapper.get(key);
//			if(StringUtils.isNotBlank(excelOriginHeader)) {
//				String fieldName = StringExtUtils.translateForSnakeCase(excelOriginHeader);
//				sb.append("private String ").append(fieldName).append(";\n");
//			}
//		}
		return sb.toString();
	}

	public static  String getCellStringValue(Cell cell){
		Object cellValue = getCellValue(cell);
		String cellStringValue= null;
		if(cellValue!=null) {
			cellStringValue = "" + cellValue;
		} else {
			cellStringValue = "";
		}
		return cellStringValue;
	}
	
    
    /** 
     * 描述：对表格中数值进行格式化 
     * @param cell 
     * @return 
     */  
	public static Object getCellValue(Cell cell) {
		Object value = null;
//		DecimalFormat df = new DecimalFormat("0"); // 格式化number String字符
//		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); // 日期格式化
//		DecimalFormat df2 = new DecimalFormat("0.00"); // 格式化数字

		switch (cell.getCellTypeEnum()) {
		case STRING:
			value = cell.getRichStringCellValue().getString();
			break;
		case NUMERIC:
//			if ("General".equals(cell.getCellStyle().getDataFormatString())) {
//				value = df.format(cell.getNumericCellValue());
//			} else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString()) || "yyyy-mm-dd".equals(cell.getCellStyle().getDataFormatString())) {
//				value = sdf.format(cell.getDateCellValue());
//			} else {
//				value = df2.format(cell.getNumericCellValue());
//			}
			String dataFormatString = cell.getCellStyle().getDataFormatString();
//			if("yyyy\\-mm\\-dd".equals(dataFormatString) || "yyyy\\-mm\\-dd\\ h:mm:ss".equals(dataFormatString) || "m/d/yy".equals(dataFormatString)) {
			if(dataFormatString.contains("yy") || dataFormatString.contains("m") || dataFormatString.contains("d") || dataFormatString.contains("h") || dataFormatString.contains("ss")) {
				value = cell.getDateCellValue();
			} else {
				value = cell.getNumericCellValue();
			}
			break;
		case BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case BLANK:
			value = "";
			break;
		default:
			break;
		}
		return value;
	}
}
