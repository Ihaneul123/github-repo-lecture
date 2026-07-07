package com.kyh.system.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kyh.system.model.Syain;
import com.kyh.system.model.User;
import com.kyh.system.service.SettingService;
import com.kyh.system.service.SyainService;
import com.kyh.system.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	static final int pageSize = 10;

	@Autowired
	private UserService userService;
	
	@Autowired
	private SettingService settingService;
	
	@Autowired
	private SyainService syainService;

	// 社員管理 初期表示
	@RequestMapping(value = "/userinfomation", method = RequestMethod.GET) 
	public ModelAndView userinfomation(HttpSession session) {
		ModelAndView model = new ModelAndView(); 
		model.addObject("companyList",settingService.getSettingsByCategory1AndCategory3(1, 1)); 
		model.addObject("jobList", settingService.getSettingsByCategory1AndCategory2(3, 4)); 
		model.addObject("syainList", new ArrayList<>());
		model.addObject("companyMap", settingService.getSettingMapByCategory1AndCategory3(1, 1));
		model.addObject("genderMap", settingService.getSettingMapByCategory1AndCategory2(3, 1)); 
		model.addObject("jobMap", settingService.getSettingMapByCategory1AndCategory2(3, 4)); 
		
		model.addObject("selectedCompanyId", null);
		model.addObject("selectedEmployeeName", "");
		model.addObject("selectedJobKind", 4);
		model.addObject("selectedWorking", true);
		model.addObject("selectedRetired", false);
		
		model.setViewName("/common/information"); return model; 
	}
	
	// 社員管理検索
	@RequestMapping(value = "/userinfomation", method = RequestMethod.POST)
	public ModelAndView searchSyain(
	        @RequestParam(required = false) Integer companyId,
	        @RequestParam(required = false) String employeeName,
	        @RequestParam(required = false) Integer jobKind,
	        @RequestParam(required = false, defaultValue = "false") boolean working,
	        @RequestParam(required = false, defaultValue = "false") boolean retired,
	        HttpSession session) {
	    ModelAndView model = new ModelAndView();

	    model.addObject("companyList", settingService.getSettingsByCategory1AndCategory3(1, 1));
	    model.addObject("jobList", settingService.getSettingsByCategory1AndCategory2(3, 4));
	    model.addObject("syainList",
	            syainService.search(companyId, employeeName, jobKind, working, retired));
	    model.addObject("companyMap", settingService.getSettingMapByCategory1AndCategory3(1, 1));
	    model.addObject("genderMap", settingService.getSettingMapByCategory1AndCategory2(3, 1));
	    model.addObject("jobMap", settingService.getSettingMapByCategory1AndCategory2(3, 4));

	    model.addObject("selectedCompanyId", companyId);
	    model.addObject("selectedEmployeeName", employeeName);
	    model.addObject("selectedJobKind", jobKind);
	    model.addObject("selectedWorking", working);
	    model.addObject("selectedRetired", retired);
	    
	    model.setViewName("/common/information");
	    return model;
	}
	
	// 個人情報のタグ
	@RequestMapping(value = "/myInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView myInfo(HttpSession session) {
		ModelAndView model = new ModelAndView();
		User user = (User) session.getAttribute("user");
		User userLogined = userService.getUserByPrimaryKey(user.getNo());
		session.setAttribute("user", userLogined);
		model.addObject("user", userLogined);
		model.setViewName("/common/myInfo");
		return model;
	}

	//ユーザー一覧を初期化する際、ユーザー全量の情報とその数をマップに入れておく
	@PostMapping(value = "/userinforlist")
	@ResponseBody
	public Map<String, Object> userinforlist(HttpServletRequest request) {
		int page = Integer.parseInt(request.getParameter("page"));
		int pageSize = Integer.parseInt(request.getParameter("rows")); // pageSize
		int startRecord = (page - 1) * pageSize + 1;
		int total = userService.getCount();
		List<User> userinforlist = userService.selectAll(startRecord, pageSize);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", total - 1);
		resultMap.put("rows", userinforlist);
		return resultMap;
	}

	// 個人情報更新時（パスワードの更新は右上）
	@PostMapping(value = "/updateMyInfo")
	@ResponseBody
	public Map<String, String> updateMyInfo(
			@RequestParam("no") Integer no,
			@RequestParam("username") String username,
			@RequestParam("phone") String phone, HttpSession session) {
		Map<String, String> result = new HashMap<>();

		User user = new User();
		user.setNo(no);
		user.setUsername(username);
		user.setPhone(phone);
		try {
			userService.update(user);
			session.setAttribute("user", userService.getUserByPrimaryKey(no));
			result.put("success", "true");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// ユーザー情報の追加
	@PostMapping(value = "/add")
	@ResponseBody
	public Map<String, String> saveUsers(
			@RequestParam("userid") String userid,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("phone") String phone, HttpSession session) {

		Map<String, String> map = new HashMap<>();
		User user = new User();
		user.setUserid(userid);
		user.setPassword(password);
		user.setUsername(username);
		user.setPhone(phone);
		try {
			if (userService.checkExistenceByUserId(user) > 0) {
				map.put("msg", "ユーザーは既に存在しているため、追加できません。");
			} else {
				userService.insert(user);
				map.put("success", "true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	// ユーザー情報の削除
	@PostMapping(value = "/delete")
	@ResponseBody
	public Map<String, String> removeUsers(@RequestParam("no") Integer no, HttpSession session) {
		Map<String, String> result = new HashMap<>();
		if (((User) session.getAttribute("user")).getNo().equals(no)) {
			result.put("msg", "現在ロングインしているユーザーは削除できません。");
			return result;
		}
		try {
			userService.delete(no);
			result.put("success", "true");
			System.out.println("削除No: " + no);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "エラーが発生しました。");
		}
		return result;
	}

	// ユーザー情報の更新
	@PostMapping(value = "/update")
	@ResponseBody
	public Map<String, String> update(
			@RequestParam("no") Integer no,
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("phone") String phone, HttpSession session) {

		Map<String, String> map = new HashMap<>();
		User user = new User();
		user.setNo(no);
		user.setPassword(password);
		user.setUsername(username);
		user.setPhone(phone);
		try {
			userService.update(user);
			map.put("success", "true");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	// 社員情報の更新
	@RequestMapping(value = "/updateSyain", method = RequestMethod.GET)
	public ModelAndView updateSyain(@RequestParam("syainId") Integer syainId) {
	    ModelAndView model = new ModelAndView();

	    Syain syain = syainService.selectByPrimaryKey(syainId);

	    model.addObject("syain", syain);
	    model.addObject("companyList", settingService.getSettingsByCategory1AndCategory3(1, 1));
	    model.addObject("jobList", settingService.getSettingsByCategory1AndCategory2(3, 4));

	    model.setViewName("/common/update");
	    return model;
	}
	
	// 社員新規登録画面へ遷移
	@RequestMapping(value = "/registerSyain", method = RequestMethod.GET)
	public ModelAndView registerSyain() {
	    ModelAndView model = new ModelAndView();
	    
	    model.addObject("companyList", settingService.getSettingsByCategory1AndCategory3(1, 1));
	    model.addObject("jobList", settingService.getSettingsByCategory1AndCategory2(3, 4));
	    model.addObject("osList", settingService.getSettingsByCategory1AndCategory2(3, 6));
	    
	    model.setViewName("/common/registerSyain");
	    return model;
	}
	
	// 社員新規登録
	@PostMapping("/registerSyain")
	public ModelAndView registerSyainPost(
	        @RequestParam("firstNameKanji") String firstNameKanji,
	        @RequestParam("lastNameKanji") String lastNameKanji,
	        @RequestParam("firstNameKana") String firstNameKana,
	        @RequestParam("lastNameKana") String lastNameKana,
	        @RequestParam("firstNameEigo") String firstNameEigo,
	        @RequestParam("lastNameEigo") String lastNameEigo,
	        @RequestParam("seibetu") Integer seibetu,
	        @RequestParam("syozokuKaisya") Integer syozokuKaisya,
	        @RequestParam(required = false) String nyuusyaDate,
	        @RequestParam(required = false) String taisyaDate,
	        @RequestParam("syokugyoKind") Integer syokugyoKind,
	        @RequestParam(required = false) List<Integer> itOsId,
	        @RequestParam(required = false) List<String> itOsLevel,
	        @RequestParam(required = false) String kinyukikanCode,
	        @RequestParam(required = false) String kinyukikanName,
	        @RequestParam(required = false) String sitenCode,
	        @RequestParam(required = false) String sitenName,
	        @RequestParam(required = false) String kouzaNum,
	        @RequestParam(required = false) String meigiName) {

	    try {
	        Syain syain = new Syain();
	        syain.setFirstNameKanji(firstNameKanji);
	        syain.setLastNameKanji(lastNameKanji);
	        syain.setFirstNameKana(firstNameKana);
	        syain.setLastNameKana(lastNameKana);
	        syain.setFirstNameEigo(firstNameEigo);
	        syain.setLastNameEigo(lastNameEigo);
	        syain.setSeibetu(seibetu);
	        syain.setSyozokuKaisya(syozokuKaisya);
	        syain.setSyokugyoKind(syokugyoKind);
	        syain.setKinyukikanCode(kinyukikanCode);
	        syain.setKinyukikanName(kinyukikanName);
	        syain.setSitenCode(sitenCode);
	        syain.setSitenName(sitenName);
	        syain.setKouzaNum(kouzaNum);
	        syain.setMeigiName(meigiName);

	        StringBuilder itOs = new StringBuilder();

	        if (itOsId != null && itOsLevel != null) {
	            for (int i = 0; i < itOsId.size(); i++) {
	                if (itOsLevel.get(i) != null && !itOsLevel.get(i).isEmpty()) {

	                    if (itOs.length() > 0) {
	                        itOs.append(",");
	                    }

	                    itOs.append(itOsId.get(i))
	                        .append("-")
	                        .append(itOsLevel.get(i));
	                }
	            }
	        }

	        syain.setItOs(itOs.toString());
	        
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	        if (nyuusyaDate != null && !nyuusyaDate.isEmpty()) {
	            syain.setNyuusyaDate(sdf.parse(nyuusyaDate));
	        }
	        if (taisyaDate != null && !taisyaDate.isEmpty()) {
	            syain.setTaisyaDate(sdf.parse(taisyaDate));
	        }
	        
	        Date now = new Date();
	        syain.setDeleteFlag(0);
	        syain.setTourokubi(now);
	        syain.setKousinnbi(now);
	        syainService.insert(syain);
	        return new ModelAndView("redirect:/user/userinfomation?success=1");
	        
	        } catch (Exception e) {
	            e.printStackTrace();

	            ModelAndView mv = new ModelAndView("/common/registerSyain");
	            mv.addObject("error", "登録に失敗しました。");

	            mv.addObject("companyList", settingService.getSettingsByCategory1AndCategory3(1, 1));
	            mv.addObject("jobList", settingService.getSettingsByCategory1AndCategory2(3, 4));
	            mv.addObject("osList", settingService.getSettingsByCategory1AndCategory2(3, 6));

	            return mv;
	        }
	}
	
	
	// 社員削除
	@PostMapping("/deleteSyain")
	@ResponseBody
	public Map<String, String> deleteSyain(
	        @RequestParam("syainId") Integer syainId) {

	    Map<String, String> map = new HashMap<>();

	    try {
	        syainService.delete(syainId);
	        map.put("success", "true");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return map;
	}
	
	// 右上のパスワード変更機能
	@PostMapping(value = "/modifypassword")
	@ResponseBody
	public Map<String, String> modifypassword(
			@RequestParam("no") int no,
			@RequestParam("oldpassword") String oldpassword,
			@RequestParam("newpassword1") String newpassword1,
			@RequestParam("newpassword2") String newpassword2, HttpSession session) {
		Map<String, String> result = new HashMap<>();
		User userLogined = (User) session.getAttribute("user");
		if (oldpassword == null || "".equals(oldpassword)) {
			result.put("msg", "現在のパスワードを入力してください。");
			return result;
		} else if (!userLogined.getPassword().equals(oldpassword)) {
			result.put("msg", "現在のパスワードが正しくありません");
			return result;
		} else if (newpassword1 == null || "".equals(newpassword1)) {
			result.put("msg", "新しいパスワードを入力してください。");
			return result;
		} else if (newpassword2 == null || "".equals(newpassword2)) {
			result.put("msg", "確認用パスワードを入力してください。");
			return result;
		} else if (!newpassword2.equals(newpassword1)) {
			result.put("msg", "２回入力したパスワードが一致しません。");
			return result;
		} else if (userLogined.getPassword().equals(newpassword1)) {
			result.put("msg", "前回のパスワードと同じものは設定できません。");
			return result;
		}

		User user = new User();
		user.setNo(no);
		user.setPassword(newpassword2);
		try {
			userService.update(user);
			session.setAttribute("user", userService.getUserByPrimaryKey(no));			
			result.put("success", "true");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("msg", "変更が失敗しました。");
		return result;
	}

	@PostMapping("/updateSyain")
	public ModelAndView updateSyainPost(
	        @RequestParam("syainId") Integer syainId,
	        @RequestParam("firstNameKanji") String firstNameKanji,
	        @RequestParam("lastNameKanji") String lastNameKanji,
	        @RequestParam("firstNameKana") String firstNameKana,
	        @RequestParam("lastNameKana") String lastNameKana,
	        @RequestParam("firstNameEigo") String firstNameEigo,
	        @RequestParam("lastNameEigo") String lastNameEigo,
	        @RequestParam("seibetu") Integer seibetu,
	        @RequestParam("syozokuKaisya") Integer syozokuKaisya,
	        @RequestParam(required = false) String nyuusyaDate,
	        @RequestParam(required = false) String taisyaDate,
	        @RequestParam("syokugyoKind") Integer syokugyoKind) {

	    try {
	        Syain syain = new Syain();

	        syain.setSyainId(syainId);
	        syain.setFirstNameKanji(firstNameKanji);
	        syain.setLastNameKanji(lastNameKanji);
	        syain.setFirstNameKana(firstNameKana);
	        syain.setLastNameKana(lastNameKana);
	        syain.setFirstNameEigo(firstNameEigo);
	        syain.setLastNameEigo(lastNameEigo);
	        syain.setSeibetu(seibetu);
	        syain.setSyozokuKaisya(syozokuKaisya);
	        syain.setSyokugyoKind(syokugyoKind);

	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	        if (nyuusyaDate != null && !nyuusyaDate.isEmpty()) {
	            syain.setNyuusyaDate(sdf.parse(nyuusyaDate));
	        }

	        if (taisyaDate != null && !taisyaDate.isEmpty()) {
	            syain.setTaisyaDate(sdf.parse(taisyaDate));
	        }

	        syain.setKousinnbi(new Date());

	        syainService.update(syain);

	        return new ModelAndView("redirect:/user/userinfomation?updateSuccess=1");

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ModelAndView("redirect:/user/userinfomation?updateError=1");
	    }
	}
	
	// ログアウト
	@RequestMapping(value = "/exit", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView exit(HttpSession session) {
		session.removeAttribute("user");
		ModelAndView model = new ModelAndView();
		model.setViewName("redirect:/login/");
		return model;
	}
}
