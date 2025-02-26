package followarcane.wowdatacrawler.infrastructure.service.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

@Slf4j
@Service
public class TorProxyManager {

    private static final String TOR_HOST = "127.0.0.1";
    private static final int TOR_SOCKS_PORT = 9050;
    private static final int TOR_CONTROL_PORT = 9051;

    public Proxy getProxy() {
        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(TOR_HOST, TOR_SOCKS_PORT));
    }

    public void newIdentity() {
        try (Socket socket = new Socket(TOR_HOST, TOR_CONTROL_PORT)) {
            String auth = "AUTHENTICATE \"\"\r\n";
            String newIdentity = "SIGNAL NEWNYM\r\n";

            socket.getOutputStream().write(auth.getBytes());
            socket.getOutputStream().write(newIdentity.getBytes());
            socket.getOutputStream().flush();

            byte[] response = new byte[1024];
            socket.getInputStream().read(response);

            log.info("Tor identity changed successfully");
        } catch (IOException e) {
            log.error("Failed to change Tor identity", e);
        }
    }

    public boolean testConnection() {
        try (Socket testSocket = new Socket(TOR_HOST, TOR_SOCKS_PORT)) {
            return true;
        } catch (IOException e) {
            log.error("Tor proxy is not available", e);
            return false;
        }
    }
} 