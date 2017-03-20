package attendence.gui.model;

import attendence.bll.DateTimeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author sBirke
 */
public class DateTimeModel
{

    private final DateTimeManager dateTimeManager;
    private final ObservableList<String> months;

    public DateTimeModel()
    {
        this.dateTimeManager = new DateTimeManager();
        this.months = FXCollections.observableArrayList(dateTimeManager.getMonthsCapitalized());
    }

    public ObservableList<String> getFormattedMonths()
    {
        return this.months;
    }

    public String getCurrentMonth()
    {
        return dateTimeManager.getCurrentMonth();
    }

    public int getCurrentDayOfMonth()
    {
        return dateTimeManager.getCurrentDayOfMonth();
    }

}
