package cn.meteor.module.core.jpa.datatable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import cn.meteor.module.core.jpa.datatable.criteria.DataTablePaginationCriteria;
import cn.meteor.module.core.jpa.datatable.criteria.DataTablePaginationInputCriteria.ColumnCriterias;
import cn.meteor.module.core.jpa.datatable.criteria.DataTablePaginationInputCriteria.OrderCriterias;

public class DataTableCriteriaUtils {

	public static <T> Specification<T> buildSpecification(DataTablePaginationCriteria criteria) {
		return new DataTablesSpecification<T>(criteria);
	}
	
	
	/**
	   * Creates a 'LIMIT .. OFFSET .. ORDER BY ..' clause for the given {@link DataTablesInput}.
	   * 
	   * @param input the {@link DataTablesInput} mapped from the Ajax request
	   * @return a {@link Pageable}, must not be {@literal null}.
	   */
	  public static Pageable buildPageable(DataTablePaginationCriteria criteria) {
	    List<Order> orders = new ArrayList<Order>();
	    if(criteria.getOrder() !=null ) {//如果开启了排序
	    	for (Map<OrderCriterias, String> order :  criteria.getOrder() ) {
		    	Integer orderColumnIndex = Integer.valueOf(order.get(OrderCriterias.column));			//order[0][column]:0		    	order[0][dir]:asc
		    	Map<ColumnCriterias, String> column = criteria.getColumns().get(orderColumnIndex);
		    	
		      if ("true".equals(column.get(ColumnCriterias.orderable))) {//如果该字段是开启排序的
		        String sortColumn = column.get(ColumnCriterias.data);//取字段名
		        Direction sortDirection = Direction.fromString(order.get(OrderCriterias.dir));//取字段排序
		        orders.add(new Order(sortDirection, sortColumn));
		      }
		    }
	    }
	    
	    Sort sort = orders.isEmpty() ? null : new Sort(orders);

	    if (criteria.getLength() == -1) {
	    	criteria.setStart(0);
	    	criteria.setLength(Integer.MAX_VALUE);
	    }
	    Pageable pageable = new PageRequest(criteria.getPageNumber() - 1, criteria.getPageSize(), sort);
	    return pageable;
	  }
	  
	  
	  
}
