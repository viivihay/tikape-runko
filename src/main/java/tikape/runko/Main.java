package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.OpiskelijaDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:etusivu.db");
        database.init();

        OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);
        System.out.println("jippijaijeijei");

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "Täällä pääset keskustelemaan kuumimmista ja ajankohtaisimmista aiheista yhdessä muiden opiskelijoiden kanssa.");
            

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        //databaseen lisäys work in progress, ei vielä lisää mitään.
        post("/etusivu", (req, res) -> {
            res.redirect("/etusivu");
            HashMap map = new HashMap<>();
            if (req.queryParams().contains("aihe")) {
                 String content = req.queryParams("aihe");
                 database.addToDatabase(content);
            }
            return "";
        });

        get("/etusivu", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("etusivu", opiskelijaDao.findAll());

            return new ModelAndView(map, "etusivu");
        }, new ThymeleafTemplateEngine());

        get("/etusivu/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
    }
}
