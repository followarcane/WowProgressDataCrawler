package followarcane.wowdatacrawler.infrastructure.service.proxy;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Service
@Slf4j
public class ProxyRotator {

    private final Proxy wowProgressProxy;

    public ProxyRotator() {
        // Sabit WowProgress proxy'si
        this.wowProgressProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("45.87.120.175", 8080));
        testProxy();
    }

    private void testProxy() {
        try {
            log.info("Testing WowProgress proxy connection...");
            String ip = getIpThroughProxy(wowProgressProxy);
            log.info("WowProgress proxy IP: {}", ip);
        } catch (Exception e) {
            log.error("Failed to test WowProgress proxy", e);
        }
    }

    public Proxy getWowProgressProxy() {
        return wowProgressProxy;
    }

    private String getIpThroughProxy(Proxy proxy) throws IOException {
        return Jsoup.connect("https://api.ipify.org")
                .proxy(proxy)
                .ignoreContentType(true)
                .execute()
                .body();
    }
} 