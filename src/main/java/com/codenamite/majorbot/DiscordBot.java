package com.codenamite.majorbot;

import com.codenamite.majorbot.commands.DiscordCommands;
import com.codenamite.majorbot.events.DiscordEvents;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
class DiscordBot {

    @Value("${api.discord.key}")
    private String keyId;

    @Inject
    private DiscordEvents discordEvents;
    @Inject
    DiscordCommands discordCommands;

    @EventListener
    void onApplicationEvent(ServerStartupEvent event) throws IOException {

        GatewayDiscordClient client =
                DiscordClientBuilder.create(keyId)
                        .build()
                        .login()
                        .block();
        assert client != null;
        discordEvents.createEvents(client);
        discordCommands.putCommandsIntoRegistry();
        discordCommands.registerCommands(client);
        client.onDisconnect().block();
    }
}
