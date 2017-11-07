# sso_crossdomain

### 配置 HOSTS 
127.0.0.1 sso.test.com

127.0.0.1 my.web.com


### 跨域测试
1、先登录  sso.test.com:8080/index.html  跨域 my.web.com:8090/index.html

2、访问 my.web.com:8090/index.html 自动跨域


### 跨域登录（原理）
--------------------------------------------

![GitHub](https://raw.githubusercontent.com/leqwang/kisso/master/images/cl.jpg "Kisso,crossdomain login")

### 异常处理
java.lang.IllegalArgumentException: An invalid domain [.x.com] was specified for this cookie

在写SSO同父域登录时使用
    sso.test.com
    my.web.com
在cookie共享时使用ck.setDomain(".x.com");//将cookie设置到父域下
发生错误  java.lang.IllegalArgumentException: An invalid domain [.x.com] was specified for this cookie
对此我很木纳随后找资料发现是因为我使用的tomcat版本过高  （这里我使用的是tomcat8.5）
在8.5版本以后的tomcat对应cookie的处理不一样了
cookie name的规则。 
Rfc6265CookieProcessor源码的167-197行代码如下

 private void validateDomain(String domain) {
        int i = 0;
        int prev = -1;
        int cur = -1;
        char[] chars = domain.toCharArray();
        while (i < chars.length) {
            prev = cur;
            cur = chars[i];
            if (!domainValid.get(cur)) {
                throw new IllegalArgumentException(sm.getString(
                        "rfc6265CookieProcessor.invalidDomain", domain));
            }
            // labels must start with a letter or number
            if ((prev == '.' || prev == -1) && (cur == '.' || cur == '-')) {
                throw new IllegalArgumentException(sm.getString(
                        "rfc6265CookieProcessor.invalidDomain", domain));
            }
            // labels must end with a letter or number
            if (prev == '-' && cur == '.') {
                throw new IllegalArgumentException(sm.getString(
                        "rfc6265CookieProcessor.invalidDomain", domain));
            }
            i++;
        }
        // domain must end with a label
        if (cur == '.' || cur == '-') {
            throw new IllegalArgumentException(sm.getString(
                    "rfc6265CookieProcessor.invalidDomain", domain));
        }
    }


domain规则如下 
1、必须是1-9、a-z、A-Z、. 、- （注意是-不是_）这几个字符组成 
2、必须是数字或字母开头

上篇文章使用.test.com报错就是因为使用”.”开头
3、必须是数字或字母结尾

path的规则源码及规则

 private void validatePath(String path) {
        char[] chars = path.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch < 0x20 || ch > 0x7E || ch == ';') {
                throw new IllegalArgumentException(sm.getString(
                        "rfc6265CookieProcessor.invalidPath", path));
            }
        }
    }

1、字符必须是在 0x20-0x7E之间，并且不能出现”;”号

cookie value 源码及规则

private void validateCookieValue(String value) {
        int start = 0;
        int end = value.length();

        if (end > 1 && value.charAt(0) == '"' && value.charAt(end - 1) == '"') {
            start = 1;
            end--;
        }

        char[] chars = value.toCharArray();
        for (int i = start; i < end; i++) {
            char c = chars[i];
            if (c < 0x21 || c == 0x22 || c == 0x2c || c == 0x3b || c == 0x5c || c == 0x7f) {
                throw new IllegalArgumentException(sm.getString(
                        "rfc6265CookieProcessor.invalidCharInValue", Integer.toString(c)));
            }
        }
    }

1、会自动去除开头和结尾的引号” 
2、如果包含以下规则字符则校验失败： 
c < 0x21 || c == 0x22 || c == 0x2c || c == 0x3b || c == 0x5c || c == 0x7f

对此的解决方案是：
指定完整的domain信息，但是这样单点登录就会有问题了
        Cookie cookie = new Cookie("testCookie", "test");
        cookie.setDomain("cml.test.com");
        cookie.setPath("/");
        cookie.setMaxAge(36000);
        resp.addCookie(cookie);

2.设置为一级域名（推荐）

        Cookie cookie = new Cookie("testCookie", "test");
        cookie.setDomain("test.com");
        cookie.setPath("/");
        cookie.setMaxAge(36000);
        resp.addCookie(cookie);
