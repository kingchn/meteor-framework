package cn.meteor.module.util.lang;

import java.util.List;

public class TreeBase<T> {
	
    protected String id;
    
    protected String parentId;
    
    protected List<T> children;

    public TreeBase() {
    }

    public TreeBase(String id, String parent) {
        this.id = id;
        this.parentId = parent;
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

	public List<T> getChildren() {
		return children;
	}

	public void setChildren(List<T> children) {
		this.children = children;
	}
	
}
