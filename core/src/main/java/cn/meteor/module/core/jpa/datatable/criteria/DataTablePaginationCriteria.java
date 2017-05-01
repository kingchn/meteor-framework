package cn.meteor.module.core.jpa.datatable.criteria;

public class DataTablePaginationCriteria extends DataTablePaginationInputCriteria {


    
	public int getPageSize() {
		return getLength();
	}
	
	public int getPageNumber() {
		if(getLength()==0)
			return 0;
		else
			return getStart()/getLength() + 1;
	}
}
