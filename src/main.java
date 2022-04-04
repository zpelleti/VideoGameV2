import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.time.temporal.ChronoUnit;

import javax.swing.JOptionPane;

import com.mysql.cj.xdevapi.PreparableStatement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class main extends Application {
	public static class ex {
		public static int days = 0;
	}
	public void start(Stage stage) {
		// setting objects
		Image img = new Image("file:images/java-bg2.png"); // background
		ImageView iv = new ImageView(img);
		Label username = new Label("USERNAME");
		Label password = new Label("PASSWORD");
		TextField inputUsername = new TextField();
		PasswordField inputPassword = new PasswordField();
		Button btnLogin = new Button("Login");

		// GUI design
		username.setTranslateX(240);
		username.setTranslateY(280);
		username.setTextFill(Color.web("#FFD966"));
		username.setStyle(
				"-fx-text-fill: '#FFD966';-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-family: Impact;");
		password.setTranslateX(240);
		password.setTranslateY(340);
		password.setStyle(
				"-fx-text-fill: '#FFD966';-fx-font-size: 26px; -fx-font-weight: bold; -fx-font-family: Impact;");
		inputUsername.setTranslateX(400);
		inputUsername.setTranslateY(280);
		inputUsername.setPrefWidth(300);
		inputUsername.setStyle(
				"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
		inputPassword.setTranslateX(400);
		inputPassword.setTranslateY(340);
		inputPassword.setPrefWidth(300);
		inputPassword.setStyle(
				"-fx-text-fill: #1C4587; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
		btnLogin.setTranslateX(365);
		btnLogin.setTranslateY(400);
		btnLogin.setPrefWidth(230);
		btnLogin.setStyle(
				"-fx-text-fill: #1C4587; -fx-background-color: #FFD966; -fx-font-size: 22px; -fx-font-weight: bold; ;-fx-font-family: Impact");

		// button action
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				/**
				 * create the database ==================================================
				 */
				create();

				/**
				 * accept + save user inputs:
				 */
				String username = inputUsername.getText();
				String password = inputPassword.getText();

				if (username.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter UserName");
				} else if (password.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter Password");
				} else {
					// if user fields exist:
					System.out.println("Logging in...");
					// Connection to Database:
					Connection connection = connect();

					try {
						Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
								ResultSet.CONCUR_READ_ONLY);
						statement.executeUpdate("USE GAMES_LIBRARY");
						// retrieve user name and password:
						String str = ("Select * from USERS where USERNAME = '" + username + "' and PASSWORD = '"
								+ password + "'");
						// Query execution:
						ResultSet rs = statement.executeQuery(str);
						if (rs.next() == false) {
							System.out.println("User does not exist");
							JOptionPane.showMessageDialog(null, "Wrong UserName-Password combination");
						} else {
							rs.beforeFirst();
							while (rs.next()) {
								String Admin = rs.getString("ADMIN");
								int user = rs.getInt("USERID");

								if (Admin.equals("1")) {
									adminMenu();
									stage.close();
								} else {
									userMenu(user);
									stage.close();
								}
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};

		btnLogin.setOnAction(event);

		/**
		 * scene setting
		 */
		Group root = new Group();
		root.getChildren().addAll(iv, username, password, inputUsername, inputPassword, btnLogin);
		Scene scene = new Scene(root, 960, 540);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Video Game Library Logic"); // title
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void adminMenu() {
		/**
		 * setting objects
		 */
		Image adminbg = new Image("file:images/java-admin.png"); // background
		ImageView imageview = new ImageView(adminbg);
		Image userWelcome = new Image("file:images/java-user-welcome.png"); // welcome image
		ImageView imageview2 = new ImageView(userWelcome);
		MenuBar menuBar = new MenuBar();
		Menu gameMenu = new Menu("Manage Game");
		Menu userMenu = new Menu("Manage User");
		Menu issuedMenu = new Menu("Manage Issued Game");
		MenuItem gameMenu1 = new MenuItem("View All Games");
		MenuItem gameMenu2 = new MenuItem("Add New Games");
		MenuItem gameMenu3 = new MenuItem("Delete Games");
		MenuItem gameMenu4 = new MenuItem("Return Games");
		MenuItem userMenu1 = new MenuItem("View All Users");
		MenuItem userMenu2 = new MenuItem("Add New Users");
		MenuItem userMenu3 = new MenuItem("Delete Users");
		MenuItem issuedMenu1 = new MenuItem("View Issued Game");
		MenuItem issuedMenu2 = new MenuItem("Issued Game");
		Group group = new Group();
		Scene adminScene;
		Stage adminStage;

		/**
		 * GUI design
		 */
		imageview2.setTranslateX(50);
		imageview2.setTranslateY(220);
		imageview.setTranslateY(25);
		menuBar.getMenus().addAll(gameMenu, userMenu, issuedMenu);
		gameMenu.getItems().addAll(gameMenu1, gameMenu2, gameMenu3, gameMenu4);
		userMenu.getItems().addAll(userMenu1, userMenu2, userMenu3);
		issuedMenu.getItems().addAll(issuedMenu1, issuedMenu2);
		menuBar.setPrefSize(960, 20);

		/**
		 * View All Games
		 */
		gameMenu1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				TableView table = new TableView();
				TableColumn tc1 = new TableColumn();
				TableColumn tc2 = new TableColumn();
				TableColumn tc3 = new TableColumn();
				TableColumn tc4 = new TableColumn();
				TableColumn tc5 = new TableColumn();

				/**
				 * table column add
				 */
				table.setPrefSize(860, 360);
				table.setTranslateX(50);
				table.setTranslateY(165);
				tc1.setText("GameID");
				tc1.setMinWidth(80);
				tc1.setStyle("-fx-alignment: CENTER;");
				tc1.setCellValueFactory(new PropertyValueFactory<game, String>("gameid"));
				tc2.setText("GameName");
				tc2.setMinWidth(305);
				tc2.setStyle("-fx-alignment: CENTER;");
				tc2.setCellValueFactory(new PropertyValueFactory<game, String>("gamename"));
				tc3.setText("Platform");
				tc3.setMinWidth(120);
				tc3.setStyle("-fx-alignment: CENTER;");
				tc3.setCellValueFactory(new PropertyValueFactory<game, String>("platform"));
				tc4.setText("Price");
				tc4.setMinWidth(100);
				tc4.setStyle("-fx-alignment: CENTER;");
				tc4.setCellValueFactory(new PropertyValueFactory<game, String>("price"));
				tc5.setText("IsAvailable (1-Available, 0-Rent Out)");
				tc5.setMinWidth(240);
				tc5.setStyle("-fx-alignment: CENTER;");
				tc5.setCellValueFactory(new PropertyValueFactory<game, String>("available"));
				table.getColumns().addAll(tc1, tc2, tc3, tc4, tc5);

				try {
					ObservableList<game> gameList = FXCollections.observableArrayList();
					Connection connection = connect();
					/**
					 * select available game in the list from DB
					 */
					String sql = "SELECT * FROM games_library.games ORDER BY gameid;";
					ResultSet rs = connection.createStatement().executeQuery(sql);

					while (rs.next()) {
						game data = new game(rs.getInt("gameid"), rs.getString("gamename"), rs.getString("platform"),
								rs.getInt("price"), rs.getInt("available"));
						gameList.addAll(data);
					}
					table.setItems(gameList);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				group.getChildren().clear();
				group.getChildren().remove(imageview2);
				group.getChildren().addAll(imageview, menuBar, imageview2);
				group.getChildren().addAll(table);
			}
		});

		/**
		 * Add New Games
		 */
		gameMenu2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Label title = new Label("Please enter the details of the game: ");
				Label gamename = new Label("Game Name");
				Label platform = new Label("Platform");
				Label gameprice = new Label("Game Price");
				TextField inputGamename = new TextField();
				TextField inputPlatform = new TextField();
				TextField inputPrice = new TextField();
				Button btnAddGame = new Button("Add Game");

				/**
				 * GUI design
				 */
				title.setTranslateX(350);
				title.setTranslateY(190);
				title.setStyle("-fx-text-fill: white;-fx-font-size: 18px; -fx-font-family: Impact;");
				gamename.setTranslateX(240);
				gamename.setTranslateY(240);
				gamename.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				platform.setTranslateX(240);
				platform.setTranslateY(300);
				platform.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				gameprice.setTranslateX(240);
				gameprice.setTranslateY(360);
				gameprice.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				inputGamename.setTranslateX(400);
				inputGamename.setTranslateY(240);
				inputGamename.setPrefWidth(300);
				inputGamename.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				inputPlatform.setTranslateX(400);
				inputPlatform.setTranslateY(300);
				inputPlatform.setPrefWidth(300);
				inputPlatform.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				inputPrice.setTranslateX(400);
				inputPrice.setTranslateY(360);
				inputPrice.setPrefWidth(300);
				inputPrice.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				btnAddGame.setTranslateX(365);
				btnAddGame.setTranslateY(440);
				btnAddGame.setPrefWidth(230);
				btnAddGame.setStyle(
						"-fx-text-fill: #1C4587; -fx-background-color: #FFD966; -fx-font-size: 22px; -fx-font-weight: bold; ;-fx-font-family: Impact");

				/**
				 * btnAddGame Action
				 */
				btnAddGame.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String gName = inputGamename.getText();
						String platform = inputPlatform.getText();
						String price = inputPrice.getText();
						int price_int = Integer.parseInt(price);

						Connection connection = connect();

						/**
						 * insert the data into DB
						 */
						try {
							Statement statement = connection.createStatement();
							statement.executeUpdate("USE GAMES_LIBRARY");
							statement.executeUpdate("INSERT INTO GAMES(GAMENAME, PLATFORM, PRICE, AVAILABLE) "
									+ "VALUES ('" + gName + "', '" + platform + "', " + price_int + ",1)");
							/*
							 * clear after submitting
							 */
							inputGamename.clear();
							inputPlatform.clear();
							inputPrice.clear();
							JOptionPane.showMessageDialog(null, "Adding Game...");
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1);
						}
					}
				});

				group.getChildren().clear();
				group.getChildren().addAll(imageview, menuBar);
				group.getChildren().addAll(gamename, platform, gameprice, inputGamename, inputPlatform, inputPrice,
						btnAddGame, title);

			}
		});
		/**
		 * Delete Games
		 */
		gameMenu3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Label title = new Label("Please enter the game id for deleting: ");
				Label gameid = new Label("Game ID");
				TextField inputGameid = new TextField();
				Button btnDelGame = new Button("Delete Game");

				/**
				 * GUI design
				 */
				title.setTranslateX(350);
				title.setTranslateY(250);
				title.setStyle("-fx-text-fill: white;-fx-font-size: 18px; -fx-font-family: Impact;");
				gameid.setTranslateX(240);
				gameid.setTranslateY(290);
				gameid.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				inputGameid.setTranslateX(400);
				inputGameid.setTranslateY(290);
				inputGameid.setPrefWidth(300);
				inputGameid.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				btnDelGame.setTranslateX(365);
				btnDelGame.setTranslateY(340);
				btnDelGame.setPrefWidth(230);
				btnDelGame.setStyle(
						"-fx-text-fill: #1C4587; -fx-background-color: #FFD966; -fx-font-size: 22px; -fx-font-weight: bold; ;-fx-font-family: Impact");

				/**
				 * btnAddGame Action
				 */
				btnDelGame.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String gName = inputGameid.getText();

						/**
						 * insert the data into DB
						 */
						Connection connection = connect();
						try {
							String gid = inputGameid.getText();

							Statement statement = connection.createStatement();
							statement.executeUpdate("USE GAMES_LIBRARY");

							String id = null;
							ResultSet r = statement.executeQuery("SELECT gameid FROM games WHERE gameid=" + gid);
							while (r.next()) {
								id = r.getString(1);
							}

							statement.executeUpdate("DELETE FROM games WHERE gameid=" + gid);
							inputGameid.clear();

							JOptionPane.showMessageDialog(null, "Game Deleted Successfully");

						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1);
						}
					}
				});

				group.getChildren().clear();
				group.getChildren().addAll(imageview, menuBar);
				group.getChildren().addAll(gameid, inputGameid, btnDelGame, title);
			}
		});

		/**
		 * return game
		 */
		gameMenu4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Label title = new Label("Please enter the issue id for returning game: ");
				Label issuedid = new Label("Issue ID");
				Label returnDate = new Label("Return Date");
				TextField inputIssueId = new TextField();
				TextField inputReturnDate = new TextField();
				Button btnReturn = new Button("Return Game");
				
				/**
				 * GUI design
				 */
				title.setTranslateX(310);
				title.setTranslateY(200);
				title.setStyle("-fx-text-fill: white;-fx-font-size: 18px; -fx-font-family: Impact;");
				issuedid.setTranslateX(240);
				issuedid.setTranslateY(240);
				issuedid.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				returnDate.setTranslateX(240);
				returnDate.setTranslateY(300);
				returnDate.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				inputIssueId.setTranslateX(400);
				inputIssueId.setTranslateY(240);
				inputIssueId.setPrefWidth(300);
				inputIssueId.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				inputReturnDate.setTranslateX(400);
				inputReturnDate.setTranslateY(300);
				inputReturnDate.setPrefWidth(300);
				inputReturnDate.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				btnReturn.setTranslateX(365);
				btnReturn.setTranslateY(380);
				btnReturn.setPrefWidth(230);
				btnReturn.setStyle(
						"-fx-text-fill: #1C4587; -fx-background-color: #FFD966; -fx-font-size: 22px; -fx-font-weight: bold; ;-fx-font-family: Impact");

				/**
				 * btnReturn Action
				 */
				btnReturn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String iid = inputIssueId.getText();
						String rdate = inputReturnDate.getText();

						Connection connection = connect();

						try {
							Statement statement = connection.createStatement();
							statement.executeUpdate("USE GAMES_LIBRARY");

							String d1 = null;
							String d2 = rdate;
							
							// select issue date:
							ResultSet r = statement.executeQuery("SELECT issued_date FROM issued WHERE issueid=" + iid);
							while (r.next()) {
								d1 = r.getString(1);
							}
							
							LocalDate date1 = LocalDate.parse(d1);
							LocalDate date2 = LocalDate.parse(d2);
							System.out.println ("Date1: " + date1); 
							System.out.println ("Date2: " + date2); 
							// subtract dates + save difference:
							long difference = ChronoUnit.DAYS.between(date1, date2);
							// convert from milliseconds to days:
							ex.days = (int) (TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));
							ex.days = (int) ChronoUnit.DAYS.between(date2, date1);
							System.out.println ("Days: " + ex.days);

							// update return date:
							statement.executeUpdate(
									"UPDATE ISSUED SET RETURN_DATE = '" + rdate + "' WHERE ISSUEID=" + iid);
							statement.executeUpdate(
									"UPDATE games_library.games "
									+ "JOIN games_library.issued ON issued.gameid = games.gameid "
									+ "SET games.available = 1 "
									+ "WHERE issued.issueid = " + iid + " ;"
									);
							inputIssueId.clear();
							inputReturnDate.clear();

							Connection connection2 = connect();
							Statement statement2 = connection2.createStatement();
							statement2.executeUpdate("USE GAMES_LIBRARY");
							ResultSet r2 = statement2.executeQuery("SELECT period_days FROM issued WHERE issueid =" + iid);
							String diff = null;
							while (r2.next()) {
								diff = r2.getString(1);
							}
							int dif_int = Integer.parseInt(diff);
							System.out.println(dif_int);
							if (Math.abs(ex.days) > dif_int) {
								System.out.println(Math.abs(ex.days));		// displays number days over in console 
								// fine for each day after period = r2*10
								int fine = (Math.abs(ex.days) - dif_int) * 10;
								// update fine :
								statement2.executeUpdate("UPDATE issued SET fine=" + fine + " WHERE issueid=" + iid);
								String fineStr = ("Fine: $" + fine);
								JOptionPane.showMessageDialog(null, fineStr);
							}
							JOptionPane.showMessageDialog(null, "Game Returned Successfully");

						} 
						catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1);
						}
					}
				});
				
				group.getChildren().clear();
				group.getChildren().addAll(imageview, menuBar);
				group.getChildren().addAll(title,issuedid,returnDate,inputIssueId,inputReturnDate,btnReturn);
			}
		});

		/**
		 * View All users
		 */
		userMenu1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TableView table1 = new TableView();
				TableColumn tc1 = new TableColumn();
				TableColumn tc2 = new TableColumn();
				TableColumn tc3 = new TableColumn();
				TableColumn tc4 = new TableColumn();
				table1.setPrefSize(860, 360);
				table1.setTranslateX(50);
				table1.setTranslateY(165);
				/**
				 * table column add
				 */
				tc1.setText("UserID");
				tc1.setMinWidth(150);
				tc1.setStyle("-fx-alignment: CENTER;");
				tc1.setCellValueFactory(new PropertyValueFactory<user, String>("userid"));
				tc2.setText("Username");
				tc2.setMinWidth(250);
				tc2.setStyle("-fx-alignment: CENTER;");
				tc2.setCellValueFactory(new PropertyValueFactory<user, String>("username"));
				tc3.setText("Password");
				tc3.setMinWidth(250);
				tc3.setStyle("-fx-alignment: CENTER;");
				tc3.setCellValueFactory(new PropertyValueFactory<user, String>("password"));
				tc4.setText("IsAdmin (1-admin, 0-user)");
				tc4.setMinWidth(200);
				tc4.setStyle("-fx-alignment: CENTER;");
				tc4.setCellValueFactory(new PropertyValueFactory<user, String>("admin"));
				table1.setEditable(true);
				table1.getColumns().addAll(tc1, tc2, tc3, tc4);

				try {
					ObservableList<user> userList = FXCollections.observableArrayList();
					Connection connection = connect();
					String sql = "SELECT * FROM games_library.users;";
					ResultSet rs = connection.createStatement().executeQuery(sql);

					while (rs.next()) {
						user data = new user(rs.getInt("userid"), rs.getString("username"), rs.getString("password"),
								rs.getInt("admin"));
						userList.addAll(data); // add to list
					}
					table1.setItems(userList);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				group.getChildren().clear();
				group.getChildren().remove(imageview2);
				group.getChildren().addAll(imageview, menuBar, imageview2);
				group.getChildren().add(table1);
			}
		});

		/**
		 * Add New users
		 */
		userMenu2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Label title = new Label("Please enter the details of the user: ");
				Label username = new Label("Username");
				Label password = new Label("Password");
				TextField inputUsername = new TextField();
				TextField inputPassword = new TextField();
				Button btnAddUser = new Button("Add User");
				ToggleGroup tgroup = new ToggleGroup();
				RadioButton radio1 = new RadioButton("ADMIN");
				RadioButton radio2 = new RadioButton("USER");

				/**
				 * GUI design
				 */
				title.setTranslateX(350);
				title.setTranslateY(190);
				title.setStyle("-fx-text-fill: white;-fx-font-size: 18px; -fx-font-family: Impact;");
				username.setTranslateX(240);
				username.setTranslateY(240);
				username.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				password.setTranslateX(240);
				password.setTranslateY(300);
				password.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				inputUsername.setTranslateX(400);
				inputUsername.setTranslateY(240);
				inputUsername.setPrefWidth(300);
				inputUsername.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				inputPassword.setTranslateX(400);
				inputPassword.setTranslateY(300);
				inputPassword.setPrefWidth(300);
				inputPassword.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				radio1.setToggleGroup(tgroup);
				radio1.setTranslateX(400);
				radio1.setTranslateY(360);
				radio1.setStyle("-fx-text-fill: '#FFD966'; -fx-font-size: 16px; -fx-font-family: Impact");
				radio2.setToggleGroup(tgroup);
				radio2.setTranslateX(500);
				radio2.setTranslateY(360);
				radio2.setStyle("-fx-text-fill: '#FFD966'; -fx-font-size: 16px; -fx-font-family: Impact");
				btnAddUser.setTranslateX(365);
				btnAddUser.setTranslateY(440);
				btnAddUser.setPrefWidth(230);
				btnAddUser.setStyle(
						"-fx-text-fill: #1C4587; -fx-background-color: #FFD966; -fx-font-size: 22px; -fx-font-weight: bold; ;-fx-font-family: Impact");

				/**
				 * btnAddGame Action
				 */
				btnAddUser.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String username = inputUsername.getText();
						String password = inputPassword.getText();
						Boolean admin = false;

						Connection connection = connect();

						/**
						 * insert the data into DB
						 */
						if (radio1.isSelected()) {
							admin = true;
						}

						try {
							Statement statement = connection.createStatement();
							statement.executeUpdate("USE GAMES_LIBRARY");
							statement.executeUpdate("INSERT INTO USERS(USERNAME, PASSWORD, ADMIN) " + "VALUES ('"
									+ username + "', '" + password + "'," + admin + ")");
							JOptionPane.showMessageDialog(null, "Adding User...");
							/*
							 * clear after submitting
							 */
							inputUsername.clear();
							inputPassword.clear();
							radio1.setSelected(false);
							radio2.setSelected(false);
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1);
						}
					}
				});

				group.getChildren().clear();
				group.getChildren().addAll(imageview, menuBar);
				group.getChildren().addAll(username, password, inputPassword, radio1, radio2, btnAddUser, inputUsername,
						title);
			}
		});

		/**
		 * Delete users
		 */
		userMenu3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Label title = new Label("Please enter the user id for deleting: ");
				Label userid = new Label("User ID");
				TextField inputUserid = new TextField();
				Button btnDelUser = new Button("Delete User");

				/**
				 * GUI design
				 */
				title.setTranslateX(350);
				title.setTranslateY(250);
				title.setStyle("-fx-text-fill: white;-fx-font-size: 18px; -fx-font-family: Impact;");
				userid.setTranslateX(240);
				userid.setTranslateY(290);
				userid.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				inputUserid.setTranslateX(400);
				inputUserid.setTranslateY(290);
				inputUserid.setPrefWidth(300);
				inputUserid.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				btnDelUser.setTranslateX(365);
				btnDelUser.setTranslateY(340);
				btnDelUser.setPrefWidth(230);
				btnDelUser.setStyle(
						"-fx-text-fill: #1C4587; -fx-background-color: #FFD966; -fx-font-size: 22px; -fx-font-weight: bold; ;-fx-font-family: Impact");

				/**
				 * btnAddGame Action
				 */
				btnDelUser.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String uid = inputUserid.getText();

						/**
						 * insert the data into DB
						 */
						Connection connection = connect();
						try {
							Statement statement = connection.createStatement();
							statement.executeUpdate("USE GAMES_LIBRARY");

							String d1 = null;
							ResultSet r = statement.executeQuery("SELECT userid FROM users WHERE userid=" + uid);
							while (r.next()) {
								d1 = r.getString(1);
							}

							statement.executeUpdate("DELETE FROM users WHERE userid=" + uid);
							inputUserid.clear();
							JOptionPane.showMessageDialog(null, "User Deleted Successfully");
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1);
						}
					}
				});

				group.getChildren().clear();
				group.getChildren().addAll(imageview, menuBar);
				group.getChildren().addAll(userid, inputUserid, btnDelUser, title);
			}
		});

		/**
		 * View issued game
		 */
		issuedMenu1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TableView table1 = new TableView();
				TableColumn tc1 = new TableColumn();
				TableColumn tc2 = new TableColumn();
				TableColumn tc3 = new TableColumn();
				TableColumn<issued, String> tc4 = new TableColumn<issued, String>("issued_date");
				TableColumn<issued, String> tc5 = new TableColumn<issued, String>("return_date");
				TableColumn<issued, String> tc6 = new TableColumn<issued, String>("Period");
				TableColumn tc7 = new TableColumn();

				/**
				 * table column add
				 */
				table1.setPrefSize(860, 360);
				table1.setTranslateX(50);
				table1.setTranslateY(165);
				tc1.setText("IssuedID");
				tc1.setMinWidth(100);
				tc1.setStyle("-fx-alignment: CENTER;");
				tc1.setCellValueFactory(new PropertyValueFactory<issued, String>("issueid"));
				tc2.setText("UserID");
				tc2.setMinWidth(100);
				tc2.setStyle("-fx-alignment: CENTER;");
				tc2.setCellValueFactory(new PropertyValueFactory<issued, String>("userid"));
				tc3.setText("GameID");
				tc3.setMinWidth(100);
				tc3.setStyle("-fx-alignment: CENTER;");
				tc3.setCellValueFactory(new PropertyValueFactory<issued, String>("gameid"));
				tc4.setText("Issued Date");
				tc4.setMinWidth(150);
				tc4.setStyle("-fx-alignment: CENTER;");
				tc4.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIssuedDate()));
				tc5.setText("Return Date");
				tc5.setMinWidth(150);
				tc5.setStyle("-fx-alignment: CENTER;");
				tc5.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReturnDate()));
				tc6.setText("Period Days");
				tc6.setMinWidth(150);
				tc6.setStyle("-fx-alignment: CENTER;");
				tc6.setCellValueFactory(data -> data.getValue().periodProperty().asString()); // solution for error
				tc7.setText("Fine");
				tc7.setMinWidth(100);
				tc7.setStyle("-fx-alignment: CENTER;");
				tc7.setCellValueFactory(new PropertyValueFactory<issued, String>("fine"));
				
				
				table1.setEditable(true);
				table1.getColumns().addAll(tc1, tc2, tc3, tc4, tc5, tc6, tc7);

				try {
					ObservableList<issued> issuedList = FXCollections.observableArrayList();
					Connection connection = connect();
					String sql = "SELECT * FROM games_library.issued;";
					ResultSet rs = connection.createStatement().executeQuery(sql);

					while (rs.next()) {
						issued data = new issued(rs.getInt("issueid"), rs.getInt("userid"), rs.getInt("gameid"),
								rs.getString("issued_date"), rs.getString("return_date"), rs.getInt("period_days"), rs.getInt("fine"));
						issuedList.addAll(data); // add to list
					}
					table1.setItems(issuedList);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				group.getChildren().clear();
				group.getChildren().addAll(imageview, menuBar);
				group.getChildren().add(table1);
			}
		});

		/**
		 * issued game creating
		 */
		issuedMenu2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Label title = new Label("Please enter the details below: ");
				Label gameid = new Label("Game ID");
				Label userid = new Label("User ID");
				Label issuedDate = new Label("Issued Date");
				Label periodDays = new Label("Period");
				DatePicker inputIssuedDate = new DatePicker();
				TextField inputGameId = new TextField();
				TextField inputUserId = new TextField();
				TextField inputPeriodDays = new TextField();
				Button btnAdd = new Button("Add Issued Game");

				title.setTranslateX(350);
				title.setTranslateY(200);
				title.setStyle("-fx-text-fill: white;-fx-font-size: 18px; -fx-font-family: Impact;");
				gameid.setTranslateX(240);
				gameid.setTranslateY(240);
				gameid.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				userid.setTranslateX(240);
				userid.setTranslateY(290);
				userid.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				issuedDate.setTranslateX(240);
				issuedDate.setTranslateY(340);
				issuedDate.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				periodDays.setTranslateX(240);
				periodDays.setTranslateY(390);
				periodDays.setStyle(
						"-fx-text-fill: '#FFD966';-fx-font-size: 22px; -fx-font-weight: bold; -fx-font-family: Impact;");
				inputGameId.setTranslateX(400);
				inputGameId.setTranslateY(240);
				inputGameId.setPrefWidth(300);
				inputGameId.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				inputUserId.setTranslateX(400);
				inputUserId.setTranslateY(290);
				inputUserId.setPrefWidth(300);
				inputUserId.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				inputIssuedDate.setTranslateX(400);
				inputIssuedDate.setTranslateY(340);
				inputIssuedDate.setPrefWidth(300);
				inputIssuedDate.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				inputIssuedDate.setPromptText("YYYY-MM-DD"); // placeholder
				inputPeriodDays.setTranslateX(400);
				inputPeriodDays.setTranslateY(390);
				inputPeriodDays.setPrefWidth(300);
				inputPeriodDays.setStyle(
						"-fx-text-fill: '#1C4587'; -fx-font-size: 18px; -fx-font-weight: bold; ;-fx-font-family: Impact");
				inputPeriodDays.setPromptText("Days"); // placeholder
				btnAdd.setTranslateX(365);
				btnAdd.setTranslateY(450);
				btnAdd.setPrefWidth(230);
				btnAdd.setStyle(
						"-fx-text-fill: #1C4587; -fx-background-color: #FFD966; -fx-font-size: 22px; -fx-font-weight: bold; ;-fx-font-family: Impact");

				btnAdd.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						String user = inputUserId.getText();
						String game = inputGameId.getText();
						String p_int = inputPeriodDays.getText();
						LocalDate date = inputIssuedDate.getValue(); // date

						int period_int = Integer.parseInt(p_int);

						Connection connection = connect();

						try {
							Statement statement = connection.createStatement();
							statement.executeUpdate("USE GAMES_LIBRARY;");
							statement.executeUpdate("INSERT INTO ISSUED(USERID, GAMEID, ISSUED_DATE, PERIOD_DAYS)"
									+ "VALUES ('" + user + "', '" + game + "', '" + date + "', " + p_int + ")");
							statement.executeUpdate("UPDATE games_library.games "
									+ "SET available = 0 "
									+ "WHERE gameid = " + game);
							JOptionPane.showMessageDialog(null, "Game Issued Successfully");
							inputUserId.clear();
							inputGameId.clear();
							inputPeriodDays.clear();
							inputIssuedDate.setValue(null);
						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1);
						}
					}
				});

				group.getChildren().clear();
				group.getChildren().addAll(imageview, menuBar);
				group.getChildren().addAll(title, inputPeriodDays, inputIssuedDate, inputUserId, inputGameId,
						periodDays, issuedDate, gameid, userid, btnAdd);
			}
		});

		/**
		 * screen setting
		 */
		group.getChildren().addAll(imageview, menuBar, imageview2);
		adminScene = new Scene(group, 960, 565); // size
		adminStage = new Stage();
		adminStage.setScene(adminScene);
		adminStage.setTitle("Video Game Library Admin Menu"); // title
		adminStage.show();
	}

	private void userMenu(int id) {
		/**
		 * setting objects
		 */
		Image userbg = new Image("file:images/java-user.png"); // background
		ImageView imageview = new ImageView(userbg);
		Image userWelcome = new Image("file:images/java-user-welcome.png"); // welcome image
		ImageView imageview2 = new ImageView(userWelcome);
		Button btnView = new Button("View All");
		Button btnMyGame = new Button("My Game");
		Group group = new Group();
		Scene userScene;
		Stage userStage;

		/**
		 * GUI design
		 */
		imageview2.setTranslateX(50);
		imageview2.setTranslateY(200);
		btnView.setTranslateX(350);
		btnView.setTranslateY(150);
		btnView.setPrefWidth(120);
		btnView.setStyle(
				"-fx-text-fill: #1C4587; -fx-background-color: #FFD966; -fx-font-size: 16px; -fx-font-weight: bold; ;-fx-font-family: Impact");
		btnMyGame.setTranslateX(500);
		btnMyGame.setTranslateY(150);
		btnMyGame.setPrefWidth(120);
		btnMyGame.setStyle(
				"-fx-text-fill: #1C4587; -fx-background-color: #FFD966; -fx-font-size: 16px; -fx-font-weight: bold; ;-fx-font-family: Impact");

		/**
		 * btnView review all game
		 */
		btnView.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TableView table = new TableView();
				TableColumn tc1 = new TableColumn();
				TableColumn tc2 = new TableColumn();
				TableColumn tc3 = new TableColumn();
				TableColumn tc4 = new TableColumn();
				TableColumn tc5 = new TableColumn();
				/**
				 * table column add
				 */
				table.setPrefSize(860, 300);
				table.setTranslateX(50);
				table.setTranslateY(200);
				tc1.setText("GameID");
				tc1.setMinWidth(100);
				tc1.setStyle("-fx-alignment: CENTER;");
				tc1.setCellValueFactory(new PropertyValueFactory<game, String>("gameid"));
				tc2.setText("GameName");
				tc2.setMinWidth(305);
				tc2.setStyle("-fx-alignment: CENTER;");
				tc2.setCellValueFactory(new PropertyValueFactory<game, String>("gamename"));
				tc3.setText("Platform");
				tc3.setMinWidth(150);
				tc3.setStyle("-fx-alignment: CENTER;");
				tc3.setCellValueFactory(new PropertyValueFactory<game, String>("platform"));
				tc4.setText("Price");
				tc4.setMinWidth(150);
				tc4.setStyle("-fx-alignment: CENTER;");
				tc4.setCellValueFactory(new PropertyValueFactory<game, String>("price"));
				tc5.setText("Available");
				tc5.setMinWidth(150);
				tc5.setStyle("-fx-alignment: CENTER;");
				tc5.setCellValueFactory(new PropertyValueFactory<game, String>("available"));
				table.setEditable(true);
				table.getColumns().addAll(tc1, tc2, tc3, tc4, tc5);

				try {
					ObservableList<game> gameList = FXCollections.observableArrayList();
					Connection connection = connect();
					/**
					 * select available game in the list from DB
					 */
					String sql = "SELECT * FROM games_library.games WHERE available = 1 ORDER BY gameid;";
					ResultSet rs = connection.createStatement().executeQuery(sql);

					while (rs.next()) {
						game data = new game(rs.getInt("gameid"), rs.getString("gamename"), rs.getString("platform"),
								rs.getInt("price"), rs.getInt("available"));
						gameList.addAll(data);
					}
					table.setItems(gameList);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				group.getChildren().clear();
				group.getChildren().addAll(imageview,btnView,btnMyGame,table);
			}
		});

		/**
		 * btnMyGame
		 */
		btnMyGame.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				TableView table = new TableView();
				TableColumn tc1 = new TableColumn();
				TableColumn tc2 = new TableColumn();
				TableColumn tc3 = new TableColumn();
				TableColumn tc4 = new TableColumn();
				TableColumn tc5 = new TableColumn();
				table.setPrefSize(860, 300);
				table.setTranslateX(50);
				table.setTranslateY(200);
				/**
				 * table column add
				 */
				tc1.setText("GameID");
				tc1.setMinWidth(100);
				tc1.setStyle("-fx-alignment: CENTER;");
				tc1.setCellValueFactory(new PropertyValueFactory<game, String>("gameid"));
				tc2.setText("GameName");
				tc2.setMinWidth(305);
				tc2.setStyle("-fx-alignment: CENTER;");
				tc2.setCellValueFactory(new PropertyValueFactory<game, String>("gamename"));
				tc3.setText("Platform");
				tc3.setMinWidth(150);
				tc3.setStyle("-fx-alignment: CENTER;");
				tc3.setCellValueFactory(new PropertyValueFactory<game, String>("platform"));
				tc4.setText("Price");
				tc4.setMinWidth(150);
				tc4.setStyle("-fx-alignment: CENTER;");
				tc4.setCellValueFactory(new PropertyValueFactory<game, String>("price"));
				tc5.setText("Available");
				tc5.setMinWidth(150);
				tc5.setStyle("-fx-alignment: CENTER;");
				tc5.setCellValueFactory(new PropertyValueFactory<game, String>("available"));
				table.setEditable(true);
				table.getColumns().addAll(tc1, tc2, tc3, tc4, tc5);

				try {
					ObservableList<game> gameList = FXCollections.observableArrayList();
					Connection connection = connect();
					/**
					 * select available game in the list from DB
					 */
					Statement statement = connection.createStatement();
					statement.executeUpdate("USE GAMES_LIBRARY");
					String sql = "SELECT G.gameid, G.gamename, G.platform, G.price, G.available "
							+ "FROM games_library.games AS G "
							+ "LEFT JOIN games_library.issued AS I "
							+ "ON G.gameid = I.gameid "
							+ "WHERE I.userid = " + id + ";";
								
					ResultSet rs;
					rs = connection.createStatement().executeQuery(sql);
					
					while (rs.next()) {
						game data = new game(rs.getInt("gameid"), rs.getString("gamename"), rs.getString("platform"),
								rs.getInt("price"), rs.getInt("available"));
						gameList.addAll(data);
					}
					table.setItems(gameList);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				group.getChildren().clear();
				group.getChildren().addAll(imageview,btnView,btnMyGame,table);
			}
		});

		/**
		 * screen setting
		 */

		group.getChildren().addAll(imageview, btnView, btnMyGame, imageview2);
		userScene = new Scene(group, 960, 540); // size
		userStage = new Stage();
		userStage.setScene(userScene);
		userStage.setTitle("Video Game Library - User Menu"); // title
		userStage.show();
	}

	/**
	 * connect the database
	 */
	private static Connection connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loading...");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/mysql?user=root&password=Pa55word01");
			System.out.println("Connected to MySQL");
			return connection;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * create the table and insert values
	 */
	private static void create() {
		try {
			Connection connection = connect();
			ResultSet rs = connection.getMetaData().getCatalogs();
			// to iterate catalogs in the ResultSet:
			while (rs.next()) {
				// database name = position 1:
				String DB = rs.getString(1);
				if (DB.equals("GAMES_LIBRARY")) { // GAMES_LIBRARY
					System.out.println("Connecting to Games_Library...");
					Statement statement = connection.createStatement();
					// Drop to reset the database if it already exists:
					String mysql = "Drop Database...";
					statement.executeUpdate(mysql);
				}
			}
			Statement statement = connection.createStatement();

			String mysql = "CREATE DATABASE IF NOT EXISTS GAMES_LIBRARY";
			statement.executeUpdate(mysql);
			statement.executeUpdate("USE GAMES_LIBRARY");
			// to create users table :
			String mysql2 = "CREATE TABLE IF NOT EXISTS USERS(USERID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(30) UNIQUE, PASSWORD VARCHAR(30), ADMIN BOOLEAN)";
			statement.executeUpdate(mysql2);
			// to insert into user table :
			statement.executeUpdate(
					"INSERT INTO USERS(username, password, admin) VALUES ('admin', 'admin', 1), ('oprah','1234',0), ('mario','1234',0), ('diyaden','1234',0);");
			// create Games Table :
			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS GAMES(GAMEID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,GAMENAME VARCHAR(50) UNIQUE, PLATFORM VARCHAR(30), PRICE INT, AVAILABLE BOOLEAN)");
			// create 'issued' table :
			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS ISSUED(ISSUEID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, USERID INT, GAMEID INT, ISSUED_DATE VARCHAR(50), RETURN_DATE VARCHAR(50), PERIOD_DAYS INT, FINE INT, FOREIGN KEY (userid) REFERENCES users(userid), FOREIGN KEY (gameid) REFERENCES games(gameid))");
			// insert new Games table :
			statement.executeUpdate("INSERT INTO GAMES(GAMENAME, PLATFORM, PRICE, available) "
					+ "VALUES ('Minecraft', 'Xbox One', 45, 1),"
					+ "('Kirby and the Forgotten Land', 'Nintendo Switch', 60, 1)," + "('Elden Ring', 'PS4', 60, 1),"
					+ "('Mario Party Superstars', 'Nintendo Switch', 57, 1),"
					+ "('Super Mario 3D World', 'Nintendo Switch', 46, 1),"
					+ "('Mario Kart 8 Deluxe', 'Nintendo Switch', 55, 1)," + "('Just Dance 2022', 'Xbox One', 50, 1),"
					+ "('The Legend of Zelda','Nintendo Switch', 40, 1),"
					+ "('Animal Crossing', 'Nintendo Switch', 58, 1),"
					+ "('Lego Star Wars: The Skywalker Saga', 'PS5', 60, 1),"
					+ "('Horizon Forbidden West', 'PS5', 70, 1)," + "('Hogwarts Legacy', 'PS5', 70, 1),"
					+ "('FIFA 22', 'PS5', 25, 1)," + "('Grand Theft Auto V', 'Xbox 360', 22, 1);");

			rs.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}