package cn.meteor.module.core.freemarker;

import java.io.IOException;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jagregory.shiro.freemarker.ShiroTags;

import cn.meteor.module.core.freemarker.directive.ObjInCollectionDirective;
import cn.meteor.module.core.freemarker.directive.ResourceNamesDirective;
import cn.meteor.module.core.freemarker.tags.MTags;
import freemarker.template.TemplateException;

public class MeteorTagFreeMarkerConfigurer extends FreeMarkerConfigurer {

	@Override  
    public void afterPropertiesSet() throws IOException, TemplateException {  
        super.afterPropertiesSet();  
        this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
        this.getConfiguration().setSharedVariable("m", new MTags());
//        this.getConfiguration().setSharedVariable("upper", new UpperDirective());
//        this.getConfiguration().setSharedVariable("organizationName", new OrganizationNameDirective());
//        this.getConfiguration().setSharedVariable("roleNames", new RoleNamesDirective());
        this.getConfiguration().setSharedVariable("resourceNames", new ResourceNamesDirective());
        this.getConfiguration().setSharedVariable("objInCollection", new ObjInCollectionDirective());
    }
}
