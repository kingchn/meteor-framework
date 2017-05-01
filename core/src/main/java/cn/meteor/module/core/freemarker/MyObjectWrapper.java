package cn.meteor.module.core.freemarker;

import com.github.pagehelper.Page;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.Version;

/**
 * FreeMarker对象包装类
 * @author shenjc
 *
 */
public class MyObjectWrapper extends DefaultObjectWrapper {

	public MyObjectWrapper(Version incompatibleImprovements) {
        super(incompatibleImprovements);
    }
	
	@Override
	public TemplateModel wrap(Object obj) throws TemplateModelException {
		if (obj instanceof Page) {
			return new PageAdapter((Page<?>) obj, this);
        }
		return super.wrap(obj);
	}    

	@Override
	protected TemplateModel handleUnknownType(Object obj)
			throws TemplateModelException {
		if (obj instanceof Page) {
			return new PageAdapter((Page<?>) obj, this);
        }
		return super.handleUnknownType(obj);
	}
	
}
