package boaz.site.boazback.common.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class HeaderUtil {

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();

            String value = request.getHeader(key);
            System.out.println(key+" -  "+value);
            map.put(key, value);
        }

        return map;
    }


    // token 추출하기
    public String extractHeaderFromRequest(HttpServletRequest request, String name){
        Map<String, String> headers = getHeadersInfo(request);
        String value = headers.get(name);
        if(value != null) {
            return value;
        }
        return null;
    }

}
