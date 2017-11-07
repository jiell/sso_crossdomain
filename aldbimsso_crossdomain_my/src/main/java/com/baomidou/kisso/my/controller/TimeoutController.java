/**
 * Copyright (c) 2011-2014, jiel (liujie_sun@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.kisso.my.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 跨域超时
 * <p>
 * @author   jiel
 * @date	 2017年11月05日
 * @version  1.0.0
 */
@Controller
public class TimeoutController extends BaseController {

	/**
	 * 跨域登录超时
	 */
	@RequestMapping("/timeout")
	public String timeout() {
		return "timeout";
	}
}