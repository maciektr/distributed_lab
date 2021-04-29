package monitoring.commands;

import java.util.ArrayList;

public class DbSave implements Command {
    public final ArrayList<Integer> satellites;

    public DbSave(ArrayList<Integer> satellites) {
        this.satellites = satellites;
    }

}
