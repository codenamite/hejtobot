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
import java.util.List;
import java.util.Map;

@Singleton
public class DiscordEvents {

    @Inject
    private WeatherApiClient weatherApiClient;
    private static Map<String, String> commands;

    public void createEvents(GatewayDiscordClient client) {
        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(readyEvent -> {
                    User self = readyEvent.getSelf();
                    System.out.printf(
                            "Logged in as %s#%s%n", self.getUsername(), self.getDiscriminator());
                });
        registerResponse(client, "Cześć!", "Hej ;)");
        registerResponse(client, "Bocie jedz", "https://tenor.com/wH0h.gif");
        registerResponse(client, "Bocie pogoda", weatherApiClient.fetchWeatherInfo("Zagreb"));
        registerRandomResponse(client, MessageValues.RANDOM.values);

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

    private void registerRandomResponse(GatewayDiscordClient client, List<String> answers) {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().startsWith("Random!"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(RandomListElement.randomize(answers)))
                .subscribe();
    }

}
