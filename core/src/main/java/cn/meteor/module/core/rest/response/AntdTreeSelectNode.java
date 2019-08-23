package cn.meteor.module.core.rest.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AntdTreeSelectNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String id;
	 
	private String parentId;
	
	private List<AntdTreeSelectNode> children;
	
	
	
	/**
	 * 是否可选
	 */
	private boolean selectable = true;
	
	/**
	 * 禁掉 checkbox
	 */
	private boolean disableCheckbox = false;
	
	/**
	 * 是否禁用
	 */
	private boolean disabled = false;
	
	/**
	 * 是否是叶子节点
	 */
	private boolean isLeaf = false;
	
	/**
	 * 此项必须设置（其值在整个树范围内唯一）	类型：string | number	-
	 */
	@JsonProperty("key")
	private String key;

	/**
	 * 树节点显示的内容	类型：string|slot	'---'
	 */
	private String title;	

	/**
	 * 默认根据此属性值进行筛选（其值在整个树范围内唯一）	类型：string	-
	 */
	private String value;
	
	/**
	 * 使用treeData时，可以通过该属性配置支持slot的属性，如 scopedSlots: { title: 'XXX'}  类型：object
	 */
//	private Map<String,String> scopedSlots;
	
	
//	private String slotTitle;
//	private String icon;	
//	private Integer ruleFlag;
//	private String label;
	
	
	public AntdTreeSelectNode() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<AntdTreeSelectNode> getChildren() {
		return children;
	}

	public void setChildren(List<AntdTreeSelectNode> children) {
		this.children = children;
	}

	public boolean isSelectable() {
		return selectable;
	}

	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	public boolean isDisableCheckbox() {
		return disableCheckbox;
	}

	public void setDisableCheckbox(boolean disableCheckbox) {
		this.disableCheckbox = disableCheckbox;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

//	public Map<String, String> getScopedSlots() {
//		return scopedSlots;
//	}
//
//	public void setScopedSlots(Map<String, String> scopedSlots) {
//		this.scopedSlots = scopedSlots;
//	}
	
	
	
	
}
