package cn.meteor.module.core.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.meteor.module.core.auth.service.ResourceService;
import cn.meteor.module.core.auth.service.RoleService;
import cn.meteor.module.core.auth.service.UserService;

@Controller
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ResourceService resourceService;

	@RequiresPermissions("role:view")
	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model) {// 起始页-角色首页-角色列表页
		String username = (String)SecurityUtils.getSubject().getPrincipal();
		Set<String> permissions = userService.findPermissions(username);
		List<Map<String, Object>> menus = resourceService.findMenus(permissions);
		model.addAttribute("menus", menus);
		return "role/roleList";
	}

	@RequiresPermissions("role:view")
	@RequestMapping(value = "/listAjax", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody Map<String, Object> listAjax(Model model, ServletRequest request,
			@RequestParam(value = "iDisplayStart", defaultValue = "0") int iDisplayStart,
			@RequestParam(value = "iDisplayLength", defaultValue = "10") int iDisplayLength,
			@RequestParam(value = "sortType", defaultValue = "auto") String sortType) {
		int pageSize = iDisplayLength;
		int pageNumber = iDisplayStart / pageSize + 1;
		// Page<Company> companyList = roleService.list(searchParams, pageNumber, pageSize, sortType);
		List<Map<String, Object>> roles = roleService.findAll();
		String sEcho = request.getParameter("sEcho");
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("aaData", roles);
		map.put("data", roles);
		// map.put("aaData",companyList.getContent());
		// map.put("iTotalRecords",companyList.getTotalElements());
		// map.put("iTotalDisplayRecords", companyList.getTotalElements());
		map.put("sEcho", sEcho);
		return map;
	}

	@RequiresPermissions("role:create")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String showCreateForm(Model model) {
		Map<String, Object> role = new HashMap<String, Object>();
		role.put("available", true);
		model.addAttribute("role", role);			//设置role实体到model中
		setResourceListDataToModel(model);	//把所有的资源列表设置到model中，用于选择资源
		model.addAttribute("op", "新增");			//设置操作类型
		return "role/roleEdit";
	}

	@RequiresPermissions("role:create")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@RequestParam Map<String, Object> allRequestParams, RedirectAttributes redirectAttributes) {
		roleService.createRole(allRequestParams);
		redirectAttributes.addFlashAttribute("msg", "新增成功");
		return "redirect:/role";
	}

	@RequiresPermissions("role:update")
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
	public String showUpdateForm(Model model, @PathVariable("id") Long id) {
		Map<String, Object> role = roleService.findOne(id);	//根据传来的id查询得到role实体
		model.addAttribute("role", role);			//设置role实体到model中
		setResourceListDataToModel(model);	//把所有的资源列表设置到model中，用于选择资源
		model.addAttribute("op", "修改");			//设置操作类型
		return "role/roleEdit";
	}

	@RequiresPermissions("role:update")
	@RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
	public String update(@RequestParam Map<String, Object> allRequestParams, RedirectAttributes redirectAttributes) {
		roleService.updateRole(allRequestParams);
		redirectAttributes.addFlashAttribute("msg", "修改成功");
		return "redirect:/role";
	}

	@RequiresPermissions("role:delete")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
	public String showDeleteForm(@PathVariable("id") Long id, Model model) {
		Map<String, Object> role = roleService.findOne(id);
		model.addAttribute("role", role);
		setResourceListDataToModel(model);
		model.addAttribute("op", "删除");
		return "role/roleEdit";
	}

	@RequiresPermissions("role:delete")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		roleService.deleteRole(id);
		redirectAttributes.addFlashAttribute("msg", "删除成功");
		return "redirect:/role";
	}

	/**
	 * 把所有的资源列表设置到model中
	 * @param model
	 */
	private void setResourceListDataToModel(Model model) {
		List<Map<String, Object>> resourceList = resourceService.findAll();
		model.addAttribute("resourceList", resourceList);
	}

}
