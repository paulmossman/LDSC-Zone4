package paulmossman;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import paulmossman.deltas.Delta;
import paulmossman.deltas.Registration;
import paulmossman.csv.base.TmpBaseCsvRow;
import paulmossman.csv_output.CsvClubBoatList;
import paulmossman.csv_output.CsvKeelboatList;
import paulmossman.csv_output.CsvMemberMailingList;
import paulmossman.csv_output.CsvNewMemberWelcome;

public class Main {

   public static void main(String[] args) throws Exception {

      String year;
      if (args.length == 0) {
         year = String.valueOf(Year.now().getValue());
      } else {
         year = args[0];
      }

      int yearInt = Integer.parseInt(year);

      List<? extends TmpBaseCsvRow> allRegistrantRows = Util.getAllRegistrantsByYear(yearInt);
      if (allRegistrantRows == null) {
         System.err.println("Parameter error");
         return;
      }

      String dataDir = allRegistrantRows.get(0).getDataDir();
      String statisticsFilePath = dataDir + "Statistics-" + allRegistrantRows.get(0).simple_getYearString() + ".txt";
      PrintStream out = new PrintStream(new FileOutputStream(statisticsFilePath, false));

      Util.printStatistics(allRegistrantRows, out, yearInt);


      // Util.printYouthMemberAgeGroupTotals(allRegistrantRows, System.out);

      out.close();
      Util.printFileToStdout(statisticsFilePath);
      
      Anomalies.check(allRegistrantRows);
      
      Util.generateConciseAdultMembershipCSV(allRegistrantRows);

      Util.generateRegistrationQuestionsLists(allRegistrantRows);
      
      Util.generateNeMembersReport(allRegistrantRows);
      Util.generateConciseMembershipCSV(allRegistrantRows);

      CsvMemberMailingList.builder().build().generateTodos(allRegistrantRows, dataDir);
      CsvNewMemberWelcome.builder().build().generateTodos(allRegistrantRows, dataDir, true);
      CsvClubBoatList.builder().build().generateTodos(allRegistrantRows, dataDir);
      CsvKeelboatList.builder().build().generateTodos(allRegistrantRows, dataDir);

//      generateRegistrationNumbersOverTimeCSV(allRegistrantRows);
//

//      
//      printNewMemberDetails(allRegistrantRows);

      //Util.printMembersWhoJoinedBefore2021(allRegistrantRows, out);

   
   
   }

   public static List<? extends TmpBaseCsvRow> getNewFullMemberRows(List<? extends TmpBaseCsvRow> CSVRows) {

      // @formatter:off
      return CSVRows.stream().filter(row -> row.getFirstHalfInitiationPayment().equals("✓")).collect(Collectors.toList());
      // @formatter:on
   }

   public static List<? extends TmpBaseCsvRow> getNewMemberRows(List<? extends TmpBaseCsvRow> CSVRows) {

      // @formatter:off
      return Stream.concat(
            getNewFullMemberRows(CSVRows).stream(),
            Stream.concat(
                  CSVRows.stream().filter(row -> row.getNewCrewLateFeeExemption().equals("✓")),
                  CSVRows.stream().filter(row -> row.getNewYoungAdultLateFeeExemption().equals("✓"))
            )
      ).collect(Collectors.toList());
      // @formatter:on
   }

}
