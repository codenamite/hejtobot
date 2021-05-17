package com.codenamite.majorbot.events;

import com.codenamite.majorbot.request.weather.WeatherApiClient;
import com.codenamite.majorbot.util.MessageValues;
import com.codenamite.majorbot.util.RandomListElement;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Singleton
public class DiscordEvents {

    @Inject
    private WeatherApiClient weatherApiClient;
    private static Map<String, String> commands;

    public void createEvents(GatewayDiscordClient client) throws IOException {
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(readyEvent -> {
                    User self = readyEvent.getSelf();
                    System.out.printf(
                            "Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                });
        registerResponse(client, "Majorze gówno", "Zjedz se równo");
        registerResponse(client, "Majorze test", "Ja sie stqd wprowadze!");
        registerResponse(client, "Majorze jedz", "https://tenor.com/ba6un.gif");
        registerResponse(client, "Majorze pogoda", weatherApiClient.fetchWeatherInfo("bialystok"));
        registerRandomResponse(client, "Majorze ile butelek nitro masz", MessageValues.NITRO.values);

    }

    private void registerResponse(GatewayDiscordClient client, String input, String output) {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().startsWith(input))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(output))
                .subscribe();
    }

    private void registerRandomResponse(GatewayDiscordClient client, String input, List<String> answers) {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().startsWith(input))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(RandomListElement.randomize(answers)))
                .subscribe();
    }

}
