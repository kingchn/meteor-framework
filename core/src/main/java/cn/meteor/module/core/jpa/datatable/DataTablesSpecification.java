package cn.meteor.module.core.jpa.datatable;

import java.util.Arrays;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import cn.meteor.module.core.jpa.datatable.criteria.DataTablePaginationCriteria;
import cn.meteor.module.core.jpa.datatable.criteria.DataTablePaginationInputCriteria.ColumnCriterias;
import cn.meteor.module.core.jpa.datatable.criteria.DataTablePaginationInputCriteria.SearchCriterias;



/**
 * columns[0][searchValue]=a 代表 =a
 * columns[0][searchValue]=like_a 代表 like ‘%a%'
 * columns[0][searchValue]=a+b+c 代表 in(a,b,c)
 * 
 * @author shenjc
 *
 * @param <T>
 */
public class DataTablesSpecification<T> implements Specification<T> {	
	//import org.springframework.data.jpa.datatables.repository.DataTablesUtils.DataTablesPageRequest;
	//import org.springframework.data.jpa.datatables.repository.SpecificationFactory;
	//参考org.springframework.data.jpa.datatables.repository.SpecificationFactory
	
//    private final DataTablesInput input;
//
//    public DataTablesSpecification(DataTablesInput input) {
//      this.input = input;
//    }
	
	
	public final static String OR_SEPARATOR = "+";
	public final static String ESCAPED_OR_SEPARATOR = "\\+";
	public final static String ATTRIBUTE_SEPARATOR = ".";
	public final static String ESCAPED_ATTRIBUTE_SEPARATOR = "\\.";
	public final static char ESCAPE_CHAR = '\\';
	
	public final static String LIKE_SEARCH = "like_";
	
	public static boolean isBoolean(String filterValue) {
		return "TRUE".equalsIgnoreCase(filterValue) || "FALSE".equalsIgnoreCase(filterValue);
	}

	public static String getLikeFilterValue(String filterValue) {
		return "%" + filterValue.toLowerCase().replaceAll("%", "\\\\" + "%").replaceAll("_", "\\\\" + "_") + "%";
	}
	
	
    private final DataTablePaginationCriteria criterias;//Criteria 查询：是一种类型安全和更面向对象的查询		Criteria 标准

    public DataTablesSpecification(DataTablePaginationCriteria criterias) {
      this.criterias = criterias;
    }
    
    private static <S> Expression<S> getExpression(Root<?> root, String columnData, Class<S> clazz) {// 中间的参数为columns[0][data]即字段名
        if (!columnData.contains(ATTRIBUTE_SEPARATOR)) {//如果字段名不包含.   说明就是属性，所有没什么特别的要处理
          return root.get(columnData).as(clazz);	//返回，例如：root.get("companyName").as(String.class)
        }
        // 如果columnData 是类似 "joinedEntity.attribute" 则 join 语句 ========================>待验证
        String[] values = columnData.split(ESCAPED_ATTRIBUTE_SEPARATOR);
        if (root.getModel().getAttribute(values[0])
            .getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
          // with @Embedded attribute
          return root.get(values[0]).get(values[1]).as(clazz);
        }
        From<?, ?> from = root;
        for (int i = 0; i < values.length - 1; i++) {
          from = from.join(values[i], JoinType.LEFT);
        }
        return from.get(values[values.length - 1]).as(clazz);
      }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//      Predicate p1=cb.like(root.get("companyName").as(String.class), "%" +"a"+"%");
//      return p1;
    	
      Predicate predicate = cb.conjunction();		//predicate 断言
      Expression<Boolean> booleanExpression;
      Expression<String> stringExpression;

