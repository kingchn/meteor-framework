package cn.meteor.module.core.auth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.meteor.module.core.auth.domain.Resource;
import cn.meteor.module.core.auth.service.ResourceService;
import cn.meteor.module.core.auth.service.UserService;

@Controller
@RequestMapping("/resource")
public class ResourceController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ResourceService resourceService;
	
	

	@ModelAttribute("types")
	public Resource.ResourceType[] resourceTypes() {
		return Resource.ResourceType.values();
	}

	@RequiresPermissions("resource:view")
	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model,ServletRequest req) {// 起始页-资源首页-最外页
		String username = (String)SecurityUtils.getSubject().getPrincipal();
		Set<String> permissions = userService.findPermissions(username);
		List<Map<String, Object>> menus = resourceService.findMenus(permissions);		
		model.addAttribute("menus", menus);
		return "resource/resourceIndex";
	}

	@RequiresPermissions("resource:view")
	@RequestMapping(value = "/indexFrame", method = RequestMethod.GET)
	public String indexContent(Model model) {// 总框架页--这里里面对应的页面是独立的才能使其可以使用jquery的applyDemoStyles布局分隔页面
		return "resource/frame/resourceIndexFrame";
	}
	
	@RequiresPermissions("resource:view")
	@RequestMapping(value = "/indexFrame2", method = RequestMethod.GET)
	public String indexContent2(Model model) {// 总框架页--这里里面对应的页面是独立的才能使其可以使用jquery的applyDemoStyles布局分隔页面
		return "resource/frame/resourceIndexFrame2";
	}

	@RequiresPermissions("resource:view")
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public String showTree(Model model) {// 左侧资源树
		Object object = resourceService.findAll();
		model.addAttribute("resourceList", object);
		return "resource/frame/tree/resourceTree";
	}

	@RequiresPermissions("resource:create")
	@RequestMapping(value = "/{parentId}/createChild", method = RequestMethod.GET)
	public String showCreateChildForm(@PathVariable("parentId") Long parentId, Model model) {
		Map<String, Object> parent = resourceService.findOne(parentId);
		model.addAttribute("parent", parent);
		Map<String, Object> child = new HashMap<String, Object>();
		child.put("parentId", parentId);
		String childParentIds = "" + parent.get("parentIds") + parent.get("id") + "/";
		child.put("parentIds", childParentIds);
		model.addAttribute("child", child);
		model.addAttribute("op", "新增子节点");
		return "resource/frame/content/resourceCreateChild";
	}

	@RequiresPermissions("resource:create")
	@RequestMapping(value = "/{parentId}/createChild", method = RequestMethod.POST)
	public String createChild(@RequestParam Map<String, Object> allRequestParams,
			RedirectAttributes redirectAttributes) {
		allRequestParams.put("type", "menu");
		allRequestParams.put("available", "1");
		resourceService.createResource(allRequestParams);
		redirectAttributes.addFlashAttribute("msg", "新增子节点成功");
		return "redirect:/resource/operateSuccess";
	}

	@RequiresPermissions("resource:update")
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
	public String showUpdateForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("resource", resourceService.findOne(id));
		return "resource/frame/content/resourceEdit";
	}

	@RequiresPermissions("resource:update")
	@RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
	public String update(@RequestParam Map<String, Object> allRequestParams, RedirectAttributes redirectAttributes) {
		resourceService.updateResource(allRequestParams);
		redirectAttributes.addFlashAttribute("msg", "修改成功");
		return "redirect:/resource/operateSuccess";
	}

	@RequiresPermissions("resource:update")
	@RequestMapping(value = "/batchUpdateIndex", method = RequestMethod.POST)
	@ResponseBody
	public String batchUpdateResourceIndex(String ids, RedirectAttributes redirectAttributes) {
		if (ids == null)
			return "0";

		//将逗号隔开的资源id转化为资源id列表
		String[] arrString = ids.split(",");
		List<Long> resourceIds = new ArrayList<Long>();
		for (String s : arrString) {
			resourceIds.add(Long.valueOf(s));
		}
		resourceService.batchUpdateResourceIndex(resourceIds);	//将接收到的资源id列表按顺序更新其对应的index字段
		return "1";
	}

	@RequiresPermissions("resource:delete")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
		resourceService.deleteResource(id);
		redirectAttributes.addFlashAttribute("msg", "删除成功");
		return "redirect:/resource/operateSuccess";
	}

	@RequiresPermissions("resource:update")
	@RequestMapping(value = "/{sourceId}/move", method = RequestMethod.GET)
	public String showMoveForm(@PathVariable("sourceId") Long sourceId, Model model) {
		Map<String, Object> source = resourceService.findOne(sourceId);
		model.addAttribute("source", source);
		model.addAttribute("targetList", resourceService.findAllWithExclude(source));
		return "resource/frame/content/resourceMove";
	}

	@RequiresPermissions("resource:update")
	@RequestMapping(value = "/{sourceId}/move", method = RequestMethod.POST)
	public String move(@PathVariable("sourceId") Long sourceId, @RequestParam("targetId") Long targetId) {
		Map<String, Object> source = resourceService.findOne(sourceId);
		Map<String, Object> target = resourceService.findOne(targetId);
		resourceService.move(source, target);
		return "redirect:/resource/operateSuccess";
	}

	@RequiresPermissions("resource:view")
	@RequestMapping(value = "/operateSuccess", method = RequestMethod.GET)
	public String success() {
		return "resource/frame/content/resourceOperateSuccess";
	}

}
