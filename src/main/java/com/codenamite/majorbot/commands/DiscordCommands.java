package com.codenamite.majorbot.commands;

import com.codenamite.majorbot.commands.interfaces.Command;
import com.codenamite.majorbot.request.weather.WeatherApiClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class DiscordCommands {

    private static final Map<String, Command> commands = new HashMap<>();

    @Inject
    private WeatherApiClient weatherApiClient;

    public void putCommandsIntoRegistry() {
        commands.put("ping", event -> event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage("Pong!"))
                .then());
        commands.put("pogoda", event -> Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .flatMap(command -> event.getMessage().getChannel().flatMap(channel -> channel.createMessage(
                        weatherApiClient.fetchWeatherInfo(command.get(1)))))
                .then());
    }



    public void registerCommands(GatewayDiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(event -> Mono.just(event.getMessage().getContent())
                        .flatMap(content -> Flux.fromIterable(commands.entrySet())
                                .filter(entry -> content.startsWith('!' + entry.getKey()))
                                .flatMap(entry -> entry.getValue().execute(event))
                                .next()))
                .subscribe();
    }

}
