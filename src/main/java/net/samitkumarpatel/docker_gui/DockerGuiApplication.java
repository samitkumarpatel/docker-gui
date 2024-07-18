package net.samitkumarpatel.docker_gui;

import io.netty.channel.unix.DomainSocketAddress;
import lombok.RequiredArgsConstructor;
import net.samitkumarpatel.docker_gui.models.Node;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;


@SpringBootApplication
public class DockerGuiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DockerGuiApplication.class, args);
	}

	@Bean
	WebClient webClient() {
		HttpClient client = HttpClient.create()
				.remoteAddress(() -> new DomainSocketAddress("/var/run/docker.sock"));
		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(client))
				.build();

	}

	@Bean
	RouterFunction<ServerResponse> route(RouteHandlers routeHandlers) {
		return RouterFunctions
				.route()
				.GET("/nodes", routeHandlers::nodes)
				.GET("/containers", routeHandlers::containers)
				.build();
	}

}

@Component
@RequiredArgsConstructor
class RouteHandlers {

	final WebClient webClient;

	public Mono<ServerResponse> containers(ServerRequest request) {
		return ServerResponse.noContent().build();
	}

	public Mono<ServerResponse> nodes(ServerRequest request) {
		return webClient
				.get()
				.uri("/v1.46/nodes")
				.retrieve()
				.bodyToMono(Node.class)
				.flatMap(ServerResponse.ok()::bodyValue);
	}
}