package TFIP.Day26v2.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import TFIP.Day26v2.Model.Game;
import TFIP.Day26v2.Model.ShowGames;
import TFIP.Day26v2.Repo.Repo;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@RestController
public class GameController {

    @Autowired
    private Repo repo;

    @GetMapping("/games")
    public ResponseEntity<String> showAllGames(@RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "25") Integer limit) {

        List<Game> getAllGames = repo.getAllGames(offset, limit);
        ShowGames allGames = new ShowGames();
        allGames.setAllGames(getAllGames);
        allGames.setOffset(offset);
        allGames.setLimit(limit);
        allGames.setTotal(getAllGames.size());
        allGames.setTimestamp(LocalDate.now());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(allGames.toJson().toString());

    }

    @GetMapping("/games/rank")
    public ResponseEntity<String> showAllGamesByRank(@RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "25") Integer limit) {

        List<Game> getAllGamesByRank = repo.getGamesByRank(offset, limit);
        ShowGames allGames = new ShowGames();
        allGames.setAllGames(getAllGamesByRank);
        allGames.setOffset(offset);
        allGames.setLimit(limit);
        allGames.setTotal(getAllGamesByRank.size());
        allGames.setTimestamp(LocalDate.now());

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(allGames.toJson().toString());

    }

    @GetMapping("/games/{game_id}")
    public ResponseEntity<String> showGameById(@PathVariable String game_id) {
       
        Game getGameById = null;
        try {
            getGameById = repo.getGameById(game_id);
            getGameById.setTimestamp(LocalDate.now());
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Unable to find game id");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getGameById.toJsonId().build().toString());

    }

}
