package br.com.Icompras.pedidos.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "br.com.Icompras.pedidos.client")
public class ClientsConfig {
}
