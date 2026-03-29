package paulmossman.deltas;

import java.util.ArrayList;
import java.util.List;

// All registrations for one season.
public class AllRegistrations {
    
    public List<Registration> list;

    public AllRegistrations() {
        list = new ArrayList<Registration>();
    }

    public boolean overlaps(Registration other) {
        for( Registration list_reg : list) {
            if( list_reg.overlaps(other)) {
                return true;
            }
        }

        return false;
    }

}
