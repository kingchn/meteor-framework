package cn.meteor.module.core.openApi;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import cn.meteor.module.core.openApi.annotation.RestJsonRootName;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

//@JsonAutoDetect(value=JsonMethod.FIELD)
//@JsonSerialize(include=Inclusion.NON_NULL)
//@JsonPropertyOrder({"id","name","bookTelephone","address","picPath","recommendDishes","cuisineFirstId","cuisineFirstName","cuisineSecondId","cuisineSecondName","introduction","avgCost","popularValue","trafficRoute","businessTime","serviceInfo","orderSupport","dishOrderSupport","queueSupport","hasCoupon","hasGroupBuy","companyImages","coupons","dishes","distance"})

//@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonPropertyOrder({"data", "recordsTotal", "recordsFiltered"})
@JsonInclude(Include.NON_NULL)
//@RestJsonRootName("dataTablesOutput")
@RestJsonRootName
public class DataTablesOutput {

		  /**
		   * The draw counter that this object is a response to - from the draw parameter sent as part of
		   * the data request. Note that it is strongly recommended for security reasons that you cast this
		   * parameter to an integer, rather than simply echoing back to the client what it sent in the draw
		   * parameter, in order to prevent Cross Site Scripting (XSS) attacks.
		   */
		  @JsonView(View.class)
		  private int draw;

		  /**
		   * Total records, before filtering (i.e. the total number of records in the database)
		   */
		  @JsonView(View.class)
		  private long recordsTotal = 0L;

		  /**
		   * Total records, after filtering (i.e. the total number of records after filtering has been
		   * applied - not just the number of records being returned for this page of data).
		   */
		  @JsonView(View.class)
		  private long recordsFiltered = 0L;

		  /**
		   * The data to be displayed in the table. This is an array of data source objects, one for each
		   * row, which will be used by DataTables. Note that this parameter's name can be changed using the
		   * ajaxDT option's dataSrc property.
		   */
		  @JsonView(View.class)
		  private List<?> data = Collections.emptyList();

		  /**
		   * Optional: If an error occurs during the running of the server-side processing script, you can
		   * inform the user of this error by passing back the error message to be displayed using this
		   * parameter. Do not include if there is no error.
		   */
		  @JsonView(View.class)
		  private String error;

		  public interface View {
		  }

		  public int getDraw() {
		    return draw;
		  }

		  public void setDraw(int draw) {
		    this.draw = draw;
		  }

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

		  public String getError() {
		    return error;
		  }

		  public void setError(String error) {
		    this.error = error;
		  }

		  @Override
		  public String toString() {
		    return "DataTablesOutput [draw=" + draw + ", recordsTotal=" + recordsTotal
		        + ", recordsFiltered=" + recordsFiltered + ", error=" + error + "]";
		  }
		  
}
