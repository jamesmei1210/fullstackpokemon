import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class MySQLConnection {

    String MySQLURL = "jdbc:mysql://localhost:3306/mydb?allowPublicKeyRetrieval=true&useSSL=false";
    String databaseUserName = "";
    String databasePassword = "";

    Connection con = null;
    String query = "";
    ResultSet resultSet = null;

    //establishes the accepted username and password by reading from db_user.txt
    public void establish_connection(){
        File file = new File(System.getProperty("user.dir") + "\\db_user.txt");
        System.out.println(System.getProperty("user.dir") + "\\db_user.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        databaseUserName = scanner.next();
        databasePassword = scanner.next();
    } //end establish_connection

    //queries the database for the searching functionality
    public ArrayList<Pokemon> return_pokedex(String search, ArrayList<String> filters){

        ArrayList<Pokemon> pokemon = new ArrayList<>();

        // if filters are empty -- normal query with dynamic search
        if(filters.size() == 0){
            try {
            // open connection
            con = DriverManager.getConnection(MySQLURL, databaseUserName, databasePassword);
            if (con != null) {
                Statement stmt = con.createStatement();
                if(search.length() == 0){
                    query = "SELECT * " +
                            "FROM mydb.pokemon";
                }
                else{
                    query = "SELECT *" +
                            "FROM mydb.pokemon " +
                            "WHERE name LIKE '%" + search + "%';";
                }
                resultSet = stmt.executeQuery(query);

                // loop through the result set
                while (resultSet.next()) {
//                    System.out.println(resultSet.getString("pokedex_number") + "\t" +
//                            resultSet.getString("name"));
                    pokemon.add(new Pokemon(Integer.parseInt(resultSet.getString("pokedex_number")),
                                            resultSet.getString("name"), "",
                                        0,0,0,0,0,0, "normal", "normal"));
                }
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // if filters is not empty -- apply filters to query

        return pokemon;
    } //end return_pokedex

    //query used to add a pokemon to the database
    public void addToPokedex(String name, String classification, String type1, String type2,
                                       String ability1, String ability2,
                                       String hp, String atk, String def, String spa, String spd, String spe){

        try {
            // open connection
            con = DriverManager.getConnection(MySQLURL, databaseUserName, databasePassword);
            if (con != null) {
                Statement stmt = con.createStatement();
                query = "INSERT INTO pokemon VALUES(\"" +
                        ability1 + " " + ability2 + "\", " +
                        Integer.parseInt(atk) + "," + " 5120, 70, " +
                        (Integer.parseInt(hp)+Integer.parseInt(atk)+Integer.parseInt(def)+ Integer.parseInt(spa)+Integer.parseInt(spd)+Integer.parseInt(spe)) +
                        ", 225, \"" + classification + "\", " + Integer.parseInt(def) + ", 1059860, 0.0, " + Integer.parseInt(hp) + ", \"None\", \"" + name + "\", 50, " + Integer.parseInt(spa) + ", " + Integer.parseInt(spd) + ", " + Integer.parseInt(spe) + ", \"" +
                        type1 + "\", \"" + type2 + "\", 0," + "(Select MAX(pokedex_number)+1 FROM (SELECT * FROM pokemon) as temp), 0, 0);";

                stmt.executeUpdate(query);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    } //end addToPokedex

    //query used to generate a team of 6 random pokemon
    public ArrayList<Pokemon> generate_random_pokemon(){

        ArrayList<Pokemon> pokemon = new ArrayList<>();

        try {
            // open connection
            con = DriverManager.getConnection(MySQLURL, databaseUserName, databasePassword);
            if (con != null) {
                Statement stmt = con.createStatement();
                query = "SELECT * " +
                        "FROM pokemon " +
                        "ORDER BY RAND() " +
                        "LIMIT 6";

                resultSet = stmt.executeQuery(query);

                while (resultSet.next()) {
                    pokemon.add(new Pokemon(Integer.parseInt(resultSet.getString("pokedex_number")),
                            resultSet.getString("name"),
                            resultSet.getString("abilities"),
                            Integer.parseInt(resultSet.getString("hp")),Integer.parseInt(resultSet.getString("attack")),Integer.parseInt(resultSet.getString("defense")),
                            Integer.parseInt(resultSet.getString("sp_attack")),Integer.parseInt(resultSet.getString("sp_defense")),Integer.parseInt(resultSet.getString("speed")),
                            resultSet.getString("type1"),resultSet.getString("type2")));
                }

                return pokemon;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return pokemon;
    } //end generate_random_pokemon
}