package cn.meteor.module.core.openApi.response;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"code", "msg", "data", "recordsTotal", "recordsFiltered"})
@JsonInclude(Include.NON_NULL)
public class DataTableResponse<T> extends BaseResponse {
	
//	private Page<T> page;

//	public Page<T> getPage() {
//		return page;
//	}

	public void setPage(Page<T> page) {
//		this.page = page;
		this.setData(page.getContent());
		this.setRecordsTotal(page.getTotalElements());
		this.setRecordsFiltered(page.getTotalElements());
	}

	
//	  /**
//	   * The draw counter that this object is a response to - from the draw parameter sent as part of
//	   * the data request. Note that it is strongly recommended for security reasons that you cast this
//	   * parameter to an integer, rather than simply echoing back to the client what it sent in the draw
//	   * parameter, in order to prevent Cross Site Scripting (XSS) attacks.
//	   */
//	  protected int draw;

	  /**
	   * Total records, before filtering (i.e. the total number of records in the database)
	   */
	  protected long recordsTotal = 0L;

	  /**
	   * Total records, after filtering (i.e. the total number of records after filtering has been
	   * applied - not just the number of records being returned for this page of data).
	   */
	  protected long recordsFiltered = 0L;

	  /**
	   * The data to be displayed in the table. This is an array of data source objects, one for each
	   * row, which will be used by DataTables. Note that this parameter's name can be changed using the
	   * ajaxDT option's dataSrc property.
	   */
	  protected List<?> data = Collections.emptyList();
	  
	  

//	public int getDraw() {
//		return draw;
//	}
//
//	public void setDraw(int draw) {
//		this.draw = draw;
//	}

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public long getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}
	  
	  
}
