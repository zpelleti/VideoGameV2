import java.sql.Date;
import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class issued {
	private final IntegerProperty issueid = new SimpleIntegerProperty(this,"issueid");
	private final IntegerProperty userid = new SimpleIntegerProperty(this,"userid");
	private final IntegerProperty gameid = new SimpleIntegerProperty(this,"gameid");
	private final StringProperty issued_date = new SimpleStringProperty(this,"issued_date");
	private final StringProperty return_date = new SimpleStringProperty(this,"return_date");
	private final IntegerProperty period_days = new SimpleIntegerProperty(this,"period_days");
	private final IntegerProperty fine = new SimpleIntegerProperty(this,"fine");
	/**
	 * setting data property
	 */
	public IntegerProperty issueidProperty() {
		return issueid;
	}
	public final int getIssueid() {
		return issueidProperty().get();
	}
	public final void setIssueid(int id) {
		issueidProperty().set(id);
	}
	public IntegerProperty useridProperty() {
		return userid;
	}
	public final int getUserid() {
		return useridProperty().get();
	}
	public final void setUserid(int id) {
		useridProperty().set(id);
	}
	public IntegerProperty gameidProperty() {
		return gameid;
	}
	public final int getGameid() {
		return gameidProperty().get();
	}
	public final void setGameid(int id) {
		gameidProperty().set(id);
	}
	public StringProperty issuedateProperty() {
		return issued_date;
	}
	public final String getIssuedDate() {
		return issuedateProperty().get();
	}
	public final void setIssuedDate(String date1) {
		issuedateProperty().set(date1);
	}
	public StringProperty returndateProperty() {
		return return_date;
	}
	public final String getReturnDate() {
		return returndateProperty().get();
	}
	public final void setReturnDate(String date2) {
		returndateProperty().set(date2);
	}
	public IntegerProperty periodProperty() {
		return period_days;
	}
	public final int getPeriod() {
		return periodProperty().get();
	}
	public final void setPeriod(int days) {
		periodProperty().set(days);
	}
	public IntegerProperty fineProperty() {
		return fine;
	}
	public final int getFine() {
		return fineProperty().get();
	}
	public final void setFine(int fine) {
		fineProperty().set(fine);
	}

	public issued(int iid, int uid, int gid, String date, String date2, int days, int fine) {
		setIssueid(iid);
		setUserid(uid);
		setGameid(gid);
		setIssuedDate(date);
		setReturnDate(date2);
		setPeriod(days);
		setFine(fine);
	} 
}
