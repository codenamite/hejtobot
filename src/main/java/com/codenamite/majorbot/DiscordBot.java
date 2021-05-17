package com.codenamite.majorbot;

import com.codenamite.majorbot.commands.DiscordCommands;
import com.codenamite.majorbot.events.DiscordEvents;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.event.annotation.EventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
class DiscordBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBot.class);

    @Value("${api.discord.key}")
    private String keyId;

    public DiscordBot(DiscordEvents discordEvents, DiscordCommands discordCommands) {
        this.discordEvents = discordEvents;
        this.discordCommands = discordCommands;
    }

    private final DiscordEvents discordEvents;
    private final DiscordCommands discordCommands;

    @EventListener
    void onApplicationEvent(ServerStartupEvent event) throws IOException {
        GatewayDiscordClient client =
                DiscordClientBuilder.create(keyId)
                        .build()
                        .login()
                        .block();
        assert client != null;
        LOGGER.info("Discord client started");
        discordEvents.createEvents(client);
        LOGGER.info("Events created");
        discordCommands.putCommandsIntoRegistry();
        LOGGER.info("Commands pushed to registry");
        discordCommands.registerCommands(client);
        LOGGER.info("Commands registered");
        client.onDisconnect().block();
    }
}
