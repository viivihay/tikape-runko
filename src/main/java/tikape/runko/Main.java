package tikape.runko;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.AihealueDao;
import tikape.runko.database.KeskusteluDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:etusivu.db");
        database.init();
        AihealueDao aihealueDao = new AihealueDao(database);
        KeskusteluDao keskusteluDao = new KeskusteluDao(database);
        
        

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "Täällä pääset keskustelemaan kuumimmista ja ajankohtaisimmista aiheista yhdessä muiden opiskelijoiden kanssa.");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/etusivu", (req, res) -> {
            res.redirect("/etusivu");
            Connection connection = database.getConnection();
            
            Statement statement = connection.createStatement();
            HashMap map = new HashMap<>();
            if (req.queryParams().contains("aihe")) {
                String content = req.queryParams("aihe");
                database.addToDatabase(content);
                
                statement.executeUpdate("ALTER TABLE Keskustelu ADD "+content+" varchar(255)");
                
            }
            return "";
        });

        get("/etusivu", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("etusivu", aihealueDao.findAll());

            return new ModelAndView(map, "etusivu");
        }, new ThymeleafTemplateEngine());

        get("/etusivu/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("aihealue", aihealueDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("keskustelu", keskusteluDao.findAll(Integer.parseInt(req.params("id"))));
            

            return new ModelAndView(map, "aihealue");
        }, new ThymeleafTemplateEngine());

        post("/etusivu/:id", (req, res) -> {
            String id = (req.params(":id"));
            res.redirect(id);
            HashMap map = new HashMap<>();
            
            if (req.queryParams().contains("viesti")) {
                
                String content = req.queryParams("viesti");
                database.addToKeskustelu(content, Integer.parseInt(id));
            }
            return "";
        });
    }
}
