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
 * A RequestCondition to match a list of roles to the current user's roles.
 * Simply delegates to {@link HttpServletRequest#isUserInRole(String)}.
 */
public class RolesRequestCondition implements RequestCondition<RolesRequestCondition> {
	
	private final Set<String> roles;
	
	public RolesRequestCondition(String... roles) {
		this(Arrays.asList(roles));
	}
	
	private RolesRequestCondition(Collection<String> roles) {
		this.roles = Collections.unmodifiableSet(new HashSet<String>(roles));
	}

	@Override
	public RolesRequestCondition combine(RolesRequestCondition other) {
		Set<String> allRoles = new LinkedHashSet<String>(this.roles);
		allRoles.addAll(other.roles);
		return new RolesRequestCondition(allRoles);
	}

	@Override
	public RolesRequestCondition getMatchingCondition(HttpServletRequest request) {
		for (String role : this.roles) {
			if (!request.isUserInRole(role)) {
				return null;
			}
		}
		return this;
	}

	@Override
	public int compareTo(RolesRequestCondition other, HttpServletRequest request) {
		return other.roles.size() - this.roles.size();
	}

}
