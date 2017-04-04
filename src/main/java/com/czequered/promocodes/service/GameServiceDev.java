package com.czequered.promocodes.service;

import com.czequered.promocodes.model.Game;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Martin Varga
 */
@Service
@Profile("dev")
@Primary
public class GameServiceDev implements GameService {
    Map<Game, Game> localCache;

    public GameServiceDev() {
        localCache = new HashMap<>();
        Game game1 = new Game("Krtek", "GAME-1");
        game1.setDetails("{}");
        localCache.put(game1, game1);

        Game game2 = new Game("Krtek", "GAME-2");
        game2.setDetails("{}");
        localCache.put(game1, game1);

        Game game3 = new Game("Sova", "GAME-3");
        game3.setDetails("{}");
        localCache.put(game1, game1);
    }

    @Override
    public List<Game> getGames(String userId) {
        return localCache.values().stream()
                .filter(g -> g.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Game getGame(String userId, String gameId) {
        return localCache.get(new Game(userId, gameId));
    }

    @Override
    public void deleteGame(String userId, String gameId) {
        localCache.remove(new Game(userId, gameId));
    }

    @Override
    public Game saveGame(Game game) {
        localCache.put(game, game);
        return game;
    }
}