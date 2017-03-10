package com.czequered.promocodes.controller;

import com.czequered.promocodes.model.Game;
import com.czequered.promocodes.service.GameService;
import com.czequered.promocodes.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.czequered.promocodes.config.Constants.TOKEN_HEADER;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author Martin Varga
 */
@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    private GameService gameService;

    private TokenService tokenService;

    @Autowired
    public GameController(GameService gameService, TokenService tokenService) {
        this.gameService = gameService;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/list",
        method = GET,
        produces = APPLICATION_JSON_VALUE)
    public HttpEntity<List<Game>> list(@RequestHeader(name = TOKEN_HEADER) String token) {
        String userIdFromToken = tokenService.getUserIdFromToken(token);
        List<Game> games = gameService.getGames(userIdFromToken);
        return new HttpEntity<>(games);
    }

    @RequestMapping(value = "/{gameId}",
        method = GET,
            produces = APPLICATION_JSON_VALUE)
    public HttpEntity<Game> getGame(@RequestHeader(name = TOKEN_HEADER) String token,
                                    @PathVariable("gameId") String gameId) {
        String userIdFromToken = tokenService.getUserIdFromToken(token);
        Game game = gameService.getGame(userIdFromToken, gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }
        return new HttpEntity<>(game);
    }

    @RequestMapping(method = POST,
        produces = APPLICATION_JSON_VALUE)
    public HttpEntity<Game> saveNewGame(@RequestBody Game game) {
        if (game.getGameId() != null) {
            throw new InvalidRequestException();
        }
        Game saveGame = gameService.saveGame(game);
        return new HttpEntity<>(saveGame);
    }

    @RequestMapping(method = PUT,
        produces = APPLICATION_JSON_VALUE)
    public HttpEntity<Game> saveExistingGame(@RequestBody Game game) {
        Game saveGame = gameService.saveGame(game);
        return new HttpEntity<>(saveGame);
    }
}