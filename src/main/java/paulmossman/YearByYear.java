package paulmossman;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import paulmossman.csv.base.TmpBaseCsvRow;

public class YearByYear {
    

   public static void main(String[] args) throws Exception {

      String csvFilePath = Util.getZone4DataDir() + "/YearByYear.csv";
      PrintStream out = new PrintStream(new FileOutputStream(csvFilePath, false));

      System.out.println(csvFilePath);

      out.println("Year,Total Registrations,Full Regs,Crew Regs,Young Adult Regs, Alumni Regs," +
         "Total Regs w Club Boat," +
         "Total members,Adult members,Youth members," +
         "Total new members,New Full,New Crew,New Young Adult"
      );

      for(int year = 2020; year <= 2026; year++) {

         List<? extends TmpBaseCsvRow> allRegistrantRows = Util.getAllRegistrantsByYear(year);
            if (allRegistrantRows == null) {
               System.err.println("Parameter error");
               return;
         }

         out.println(String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,",
            year, 
            Util.getOneRowForEachCart(allRegistrantRows).size(),
            Util.getOneRowForEachFullMembershipCart(allRegistrantRows).size(),
            Util.getOneRowForEachCrewMembershipCart(allRegistrantRows).size(),
            Util.getOneRowForEachYoungAdultMembershipCart(allRegistrantRows).size(),
            Util.getOneRowForEachAlumniMembershipCart(allRegistrantRows).size(),
            Util.getOneRowForEachClubBoatAccessMembershipCart(allRegistrantRows).size(),
            allRegistrantRows.size(),
            Util.getAdultMemberRows(allRegistrantRows).size(),
            Util.getYouthMemberRows(allRegistrantRows).size(),
            Util.getNewMemberRows(allRegistrantRows).size(),
            Util.getNewFullMemberRows(allRegistrantRows).size(),
            Util.getNewCrewMemberRows(allRegistrantRows).size(),
            Util.getNewYoungAdultMemberRows(allRegistrantRows).size()
         ));
      }

      out.close();
   }
}
