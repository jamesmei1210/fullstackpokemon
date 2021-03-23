import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class UserGUI extends Application {

    MySQLConnection connection;
    {
        connection = new MySQLConnection();
        connection.establish_connection();
    }

    HashMap<String, Scene> sceneMap = new HashMap<>();

    //Menu Variables
    Button button_pokedex = new Button("Pokedex");
    Button button_randomizer = new Button("Randomizer");
    Button button_backtomenu  = new Button("Back to Menu");
    Button button_backtomenu2 = new Button("Back to Menu");
    Button button_cancel = new Button("Cancel");
    EventHandler<ActionEvent> goto_pokedex;
    EventHandler<ActionEvent> goto_randomizer;
    EventHandler<ActionEvent> backto_menu;
    EventHandler<ActionEvent> cancel;

    ListView<String> display_mons = new ListView();
    ArrayList<String> filters = new ArrayList<>();

    //Pokedex Variables
    String searchResult = "";
    ArrayList<Pokemon> list_pokemon = new ArrayList<>();
    String selected_pokemon;
    ImageView show_pokemon;
    Image pkmn_image;
    BufferedImage buffered_pkmn_image;
    Button button_confirm = new Button("Confirm");
    Button button_goto_addPokemon = new Button("Add Pokemon");
    EventHandler<ActionEvent> confirm;
    EventHandler<ActionEvent> addPokemon;
    ArrayList<String> pokemon_names = new ArrayList<>();

    //Randomizer Variables
    BufferedImage buffered_pkmn_image1; Image pkmn_image1; ImageView show_pokemon1;
    BufferedImage buffered_pkmn_image2; Image pkmn_image2; ImageView show_pokemon2;
    BufferedImage buffered_pkmn_image3; Image pkmn_image3; ImageView show_pokemon3;
    BufferedImage buffered_pkmn_image4; Image pkmn_image4; ImageView show_pokemon4;
    BufferedImage buffered_pkmn_image5; Image pkmn_image5; ImageView show_pokemon5;
    BufferedImage buffered_pkmn_image6; Image pkmn_image6; ImageView show_pokemon6;
    ArrayList<Pokemon> randomized_pokemon = new ArrayList<>();

    Text result_pokedex_number = new Text("");
    Text result_name = new Text("");
    Text result_abilities = new Text("");

    Text result_type1 = new Text("");
    Text result_type2 = new Text("");

    Text result_hp = new Text("");
    Text result_atk = new Text("");
    Text result_def = new Text("");
    Text result_spa = new Text("");
    Text result_spd = new Text("");
    Text result_spe = new Text("");


    //Verify Variables
    TextField user_field;
    TextField pass_field;


    //Add Variables
    Button button_add_pokemon = new Button("Add New Entry");
    EventHandler<ActionEvent> add_query;

    TextField name_field = new TextField("Name");
    TextField classification_field = new TextField("Classification");
    TextField type1_field = new TextField("Type 1");
    TextField type2_field = new TextField("Type 2");

    TextField ability1_field = new TextField("Ability 1");
    TextField ability2_field = new TextField("Ability 2");

    TextField health_field = new TextField("Health");
    TextField attack_field = new TextField("Attack");
    TextField defense_field = new TextField("Defense");
    TextField sp_attack_field = new TextField("Special Attack");
    TextField sp_defense_field = new TextField("Special Defense");
    TextField speed_field = new TextField("Speed");

    Button generate_randoms = new Button("Generate Randomizer");
    EventHandler<ActionEvent> generate_query;


    @Override
    public void start(Stage primaryStage) throws Exception {

        sceneMap.put("init",  initialGui());
        sceneMap.put("pokedex",  pokedex());
        sceneMap.put("randomizer",  randomizer());
        sceneMap.put("verify_admin",  verify_admin());
        sceneMap.put("add_pokemon",  add_pokemon());


        /* Global Event Handlers */
        goto_pokedex = event -> {
            primaryStage.setScene(sceneMap.get("pokedex"));
            primaryStage.show();
        };
        button_pokedex.setOnAction(goto_pokedex);

        goto_randomizer = event -> {
            primaryStage.setScene(sceneMap.get("randomizer"));
            primaryStage.show();
        };
        button_randomizer.setOnAction(goto_randomizer);

        backto_menu = event -> {
            primaryStage.setScene(sceneMap.get("init"));
            primaryStage.show();
        };
        button_backtomenu.setOnAction(backto_menu);
        button_backtomenu2.setOnAction(backto_menu);

        cancel = event -> {
            user_field.clear();
            pass_field.clear();
            primaryStage.setScene(sceneMap.get("pokedex"));
            primaryStage.show();
        };
        button_cancel.setOnAction(cancel);

        //login credential check
        confirm = event -> {
            if(user_field.getText().equals(connection.databaseUserName) && pass_field.getText().equals(connection.databasePassword)){
                user_field.clear();
                pass_field.clear();
                System.out.println("Credentials Verified");
                primaryStage.setScene(sceneMap.get("add_pokemon"));
                primaryStage.show();
            }
            else{
                System.out.println("Credentials Denied");
                user_field.clear();
                pass_field.clear();
            }
        };
        button_confirm.setOnAction(confirm);

        addPokemon = event -> {
            primaryStage.setScene(sceneMap.get("verify_admin"));
            primaryStage.show();

        };
        button_goto_addPokemon.setOnAction(addPokemon);

        add_query = event -> {
            connection.addToPokedex(name_field.getText(), classification_field.getText(), type1_field.getText(), type2_field.getText(),
                    ability1_field.getText(), ability2_field.getText(),
                    health_field.getText(), attack_field.getText(), defense_field.getText(),
                    sp_attack_field.getText(), sp_defense_field.getText(), speed_field.getText());
            display_mons.getItems().clear();

            list_pokemon = connection.return_pokedex(searchResult, filters);

            pokemon_names.clear();
            for(int i = 0; i < list_pokemon.size(); i++){
                pokemon_names.add(list_pokemon.get(i).name);
            }
            display_mons.getItems().addAll(pokemon_names);

            primaryStage.setScene(sceneMap.get("pokedex"));
        };
        button_add_pokemon.setOnAction(add_query);

        /* End Event Handlers */

        primaryStage.setScene(sceneMap.get("init"));
        primaryStage.show();
    }

    public Scene initialGui(){
        button_pokedex = new Button("Pokedex");
        button_randomizer = new Button("Randomizer");
        Button button_quit = new Button("Quit");
        VBox init_box = new VBox(20, button_pokedex, button_randomizer, button_quit);
        init_box.setAlignment(Pos.CENTER);
        Scene init = new Scene(init_box, 250, 300);

        EventHandler<ActionEvent> quit = event -> System.exit(1);
        button_quit.setOnAction(quit);

        return init;
    }

    public Scene pokedex() throws IOException {
        Text searchField_text = new Text("Search: ");
        TextField searchField_field = new TextField();
        selected_pokemon = "";

        display_mons.getItems().clear();

        list_pokemon = connection.return_pokedex(searchResult, filters);
        buffered_pkmn_image = ImageIO.read(new URL("https://raw.githubusercontent.com/msikma/pokesprite/master/items/ball/poke.png"));
        pkmn_image = SwingFXUtils.toFXImage(buffered_pkmn_image, null);
        show_pokemon = new ImageView(pkmn_image);
        show_pokemon.fitHeightProperty().setValue(250);
        show_pokemon.fitWidthProperty().setValue(250);

        for(int i = 0; i < list_pokemon.size(); i++){
            pokemon_names.add(list_pokemon.get(i).name);
        }
        display_mons.getItems().addAll(pokemon_names);

        searchField_field.textProperty().addListener((observable, oldValue, newValue) -> {
            display_mons.getItems().clear();

            searchResult = newValue;
            list_pokemon = connection.return_pokedex(searchResult, filters);

            pokemon_names.clear();
            for(int i = 0; i < list_pokemon.size(); i++){
                pokemon_names.add(list_pokemon.get(i).name);
            }
            display_mons.getItems().addAll(pokemon_names);
        });

        display_mons.setOnMouseClicked((MouseEvent e)-> {
            selected_pokemon = display_mons.getSelectionModel().getSelectedItem();

            try {
                buffered_pkmn_image = ImageIO.read(new URL("https://raw.githubusercontent.com/msikma/pokesprite/master/pokemon-gen8/regular/"+ selected_pokemon.toLowerCase() +".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pkmn_image = SwingFXUtils.toFXImage(buffered_pkmn_image, null);
            show_pokemon.setImage(pkmn_image);

        });

        HBox searchField = new HBox(10, searchField_text,searchField_field);
        VBox left_panel = new VBox(10, searchField, button_goto_addPokemon, button_backtomenu);
        VBox right_panel = new VBox(10, display_mons, show_pokemon);

        HBox scene = new HBox(left_panel, right_panel);

        return new Scene(scene, 500, 500);
    }

    //selects 6 random pokemon
    public Scene randomizer() throws IOException {
        buffered_pkmn_image = ImageIO.read(new URL("https://raw.githubusercontent.com/msikma/pokesprite/master/items/ball/poke.png"));
        pkmn_image = SwingFXUtils.toFXImage(buffered_pkmn_image, null);

        show_pokemon1 = new ImageView(pkmn_image);show_pokemon1.fitHeightProperty().setValue(100);show_pokemon1.fitWidthProperty().setValue(100);
        show_pokemon2 = new ImageView(pkmn_image);show_pokemon2.fitHeightProperty().setValue(100);show_pokemon2.fitWidthProperty().setValue(100);
        show_pokemon3 = new ImageView(pkmn_image);show_pokemon3.fitHeightProperty().setValue(100);show_pokemon3.fitWidthProperty().setValue(100);
        show_pokemon4 = new ImageView(pkmn_image);show_pokemon4.fitHeightProperty().setValue(100);show_pokemon4.fitWidthProperty().setValue(100);
        show_pokemon5 = new ImageView(pkmn_image);show_pokemon5.fitHeightProperty().setValue(100);show_pokemon5.fitWidthProperty().setValue(100);
        show_pokemon6 = new ImageView(pkmn_image);show_pokemon6.fitHeightProperty().setValue(100);show_pokemon6.fitWidthProperty().setValue(100);

        show_pokemon1.setOnMouseClicked((MouseEvent e)->{
            result_pokedex_number.setText(Integer.toString(randomized_pokemon.get(0).num));
            result_name.setText(randomized_pokemon.get(0).name);
            result_abilities.setText(randomized_pokemon.get(0).ability);

            result_type1.setText(randomized_pokemon.get(0).type1);
            result_type2.setText(randomized_pokemon.get(0).type2);

            result_hp.setText(Integer.toString(randomized_pokemon.get(0).hp));
            result_atk.setText(Integer.toString(randomized_pokemon.get(0).atk));
            result_def.setText(Integer.toString(randomized_pokemon.get(0).def));
            result_spa.setText(Integer.toString(randomized_pokemon.get(0).spa));
            result_spd.setText(Integer.toString(randomized_pokemon.get(0).spd));
            result_spe.setText(Integer.toString(randomized_pokemon.get(0).spe));
        });
        show_pokemon2.setOnMouseClicked((MouseEvent e)->{
            result_pokedex_number.setText(Integer.toString(randomized_pokemon.get(1).num));
            result_name.setText(randomized_pokemon.get(1).name);
            result_abilities.setText(randomized_pokemon.get(1).ability);

            result_type1.setText(randomized_pokemon.get(1).type1);
            result_type2.setText(randomized_pokemon.get(1).type2);

            result_hp.setText(Integer.toString(randomized_pokemon.get(1).hp));
            result_atk.setText(Integer.toString(randomized_pokemon.get(1).atk));
            result_def.setText(Integer.toString(randomized_pokemon.get(1).def));
            result_spa.setText(Integer.toString(randomized_pokemon.get(1).spa));
            result_spd.setText(Integer.toString(randomized_pokemon.get(1).spd));
            result_spe.setText(Integer.toString(randomized_pokemon.get(1).spe));
        });
        show_pokemon3.setOnMouseClicked((MouseEvent e)->{
            result_pokedex_number.setText(Integer.toString(randomized_pokemon.get(2).num));
            result_name.setText(randomized_pokemon.get(2).name);
            result_abilities.setText(randomized_pokemon.get(2).ability);

            result_type1.setText(randomized_pokemon.get(2).type1);
            result_type2.setText(randomized_pokemon.get(2).type2);

            result_hp.setText(Integer.toString(randomized_pokemon.get(2).hp));
            result_atk.setText(Integer.toString(randomized_pokemon.get(2).atk));
            result_def.setText(Integer.toString(randomized_pokemon.get(2).def));
            result_spa.setText(Integer.toString(randomized_pokemon.get(2).spa));
            result_spd.setText(Integer.toString(randomized_pokemon.get(2).spd));
            result_spe.setText(Integer.toString(randomized_pokemon.get(2).spe));
        });
        show_pokemon4.setOnMouseClicked((MouseEvent e)->{
            result_pokedex_number.setText(Integer.toString(randomized_pokemon.get(3).num));
            result_name.setText(randomized_pokemon.get(3).name);
            result_abilities.setText(randomized_pokemon.get(3).ability);

            result_type1.setText(randomized_pokemon.get(3).type1);
            result_type2.setText(randomized_pokemon.get(3).type2);

            result_hp.setText(Integer.toString(randomized_pokemon.get(3).hp));
            result_atk.setText(Integer.toString(randomized_pokemon.get(3).atk));
            result_def.setText(Integer.toString(randomized_pokemon.get(3).def));
            result_spa.setText(Integer.toString(randomized_pokemon.get(3).spa));
            result_spd.setText(Integer.toString(randomized_pokemon.get(3).spd));
            result_spe.setText(Integer.toString(randomized_pokemon.get(3).spe));
        });
        show_pokemon5.setOnMouseClicked((MouseEvent e)->{
            result_pokedex_number.setText(Integer.toString(randomized_pokemon.get(4).num));
            result_name.setText(randomized_pokemon.get(4).name);
            result_abilities.setText(randomized_pokemon.get(4).ability);

            result_type1.setText(randomized_pokemon.get(4).type1);
            result_type2.setText(randomized_pokemon.get(4).type2);

            result_hp.setText(Integer.toString(randomized_pokemon.get(4).hp));
            result_atk.setText(Integer.toString(randomized_pokemon.get(4).atk));
            result_def.setText(Integer.toString(randomized_pokemon.get(4).def));
            result_spa.setText(Integer.toString(randomized_pokemon.get(4).spa));
            result_spd.setText(Integer.toString(randomized_pokemon.get(4).spd));
            result_spe.setText(Integer.toString(randomized_pokemon.get(4).spe));
        });
        show_pokemon6.setOnMouseClicked((MouseEvent e)->{
            result_pokedex_number.setText(Integer.toString(randomized_pokemon.get(5).num));
            result_name.setText(randomized_pokemon.get(5).name);
            result_abilities.setText(randomized_pokemon.get(5).ability);

            result_type1.setText(randomized_pokemon.get(5).type1);
            result_type2.setText(randomized_pokemon.get(5).type2);

            result_hp.setText(Integer.toString(randomized_pokemon.get(5).hp));
            result_atk.setText(Integer.toString(randomized_pokemon.get(5).atk));
            result_def.setText(Integer.toString(randomized_pokemon.get(5).def));
            result_spa.setText(Integer.toString(randomized_pokemon.get(5).spa));
            result_spd.setText(Integer.toString(randomized_pokemon.get(5).spd));
            result_spe.setText(Integer.toString(randomized_pokemon.get(5).spe));
        });

        //gets images for the pokemon
        generate_query = event -> {
            randomized_pokemon = connection.generate_random_pokemon();
            try {
                buffered_pkmn_image1 = ImageIO.read(new URL("https://raw.githubusercontent.com/msikma/pokesprite/master/pokemon-gen8/regular/"+ randomized_pokemon.get(0).name.toLowerCase() +".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pkmn_image1 = SwingFXUtils.toFXImage(buffered_pkmn_image1, null);
            show_pokemon1.setImage(pkmn_image1);

            try {
                buffered_pkmn_image2 = ImageIO.read(new URL("https://raw.githubusercontent.com/msikma/pokesprite/master/pokemon-gen8/regular/"+ randomized_pokemon.get(1).name.toLowerCase() +".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pkmn_image2 = SwingFXUtils.toFXImage(buffered_pkmn_image2, null);
            show_pokemon2.setImage(pkmn_image2);

            try {
                buffered_pkmn_image3 = ImageIO.read(new URL("https://raw.githubusercontent.com/msikma/pokesprite/master/pokemon-gen8/regular/"+ randomized_pokemon.get(2).name.toLowerCase() +".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pkmn_image3 = SwingFXUtils.toFXImage(buffered_pkmn_image3, null);
            show_pokemon3.setImage(pkmn_image3);

            try {
                buffered_pkmn_image4 = ImageIO.read(new URL("https://raw.githubusercontent.com/msikma/pokesprite/master/pokemon-gen8/regular/"+ randomized_pokemon.get(3).name.toLowerCase() +".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pkmn_image4 = SwingFXUtils.toFXImage(buffered_pkmn_image4, null);
            show_pokemon4.setImage(pkmn_image4);

            try {
                buffered_pkmn_image5 = ImageIO.read(new URL("https://raw.githubusercontent.com/msikma/pokesprite/master/pokemon-gen8/regular/"+ randomized_pokemon.get(4).name.toLowerCase() +".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pkmn_image5 = SwingFXUtils.toFXImage(buffered_pkmn_image5, null);
            show_pokemon5.setImage(pkmn_image5);

            try {
                buffered_pkmn_image6 = ImageIO.read(new URL("https://raw.githubusercontent.com/msikma/pokesprite/master/pokemon-gen8/regular/"+ randomized_pokemon.get(5).name.toLowerCase() +".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            pkmn_image6 = SwingFXUtils.toFXImage(buffered_pkmn_image6, null);
            show_pokemon6.setImage(pkmn_image6);

            result_pokedex_number.setText("");
            result_name.setText("");
            result_abilities.setText("");
            result_type1.setText("");
            result_type2.setText("");
            result_hp.setText("");
            result_atk.setText("");
            result_def.setText("");
            result_spa.setText("");
            result_spd.setText("");
            result_spe.setText("");
        };
        generate_randoms.setOnAction(generate_query);

        HBox top_row = new HBox(show_pokemon1, show_pokemon2, show_pokemon3);
        HBox bot_row = new HBox(show_pokemon4, show_pokemon5, show_pokemon6);

        VBox left_partition = new VBox(10, generate_randoms, button_backtomenu2);
        VBox right_partition = new VBox(top_row, bot_row);

        HBox top_partition = new HBox(10, left_partition, right_partition);
        left_partition.setAlignment(Pos.CENTER);
        top_partition.setAlignment(Pos.CENTER);

        Text num = new Text("Pokedex Number: ");
        Text name = new Text("Pokemon Name: ");
        Text abilities = new Text("Pokemon Abilities: ");
        Text types = new Text("Types: ");
        Text stats = new Text("Base Stats:");

        Text hp = new Text("HP: ");
        Text atk = new Text("ATK: ");
        Text def = new Text("DEF: ");
        Text spa = new Text("SPA: ");
        Text spd = new Text("SPD: ");
        Text spe = new Text("SPE: ");

        HBox number_box = new HBox(num, result_pokedex_number);
        HBox name_box = new HBox(name, result_name);
        HBox types_box = new HBox(types, result_type1, result_type2);
        HBox abilities_box = new HBox(abilities, result_abilities);
        VBox info_left = new VBox(number_box, name_box, types_box, abilities_box);

        HBox stat_row_one   = new HBox(hp, result_hp);
        HBox stat_row_two   = new HBox(atk, result_atk);
        HBox stat_row_three = new HBox(def, result_def);
        HBox stat_row_four  = new HBox(spa, result_spa);
        HBox stat_row_five  = new HBox(spd, result_spd);
        HBox stat_row_six   = new HBox(spe, result_spe);

        VBox info_right = new VBox(stats, stat_row_one, stat_row_two, stat_row_three, stat_row_four, stat_row_five, stat_row_six);

        HBox info = new HBox(100,info_left, info_right);
        info.setAlignment(Pos.TOP_CENTER);
        VBox scene = new VBox(top_partition, info);

        return new Scene(scene, 600,350);
    }

    public Scene verify_admin(){

        Text user = new Text("Username: ");
        Text pass = new Text("Password: ");

        user_field = new TextField();
        pass_field = new TextField();

        HBox user_box = new HBox(user, user_field);
        HBox pass_box = new HBox(pass, pass_field);

        VBox scene = new VBox(2, user_box, pass_box, button_confirm, button_cancel);
        return new Scene(scene, 250,135);
    }

    public Scene add_pokemon(){

        Text description = new Text("Pokemon Description");
        name_field.setOnMouseClicked(e->name_field.clear());
        classification_field.setOnMouseClicked(e->classification_field.clear());
        type1_field.setOnMouseClicked(event -> type1_field.clear());
        type2_field.setOnMouseClicked(event -> type2_field.clear());

        Text abilities_text = new Text("Abilities");
        ability1_field.setOnMouseClicked(e->ability1_field.clear());
        ability2_field.setOnMouseClicked(e->ability2_field.clear());

        Text stats_text = new Text("Stats");
        health_field.setOnMouseClicked(event -> health_field.clear());
        attack_field.setOnMouseClicked(event -> attack_field.clear());
        defense_field.setOnMouseClicked(event -> defense_field.clear());
        sp_attack_field.setOnMouseClicked(event -> sp_attack_field.clear());
        sp_defense_field.setOnMouseClicked(event -> sp_defense_field.clear());
        speed_field.setOnMouseClicked(event -> speed_field.clear());

        VBox part1 = new VBox(description, name_field,classification_field, type1_field, type2_field);
        VBox part2 = new VBox(abilities_text, ability1_field, ability2_field);
        VBox part3 = new VBox(stats_text, health_field, attack_field, defense_field, sp_attack_field, sp_defense_field, speed_field);
        VBox scene = new VBox(10, part1, part2, part3, button_add_pokemon);

        return new Scene(scene, 250, 500);
    }
}
