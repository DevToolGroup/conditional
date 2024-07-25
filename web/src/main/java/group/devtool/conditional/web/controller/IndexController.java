package group.devtool.conditional.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/conditional/bootstrap")
public class IndexController {

	// 返回页面
	@RequestMapping(value = "/index")
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/rule")
	public ModelAndView rule() {
		return new ModelAndView("rule");
	}



}
