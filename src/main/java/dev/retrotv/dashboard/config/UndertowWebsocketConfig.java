package dev.retrotv.dashboard.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;

/*
 * Undertow WebSocketDeploymentInfo가 설정되지 않아 기본 버퍼 풀이 사용된다는 경고 로그가 출력되는 것을 방지하는 설정
 * 참조: https://kdev.ing/spring-boot-undertow/
 */
@Component
public class UndertowWebsocketConfig implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
    private final ServerProperties.Undertow undertow;

    public UndertowWebsocketConfig(final ServerProperties serverProperties) {
        this.undertow = serverProperties.getUndertow();
    }

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            boolean direct = this.undertow.getDirectBuffers() != null && this.undertow.getDirectBuffers();
            int bufferSize = this.undertow.getBufferSize() != null ? (int) this.undertow.getBufferSize().toBytes() : 1024;
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(direct, bufferSize));
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
        });
    }    
}
