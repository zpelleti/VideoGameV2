import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class user {
	private final IntegerProperty userid = new SimpleIntegerProperty(this,"userid");
	private final StringProperty username = new SimpleStringProperty(this,"username");
	private final StringProperty password = new SimpleStringProperty(this,"password");
	private final IntegerProperty admin = new SimpleIntegerProperty(this,"admin");
	/**
	 * setting data property
	 */
	public IntegerProperty useridProperty() {
		return userid;
	}
	public final int getUserid() {
		return useridProperty().get();
	}
	public final void setUserid(int id) {
		useridProperty().set(id);
	}
	public StringProperty usernameProperty() {
		return username;
	}
	public final String getUsername() {
		return usernameProperty().get();
	}
	public final void setUsername(String name) {
		usernameProperty().set(name);
	}
	public StringProperty passwordProperty() {
		return password;
	}
	public final String getPassword() {
		return passwordProperty().get();
	}
	public final void setPassword(String pw) {
		passwordProperty().set(pw);
	}
	public IntegerProperty adminProperty() {
		return admin;
	}
	public final int getAdmin() {
		return adminProperty().get();
	}
	public final void setAdmin(int a) {
		adminProperty().set(a);
	}
	
	public user(int userid, String username, String password, int admin) {
		setUserid(userid);
		setUsername(username);
		setPassword(password);
		setAdmin(admin);
	}
}
