package boaz.site.boazback.common.filter;


import boaz.site.boazback.common.exception.GeoIpException;
import boaz.site.boazback.common.util.HttpReqResUtils;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.net.InetAddress;

@Component
@Profile("prd")
@Slf4j
@RequiredArgsConstructor
public class IpAuthenticationFilter implements Filter {

    private final DatabaseReader databaseReader;
    private final HttpReqResUtils httpReqResUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ipAddress = httpReqResUtils.getClientIpAddressIfServletRequestExist();
        InetAddress inetAddress = InetAddress.getByName(ipAddress);
        String country = null;
        try {
            country = databaseReader.country(inetAddress).getCountry().getName();
        } catch (GeoIp2Exception e) {
            log.error(e.getMessage());
        }

        if(country == null || !country.equals("South Korea")){
            log.info("Access Rejected : {}, {}", ipAddress, country);
            throw GeoIpException.GEO_IP_FORBIDDEN;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("IP Authentication Filter Init..");
    }

    @Override
    public void destroy() {
        log.info("IP Authentication Filter Destroy..");
    }
}