      for (Map<ColumnCriterias, String> column : criterias.getColumns()) {//遍历所有columns
        String filterValue = column.get(ColumnCriterias.searchValue);			//取columns[0][search][value]
        boolean isColumnSearchable = "true".equals(column.get(ColumnCriterias.searchable)) && StringUtils.hasText(filterValue);	
        //如果columns[0][searchable]是true，并且columns[0][search][value]有值
        if (!isColumnSearchable) {//否则跳过
          continue;
        }

        if (filterValue.contains(OR_SEPARATOR)) {//如果columns[0][search][value]包含+
          // 这个过滤包含多个值, 添加一个 'WHERE .. IN' 语句
          String[] values = filterValue.split(ESCAPED_OR_SEPARATOR);//按+分隔
          if (values.length > 0 && isBoolean(values[0])) {//如果过滤值是 true 或者 false
            Object[] booleanValues = new Boolean[values.length];
            for (int i = 0; i < values.length; i++) {
              booleanValues[i] = Boolean.valueOf(values[i]);
            }
            booleanExpression = getExpression(root, column.get(ColumnCriterias.data), Boolean.class);
            predicate = cb.and(predicate, booleanExpression.in(booleanValues));
          } else {
            stringExpression = getExpression(root, column.get(ColumnCriterias.data), String.class);
            predicate = cb.and(predicate, stringExpression.in(Arrays.asList(values)));
          }
        } else if(filterValue.startsWith(LIKE_SEARCH)) {//如果columns[0][search][value],变种columns[0][searchValue]不包含+而是包含like；这种过滤说明只包含一个值，添加WHERE .. LIKE语句
        		filterValue = filterValue	.replaceFirst(LIKE_SEARCH, "");
				if (isBoolean(filterValue)) { // 如果过滤值是 true 或者 false
					booleanExpression = getExpression(root, column.get(ColumnCriterias.data), Boolean.class);// 中间的参数为columns[0][data]即字段名
					predicate = cb.and(predicate, cb.equal(booleanExpression, Boolean.valueOf(filterValue)));
				} else {
					stringExpression = getExpression(root, column.get(ColumnCriterias.data), String.class);// 中间的参数为columns[0][data]即字段名
					predicate = cb.and(predicate, cb.like(cb.lower(stringExpression), getLikeFilterValue(filterValue), ESCAPE_CHAR));
					//getLikeFilterValue(filterValue) 差不多是"%" +"a"+"%"； ESCAPE_CHAR是转移符
				}
        } else {//其他，即精确查找 添加WHERE .. =语句
			if (isBoolean(filterValue)) { // 如果过滤值是 true 或者 false
				booleanExpression = getExpression(root, column.get(ColumnCriterias.data), Boolean.class);// 中间的参数为columns[0][data]即字段名
				predicate = cb.and(predicate, cb.equal(booleanExpression, Boolean.valueOf(filterValue)));
			} else {
				stringExpression = getExpression(root, column.get(ColumnCriterias.data), String.class);// 中间的参数为columns[0][data]即字段名
//				predicate = cb.and(predicate, cb.like(cb.lower(stringExpression), getLikeFilterValue(filterValue), ESCAPE_CHAR));
				//getLikeFilterValue(filterValue) 差不多是"%" +"a"+"%"； ESCAPE_CHAR是转移符
//				Predicate p2 = cb.equal(root.get("abc").as(String.class), abc);
				predicate = cb.and(predicate, cb.equal(stringExpression, filterValue) );
			}
    }
      }

      // 检测全局过滤值是否存在
      String globalFilterValue = criterias.getSearch().get(SearchCriterias.value);		//search[value]:
      if (StringUtils.hasText(globalFilterValue)) {//如果全局过滤值有值
        Predicate matchOneColumnPredicate = cb.disjunction();
        // 在每个字段都添加 WHERE .. LIKE语句
        for (Map<ColumnCriterias, String> column : criterias.getColumns()) {
          if ("true".equals(column.get(ColumnCriterias.searchable)) ) {			//如果columns[0][searchable]是true
            Expression<String> expression = getExpression(root, column.get(ColumnCriterias.data), String.class);

            matchOneColumnPredicate = cb.or(matchOneColumnPredicate,
                cb.like(cb.lower(expression), getLikeFilterValue(globalFilterValue), ESCAPE_CHAR));
          }
        }
        predicate = cb.and(predicate, matchOneColumnPredicate);
      }
      
      
      // findAll method does a count query first, and then query for the actual data. Yet in the
      // count query, adding a JOIN FETCH results in the following error 'query specified join
      // fetching, but the owner of the fetched association was not present in the select list'
      // see https://jira.spring.io/browse/DATAJPA-105
//      ========================>未理解，待验证
      boolean isCountQuery = query.getResultType() == Long.class;
      if (isCountQuery) {
        return predicate;
      }
      // add JOIN FETCH when necessary
      for (Map<ColumnCriterias, String> column : criterias.getColumns()) {
        boolean isJoinable = "true".equals(column.get(ColumnCriterias.searchable)) && column.get(ColumnCriterias.data).contains(ATTRIBUTE_SEPARATOR);
        //如果字段是searchable为true，并且字段名包含.
        if (!isJoinable) {//否则跳过
          continue;
        }
        String[] values = column.get(ColumnCriterias.data).split(ESCAPED_ATTRIBUTE_SEPARATOR);//按.分隔
        if (root.getModel().getAttribute(values[0]).getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {//如果   则跳过
          continue;
        }
        Fetch<?, ?> fetch = null;
        for (int i = 0; i < values.length - 1; i++) {
          fetch = (fetch == null ? root : fetch).fetch(values[i], JoinType.LEFT);
        }
      }
      
      return predicate;
    }
  }