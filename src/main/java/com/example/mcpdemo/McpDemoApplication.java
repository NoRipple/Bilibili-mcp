package com.example.mcpdemo;

import com.example.mcpdemo.service.FavoriteService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.tools.Tool;
import java.util.List;

@SpringBootApplication
public class McpDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpDemoApplication.class, args);
    }

    @Bean
    public List<ToolCallback> bilibiliTools(FavoriteService favoriteService) {
        return List.of(ToolCallbacks.from(favoriteService));
    }

}
