package cn.meteor.module.core.openApi.annotation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

/**
 * 
 */
public class HandlersRequestCondition implements RequestCondition<HandlersRequestCondition> {
	
	private final Set<String> handlers;
	
	public HandlersRequestCondition(String... handlers) {
		this(Arrays.asList(handlers));
	}
	
	private HandlersRequestCondition(Collection<String> handlers) {
		this.handlers = Collections.unmodifiableSet(new HashSet<String>(handlers));
	}

	@Override
	public HandlersRequestCondition combine(HandlersRequestCondition other) {
		Set<String> allRoles = new LinkedHashSet<String>(this.handlers);
		allRoles.addAll(other.handlers);
		return new HandlersRequestCondition(allRoles);
	}

	@Override
	public HandlersRequestCondition getMatchingCondition(HttpServletRequest request) {
//		for (String role : this.roles) {
//			if (!request.isUserInRole(role)) {
//				return null;
//			}
//		}
//		return this;
		if(this.handlers.size()>0) {
			for (String handler : this.handlers) {
				if(handler.equals(request.getAttribute("handler"))) {
					return this;
				}
			}
		} else {//对于没有设定handlers，默认通过
			return this;
		}
		return null;
	}

	@Override
	public int compareTo(HandlersRequestCondition other, HttpServletRequest request) {
		return other.handlers.size() - this.handlers.size();
	}

}
