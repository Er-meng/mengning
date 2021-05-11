package cn.mn.mn;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class TokenUtil {
    private final String KEY_TOKEN = "token";

    public String getToken(HttpServletRequest httpServletRequest) {
        String result = null;
        if (httpServletRequest == null) {
            return result;
        }

        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals(this.KEY_TOKEN)) {
                    result = cookie.getValue();
                    break;
                }
            }
        }

        if (result == null) {
            result = httpServletRequest.getHeader(this.KEY_TOKEN);
        }
        return result;
    }
}
