package TFIP.Day26v2.Repo;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import TFIP.Day26v2.Model.Game;

@Repository
public class Repo {

    @Autowired
    private MongoTemplate template;

    public List<Game> getAllGames(Integer offset, Integer limit) {

        Query query = new Query(
                new Criteria().andOperator(
                        // gt = greater than
                        Criteria.where("gid").gt(offset),
                        // lte = less than and equal to
                        Criteria.where("gid").lte(offset + limit)));

        List<Game> result = template.find(query, Document.class, "games")
                // convert to java steam
                .stream()
                // map each document to game object (convert document to game obj)
                .map(d -> Game.createFromMongo(d))
                // collect game obj into a list
                .toList();

        // Query query = new Query();
        // Pageable pageable = PageRequest.of(offset, limit);
        // query.with(pageable);
        // return template.find(query, Document.class, "games").stream().map(d ->
        // Game.createFromMongo(d)).toList();

        return result;
    }

    public List<Game> getGamesByRank(Integer offset, Integer limit) {

        Query query = new Query();
        Pageable pageable = PageRequest.of(offset, limit);
        query.with(pageable);
        Sort sort = Sort.by(Sort.Direction.ASC, "ranking");
        query.with(sort);

        List<Game> result = template.find(query, Document.class, "games")
                // convert to java steam
                .stream()
                // map each document to game object (convert document to game obj)
                .map(d -> Game.createFromMongo(d))
                // collect game obj into a list
                .toList();

        return result;
    }

    public Game getGameById(String game_id) {
        Query query = new Query();
        Criteria criteria =  Criteria.where("_id").is(game_id);
        query.addCriteria(criteria);

        //find return a list
        //findOne return just 1
        
        return  template.findOne(query, Game.class,"games");

        // When you use Document.class as the second argument, 
        //Spring Data MongoDB will return a Document object that 
        //contains the raw BSON document retrieved from MongoDB without 
        //deserializing it into a Java object

        //can convert Document to Game class
        //Document document = mongoTemplate.findOne(query, Document.class, "games");
        //Game game = mongoTemplate.getConverter().read(Game.class, document);

    }



}
