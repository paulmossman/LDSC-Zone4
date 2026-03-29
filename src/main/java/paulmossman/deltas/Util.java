package paulmossman.deltas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paulmossman.csv.base.TmpBaseCsvRow;

public class Util {
   
   public static AllRegistrations buildAllRegistrations(List<? extends TmpBaseCsvRow> allRegistrantRows) {
      List<Registration> registrationList = Registration.getRegistrationsFromMemberRows(allRegistrantRows);

      AllRegistrations allRegistrations = new AllRegistrations();
      allRegistrations.list.addAll(registrationList);

      return allRegistrations;
   }



    public static List<String> findMembersWhoDidNotReturn(List<? extends TmpBaseCsvRow> allRegistrantRowsPrevious,
          List<? extends TmpBaseCsvRow> allRegistrantRowsSubsequent) {

       AllRegistrations previous = buildAllRegistrations(allRegistrantRowsPrevious);

       AllRegistrations subsequent = buildAllRegistrations(allRegistrantRowsSubsequent);

       List<String> didNotReturnEmails = new ArrayList<String>();

       for (Registration previous_reg : previous.list) {

          if (!subsequent.overlaps(previous_reg)) {

             // This previous registration did not return.
             for (String email : previous_reg.getAdultEmails()) {
                didNotReturnEmails.add(email);
             }
          }
       }

       return didNotReturnEmails;
    }

}
