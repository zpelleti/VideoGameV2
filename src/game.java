import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class game {
	private final IntegerProperty gameid = new SimpleIntegerProperty(this,"gameid");
	private final StringProperty gamename = new SimpleStringProperty(this,"gamename");
	private final StringProperty platform = new SimpleStringProperty(this,"platform");
	private final IntegerProperty price = new SimpleIntegerProperty(this,"price");
	private final IntegerProperty available = new SimpleIntegerProperty(this,"available");
	/**
	 * setting data property
	 */
	public IntegerProperty gameidProperty() {
		return gameid;
	}
	public final int getGameid() {
		return gameidProperty().get();
	}
	public final void setGameid(int id) {
		gameidProperty().set(id);
	}
	public StringProperty gamenameProperty() {
		return gamename;
	}
	public final String getGamename() {
		return gamenameProperty().get();
	}
	public final void setGamename(String name) {
		gamenameProperty().set(name);
	}
	public StringProperty platformProperty() {
		return platform;
	}
	public final String getPlatform() {
		return platformProperty().get();
	}
	public final void setPlatform(String p) {
		platformProperty().set(p);
	}
	public IntegerProperty priceProperty() {
		return price;
	}
	public final int getPrice() {
		return priceProperty().get();
	}
	public final void setPrice(int p) {
		priceProperty().set(p);
	}
	public IntegerProperty availableProperty() {
		return available;
	}
	public final int getAvailable() {
		return availableProperty().get();
	}
	public final void setAvailable(int a) {
		availableProperty().set(a);
	}

	public game(int id, String name, String platform, int price, int available) {
		setGameid(id);
		setGamename(name);
		setPlatform(platform);
		setPrice(price);
		setAvailable(available);
	} 
}
