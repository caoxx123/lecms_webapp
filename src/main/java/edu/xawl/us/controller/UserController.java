package edu.xawl.us.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.xawl.common.entity.PageBean;
import edu.xawl.common.service.CommonService;
import edu.xawl.us.entity.UserBean;
import edu.xawl.us.service.UserService;

/**
 * 用户相关Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/UserController")
public class UserController {


	@Resource
	private SessionFactory sf;
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private UserService userService;
	
	@RequestMapping("/loginPage")
	public String loginPage(HttpServletRequest request,Model model){
		request.getSession().invalidate();
		return "/index/login";
	}
	
	@ResponseBody
	@RequestMapping("/login")
	public boolean login(UserBean user,HttpServletRequest req,Model model){
		HttpSession session = req.getSession();
		session.removeAttribute("user");
		UserBean loginUser = userService.login(user.getLoginName().trim(), DigestUtils.md5DigestAsHex(user.getPassWord().trim().getBytes()));
		System.out.println(user.getLoginName());
		if(loginUser!=null) {
			session.setAttribute("user", loginUser);
			return true;
		}else{
			return false;
		}
	}
	
	@ResponseBody
	@RequestMapping("/userExit")
	public boolean userExit(HttpServletRequest request){
		request.getSession().invalidate();
		return true;
	}
	
	
	@RequestMapping("/workIndex")
	public String workIndex(HttpServletRequest req,Model model){
		/*HttpSession session = req.getSession();
		UserBean user = (UserBean) session.getAttribute("user");
		if(user==null){
			model.addAttribute("loginMessage", "请先登陆！");
			return "/index/login";
		}*/
		return "/work/workIndex";
	}
	
	@RequestMapping("/userBaseInfo")
	public String userBaseInfo(){
		return "/user/userBase";
	}
	
	
	@RequestMapping("/UserList")
	public String UserList(PageBean<UserBean> pb,String role,Model model){
		if(pb!=null){
			pb = userService.findAllUserByLeval(pb,role);
		}
		model.addAttribute("pageBean", pb);
		return "/user/userList";
	}
	
	
	@RequestMapping("/editUser")
	public String editUser(UserBean userBean,String op,Model model){
		model.addAttribute("op", op);
		return "/user/editUserPage";
	}
	
	@RequestMapping("/deleteUser")
	public String deleteUser(UserBean user,String role,Model model){
		user = (UserBean) commonService.findById(UserBean.class, user.getId());
		user.setDeleted(!user.isDeleted());
		commonService.merge(user);
		
		PageBean pb = new PageBean();
		pb = userService.findAllUserByLeval(pb,role);
		model.addAttribute("pageBean", pb);
		return "/user/userList";
	}
}