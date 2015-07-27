package io.honeybadger.reporter.dto;

import com.sun.deploy.net.HttpUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.utils.HttpClientUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.net.CookieManager;
import java.util.Enumeration;
import java.util.LinkedHashMap;

/**
 * CGI parameters passed to the server when the error occurred.
 * @author <a href="https://github.com/dekobon">Elijah Zupancic</a>
 * @since 1.0.9
 */
public class CgiData extends LinkedHashMap<String, Object> 
        implements Serializable {
    private static final long serialVersionUID = 1006793090880571738L;

    public CgiData(HttpServletRequest request) {
        addCgiParams(request);
    }

    void addCgiParams(HttpServletRequest request) {
        put("REQUEST_METHOD", request.getMethod());
        put("HTTP_ACCEPT", request.getHeader(HttpHeaders.ACCEPT));
        put("HTTP_USER_AGENT", request.getHeader(HttpHeaders.USER_AGENT));
        put("HTTP_ACCEPT_ENCODING", request.getHeader(HttpHeaders.ACCEPT_ENCODING));
        put("HTTP_ACCEPT_LANGUAGE", request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
        put("HTTP_ACCEPT_CHARSET", request.getHeader(HttpHeaders.ACCEPT_CHARSET));
        put("HTTP_COOKIE", parseCookies(request));
        put("SERVER_NAME", request.getServerName());
        put("SERVER_PORT", request.getServerPort());
        put("CONTENT_TYPE", request.getContentType());
        put("CONTENT_LENGTH", request.getContentLength());
        put("REMOTE_ADDR", request.getRemoteAddr());
        put("REMOTE_PORT", request.getRemotePort());
        put("QUERY_STRING", request.getQueryString());
        put("PATH_INFO", request.getPathInfo());
    }

    static String parseCookies(HttpServletRequest request) {
        Enumeration<String> cookies = request.getHeaders("Set-Cookie");

        if (cookies == null || !cookies.hasMoreElements()) return null;

        StringBuilder builder = new StringBuilder();

        while (cookies.hasMoreElements()) {
            String c = cookies.nextElement();
            if (c == null) continue;

            builder.append(c.trim());

            if (cookies.hasMoreElements()) {
                builder.append("; ");
            }
        }

        return builder.toString();
    }
}
