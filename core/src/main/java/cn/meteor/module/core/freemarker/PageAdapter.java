package cn.meteor.module.core.freemarker;

import com.github.pagehelper.Page;

import freemarker.template.AdapterTemplateModel;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateSequenceModel;
import freemarker.template.WrappingTemplateModel;

/**
 * FreeMarker针对Page对象适配类
 * @author shenjc
 *
 */
public class PageAdapter extends WrappingTemplateModel implements
		TemplateSequenceModel, TemplateHashModelEx, AdapterTemplateModel {
	
	private final Page<?> page;

	public PageAdapter( Page<?> page, ObjectWrapper ow) {
        super(ow);  // coming from WrappingTemplateModel
        this.page = page;
    }

	@Override
	public Object getAdaptedObject(Class hint) {
		return page;
	}

	@Override
	public TemplateModel get(int index) throws TemplateModelException {
		return wrap(page.get(index));
	}

	@Override
	public int size() throws TemplateModelException {
		return page.size();
	}

	@Override
	public TemplateModel get(String key) throws TemplateModelException {
		if("pageNum".equals(key)) {
			return wrap(page.getPageNum());
		}
		if("pageSize".equals(key)) {
			return wrap(page.getPageSize());
		}
		if("startRow".equals(key)) {
			return wrap(page.getStartRow());
		}
		if("endRow".equals(key)) {
			return wrap(page.getEndRow());
		}
		if("pages".equals(key)) {
			return wrap(page.getPages());
		}
		if("total".equals(key)) {
			return wrap(page.getTotal());
		}
		return null;
	}

	@Override
	public boolean isEmpty() throws TemplateModelException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public TemplateCollectionModel keys() throws TemplateModelException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TemplateCollectionModel values() throws TemplateModelException {
		// TODO Auto-generated method stub
		return null;
	}

}
