package paulmossman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import paulmossman.csv.base.TmpBaseCsvRow;
import paulmossman.deltas.Delta;
import paulmossman.registration.Registration;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class Util {

   public static void main(String[] args) throws Exception {
      printAllFields();
   }

   public static void printFields(String dir, Path filename) throws CsvValidationException, IOException {
      String filePath = dir + "/" + filename;
      
      try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
         String[] header = reader.readNext(); // Read the first line (header)

         if (header != null) {

            FileWriter fileWriter = new FileWriter(dir + "/" + "CsvFields.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int i = 0; i < header.length; i++) {
               printWriter.printf("Column %d: %s%n", i, header[i]);
            }
            printWriter.close();

         } else {
            System.out.println("CSV file is empty or has no header: " + filePath);
         }
      }
      System.out.println();
   }

   public static void printAllFields() throws IOException, CsvValidationException {
      
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(getZone4DataDir()))) {
         for (Path dir : stream) {
            if (Files.isDirectory(dir)) {
               try (Stream<Path> files = Files.list(dir)) {
                  Optional<Path> match = files.filter(p -> {
                     String name = p.getFileName().toString();
                     return name.startsWith("All Registrants") && name.endsWith(".csv");
                  }).findFirst();

                  if (match.isPresent()) {
                     printFields(getZone4DataDir() + "/" + dir.getFileName(), match.get().getFileName());
                  }
               }
            }
         }
      }
   }


   public static String getZone4DataDir() throws IOException {
      Properties prop = new Properties();
      String fileName = "app.config";
      FileInputStream fis = new FileInputStream(fileName);
      prop.load(fis);

      return prop.getProperty("Zone4-data-dir");
   }

   public static List<? extends TmpBaseCsvRow> getAllRegistrantsByYear(String year) throws IOException {
      return getAllRegistrantsByYear(Integer.parseInt(year));
   }

   public static List<? extends TmpBaseCsvRow> getAllRegistrantsByYear(int year) throws IOException {
    String packageName = "paulmossman.csv.y" + String.format("%04d", year);
    String className = packageName + ".CsvAllRegistrants";
    
    try {
        Class<?> csvClass = Class.forName(className);
        // Get the singleton instance (assuming get() is static)
        var csvInstance = csvClass.getMethod("get").invoke(null);
        // Get and invoke loadFromCsv() method
        var loadMethod = csvClass.getMethod("loadFromCsv");
        return (List<? extends TmpBaseCsvRow>) loadMethod.invoke(csvInstance);
    } catch (ClassNotFoundException e) {
        System.err.println("No class found for year: " + year + " (" + className + ")");
        return null;
    } catch (ReflectiveOperationException | RuntimeException e) {
        e.printStackTrace();
        System.err.println("Error loading registrants for year: " + year + ": " + e.getMessage());
        return null;
    }
}
   // public static List<? extends TmpBaseCsvRow> getAllRegistrantsByYear(int year) throws IOException {
   //    switch (year) {
   //    case 2025:
   //       return paulmossman.csv.y2025.CsvAllRegistrants.get().loadFromCsv();
   //    case 2024:
   //       return paulmossman.csv.y2024.CsvAllRegistrants.get().loadFromCsv();
   //    case 2023:
   //       return paulmossman.csv.y2023.CsvAllRegistrants.get().loadFromCsv();
   //    case 2022:
   //       return paulmossman.csv.y2022.CsvAllRegistrants.get().loadFromCsv();
   //    case 2021:
   //       return paulmossman.csv.y2021.CsvAllRegistrants.get().loadFromCsv();
   //    default:
   //       System.err.println("No class for year: " + year);
   //       return null;
   //    }
   // }

   public static List<? extends TmpBaseCsvRow>  getByCartNumber(List<? extends TmpBaseCsvRow> allRegistrantRows, String cartNumber) {

      return allRegistrantRows.stream().filter(obj -> cartNumber.equals(obj.getCartNumber())).collect(Collectors.toList());
   }

   public static void sortByCartNumber(List<? extends TmpBaseCsvRow> allRegistrantRows) {
      allRegistrantRows.sort((o1, o2) -> o1.getCartNumber().compareTo(o2.getCartNumber()));
   }

   private static List<? extends TmpBaseCsvRow> getAllRowsMatchingSpecifiedPredicate(
         List<? extends TmpBaseCsvRow> allRegistrantRows, Predicate<TmpBaseCsvRow> predicate) {

      return allRegistrantRows.stream().filter(predicate).collect(Collectors.toList());
   }

   public static List<? extends TmpBaseCsvRow> getYouthMemberRows(List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isYouth);
   }

   public static List<? extends TmpBaseCsvRow> getAdultMemberRows(List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isAdult);
   }

   public static List<? extends TmpBaseCsvRow> getMooringTypeKeelboatRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isMooringTypeKeelboat);
   }

   public static List<? extends TmpBaseCsvRow> getMooringTypeMonohullDinghyRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isMooringTypeMonohullDinghy);
   }

   public static List<? extends TmpBaseCsvRow> getMooringTypeCatRows(List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isMooringTypeCat);
   }

   public static List<? extends TmpBaseCsvRow> getMooringTypeKCSRows(List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isMooringTypeKCS);
   }

   public static List<? extends TmpBaseCsvRow> getMooringTypeNoBoatRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isMooringTypeNoBoat);
   }

   public static List<? extends TmpBaseCsvRow> getMooringTypeClubBoatOnlyRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isMooringTypeClubBoatOnly);
   }

   // All Carts have the same Membership Privilege, so just get the first one.
   public static List<? extends TmpBaseCsvRow> getOneRowForEachCart(List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return new ArrayList<>(allRegistrantRows.stream()
            .collect(Collectors.toMap(TmpBaseCsvRow::getCartNumber, // Key: cart number
                  obj -> obj, // Value: object itself
                  (existing, replacement) -> existing // Handle duplicates
            )).values());
   }

   private static List<? extends TmpBaseCsvRow> getOneRowForEachSpecifiedPredicateCart(
         List<? extends TmpBaseCsvRow> allRegistrantRows, Predicate<TmpBaseCsvRow> predicate) {

      return getOneRowForEachCart(allRegistrantRows).stream().filter(predicate).collect(Collectors.toList());
   }

   public static List<? extends TmpBaseCsvRow> getOneRowForEachFullMembershipCart(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return getOneRowForEachSpecifiedPredicateCart(allRegistrantRows, TmpBaseCsvRow::isFullPriviledge);
   }

   public static List<? extends TmpBaseCsvRow> getOneRowForEachCrewMembershipCart(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return getOneRowForEachSpecifiedPredicateCart(allRegistrantRows, TmpBaseCsvRow::isCrewPriviledge);
   }

   public static List<? extends TmpBaseCsvRow> getOneRowForEachYoungAdultMembershipCart(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return getOneRowForEachSpecifiedPredicateCart(allRegistrantRows, TmpBaseCsvRow::isYoungAdultPriviledge);
   }

   public static List<? extends TmpBaseCsvRow> getOneRowForEachAlumniMembershipCart(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return getOneRowForEachSpecifiedPredicateCart(allRegistrantRows, TmpBaseCsvRow::isAlumniPriviledge);
   }

   public static List<? extends TmpBaseCsvRow> getOneRowForEachClubBoatAccessMembershipCart(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return getOneRowForEachSpecifiedPredicateCart(allRegistrantRows, TmpBaseCsvRow::hasClubBoatAccess);
   }

   public static List<? extends TmpBaseCsvRow> getNewFullMemberRows(List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isNewFullMember);
   }

   public static List<? extends TmpBaseCsvRow> getNewCrewMemberRows(List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isNewCrewMember);
   }

   public static List<? extends TmpBaseCsvRow> getNewYoungAdultMemberRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isNewYoungAdultMember);
   }

   public static List<? extends TmpBaseCsvRow> getNewMemberRows(List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::isNewMember);
   }

   // Total (Full+YA) Members with Club Boat access
   public static List<? extends TmpBaseCsvRow> getAllMembersWithClubBoatAccessRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, TmpBaseCsvRow::hasClubBoatAccess);
   }

   // Total New (Full+YA) Members with Club Boat access
   public static List<? extends TmpBaseCsvRow> getNewMembersWithClubBoatAccessRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {

      return getAllRowsMatchingSpecifiedPredicate(getNewMemberRows(allRegistrantRows),
            TmpBaseCsvRow::hasClubBoatAccess);
   }

   public static void generateConciseMembershipCSV(List<? extends TmpBaseCsvRow> allRegistrantRows) throws IOException {
      String dataDir = allRegistrantRows.get(0).getDataDir();
      String filePath = dataDir + "ConciseMembershipList-" + allRegistrantRows.get(0).simple_getYearString() + ".csv";

      FileWriter fileWriter = new FileWriter(filePath);
      PrintWriter printWriter = new PrintWriter(fileWriter);

      printWriter.println("Cart #,First Name,Last Name,Age,Email,Year joined,Mooring Type,Club Boat");

      for (TmpBaseCsvRow registrant : allRegistrantRows) {

         printWriter
               .println(registrant.getCartNumber() + "," + registrant.getFirstName() + "," + registrant.getLastName()
                     + "," + registrant.getAgeGroup() + "," + registrant.getEmail() + "," + registrant.getYearJoined()
                     + "," + registrant.getMooringType() + "," + (registrant.hasClubBoatAccess() ? "Yes" : "No"));
      }

      printWriter.close();
   }

   // A report on all new members.
   public static void generateNeMembersReport(List<? extends TmpBaseCsvRow> allRegistrantRows) throws IOException {

      String dataDir = allRegistrantRows.get(0).getDataDir();
      String filePath = dataDir + "NewMembersReport-" + allRegistrantRows.get(0).simple_getYearString() + ".txt";

      FileWriter fileWriter = new FileWriter(filePath);
      PrintWriter printWriter = new PrintWriter(fileWriter);

      printWriter.println("Updated: " + getFormttedDate().replaceAll(",", ""));
      printWriter.println();

      List<? extends TmpBaseCsvRow> newMembersRows = getNewMemberRows(allRegistrantRows);
      printWriter.println("Number of new Members (People): " + newMembersRows.size());

      List<Registration> newRegistrations = Registration.getRegistrationsFromMemberRows(newMembersRows);
      printWriter.println("Number of new Registrations: " + newRegistrations.size());
      printWriter.println();

      for (Registration newRegistration : newRegistrations) {

         printWriter.println("Membership type: " + newRegistration.getMembers().get(0).getMembershipPrivilege());
         for (TmpBaseCsvRow newMember : newRegistration.getMembers()) {
            printWriter
                  .println(" - " + newMember.getFullName() + " (" + (newMember.isAdult() ? "Adult" : "Youth") + ")");
         }

         printWriter.println();
      }

      printWriter.close();
   }

   public static void generateRegistrationQuestionsLists(List<? extends TmpBaseCsvRow> allRegistrantRows)
         throws IOException {

      String directoryPath = allRegistrantRows.get(0).getDataDir() + "/RegistrationQuestionsLists";
      File directory = new File(directoryPath);
      if (!directory.exists()) {
          directory.mkdir();
      }
      

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "AdultSailTraining",
            TmpBaseCsvRow::questionAdultSailTraining, "Are you or your family interested in Adult Sail Training?");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "AdultSailRacing",
            TmpBaseCsvRow::questionAdultSailRacing, "Are you interested in Adult Sail Racing?");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "YouthSailTraining",
            TmpBaseCsvRow::questionYouthSailTraining, "Are you or your family interested in Youth Sail Training?");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "JuniorSailRacing",
            TmpBaseCsvRow::questionJuniorSailRacing, "Are you or your family interested in Junior Sail Racing?");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "RacingThisYear",
            TmpBaseCsvRow::questionRacingThisYear, "Are you planning on racing this year?");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "ServeBoardOfDirectors",
            TmpBaseCsvRow::questionServeBoardOfDirectors, "Will you consider serving on the LDSC Board of Directors?");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "MentorNewerSailor",
            TmpBaseCsvRow::questionMentorNewerSailor, "Will you be a mentor to a newer sailor?");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "CarpentryOrMaintenanceSkills",
            TmpBaseCsvRow::questionCarpentryOrBuildingMaintenanceSkill,
            "Do you have carpentry or other building maintenance skills?");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "BoatRepairExperience",
            TmpBaseCsvRow::questionBoatRepairExperience, "Do you have boat repair experience? (e.g. Fibreglass)");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "RunOnWaterEvent",
            TmpBaseCsvRow::questionRunOnWaterEvent, "Are you interested in running an 'On the water' event?");

      generateOneRegistrationQuestionsList(allRegistrantRows, directoryPath, "RunSocialEvent",
            TmpBaseCsvRow::questionRunSocialEvent, "Are you interested in running a 'Social' event?");
   }

   private static void generateOneRegistrationQuestionsList(List<? extends TmpBaseCsvRow> allRegistrantRows,
         String directoryPath, String filename, Predicate<TmpBaseCsvRow> predicate, String questionText)
         throws IOException {

      FileWriter fileWriter = new FileWriter(directoryPath + "/" + filename + ".txt");
      PrintWriter printWriter = new PrintWriter(fileWriter);

      List<? extends TmpBaseCsvRow> yesRegistrants = getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, predicate);

      printWriter.println("Updated: " + getFormttedDate().replaceAll(",", ""));
      printWriter.println();
      printWriter.println(questionText);
      printWriter.println();
      printWriter.println("The following registrants answered Yes:");
      for (TmpBaseCsvRow yesRegistrant : yesRegistrants) {
         printWriter.println(" - " + yesRegistrant.getName() + " - " + yesRegistrant.getEmail());

      }

      printWriter.println();
      printWriter.println("Email list:");
      for (TmpBaseCsvRow yesRegistrant : yesRegistrants) {
         printWriter.println(yesRegistrant.getEmail());
      }

      printWriter.close();
   }

   // A concise contact list of all adult members.
   public static void generateConciseAdultMembershipCSV(List<? extends TmpBaseCsvRow> allRegistrantRows) throws IOException {

      String dataDir = allRegistrantRows.get(0).getDataDir();
      String filePath = dataDir + "AdultMemberList-" + allRegistrantRows.get(0).simple_getYearString() + ".csv";

      FileWriter fileWriter = new FileWriter(filePath);
      PrintWriter printWriter = new PrintWriter(fileWriter);

      printWriter.println("Updated: " + getFormttedDate().replaceAll(",", ""));
      printWriter.println("Cart #,Name,Age Group,Email,Mooring Type,Club Boat");

      for (TmpBaseCsvRow registrant : getAdultMemberRows(allRegistrantRows)) {

         printWriter.println(registrant.getCartNumber() + "," + registrant.getName() + ","
               + registrant.getAgeGroup() + "," + registrant.getEmail() + "," + registrant.getMooringType() + ","
               + (registrant.hasClubBoatAccess() ? "Yes" : "No"));
      }

      printWriter.close();
   }

   public static String getFormttedDate() {
      ZonedDateTime now = ZonedDateTime.now();
      return now.format(DateTimeFormatter.RFC_1123_DATE_TIME);
   }

   public static void printYouthMemberAgeGroupTotals(List<? extends TmpBaseCsvRow> allRegistrantRows, PrintStream out) {

      out.println("Youth member age group totals (people):");
      for (String group : allRegistrantRows.get(0).getYouthAgeGroups()) {
         out.println("  " + group + ": "
               + getAllRowsMatchingSpecifiedPredicate(allRegistrantRows, row -> row.getAgeGroup().equals(group))
                     .size());
      }
      out.println("");
   }

   public static void printStatistics(List<? extends TmpBaseCsvRow> allRegistrantRows, PrintStream out, int year) throws IOException {

      out.println("Updated: " + getFormttedDate());
      out.println("");

      // Carts
      out.println("Total Registrations: " + getOneRowForEachCart(allRegistrantRows).size());
      out.println("   Full: " + getOneRowForEachFullMembershipCart(allRegistrantRows).size());
      out.println("   Crew: " + getOneRowForEachCrewMembershipCart(allRegistrantRows).size());
      out.println("   Young Adult: " + getOneRowForEachYoungAdultMembershipCart(allRegistrantRows).size());
      out.println("   Alumni: " + getOneRowForEachAlumniMembershipCart(allRegistrantRows).size());
      out.println();
      out.println("   Total (Full+YA) Registrations with Club Boat access: "
            + getOneRowForEachClubBoatAccessMembershipCart(allRegistrantRows).size());
      out.println();

      List<Registration> didNotReturnRegistrations = Delta.getRegistrationsWhoDidNotReturn(year);
      out.println("Number of registrations from last season that have not (yet) registered for this season: " + didNotReturnRegistrations.size());
      out.println();

      // People
      out.println("People: ");
      out.println();
      out.println("   Total members: " + allRegistrantRows.size());
      out.println("      Youth: " + getYouthMemberRows(allRegistrantRows).size());
      out.println("      Adult: " + getAdultMemberRows(allRegistrantRows).size());
      out.println();

      // Club Boat Access
      out.println("   Total (Full+YA) Members with Club Boat access: "
            + getAllMembersWithClubBoatAccessRows(allRegistrantRows).size());
      out.println("   Total New (Full+YA) Members with Club Boat access: "
            + getNewMembersWithClubBoatAccessRows(allRegistrantRows).size());
      out.println();

      // New members
      out.println("   Total new members: " + Util.getNewMemberRows(allRegistrantRows).size());
      out.println("      Full: " + Util.getNewFullMemberRows(allRegistrantRows).size());
      out.println("      Crew: " + Util.getNewCrewMemberRows(allRegistrantRows).size());
      out.println("      Young Adult: " + Util.getNewYoungAdultMemberRows(allRegistrantRows).size());
      out.println();
      
      // Boats (not accurate before the 2024 season)
      if (year >= 2024) {
         out.println("Boats:");
         out.println("   Catamarans: " + Util.getMooringTypeCatRows(allRegistrantRows).size());
         out.println("   Monohulls (dinghy): " + Util.getMooringTypeMonohullDinghyRows(allRegistrantRows).size());
         out.println("   Keelboats: " + Util.getMooringTypeKeelboatRows(allRegistrantRows).size());
         out.println("   Kayak/Canoe/SUP: " + Util.getMooringTypeKCSRows(allRegistrantRows).size());
         out.println();
      }
   }
   
   public static void printFileToStdout(String filePath) throws IOException {
      printFileToStdout(Paths.get(filePath));
   }
   
   public static void printFileToStdout(Path filePath) throws IOException {
       List<String> lines = Files.readAllLines(filePath);
       for (String line : lines) {
           System.out.println(line);
       }
   }

   public static void printMembersWhoJoinedBefore2021(List<? extends TmpBaseCsvRow> allRegistrantRows,
         PrintStream out) {

      System.out.println();
      System.out.println("Cart number,Name,Email,Age Group,Year Joined");
      for (TmpBaseCsvRow registrant : allRegistrantRows) {

         if (registrant.getYearJoined().compareTo("2021") < 0 && !registrant.getYearJoined().equals("")
               && registrant.isAdult()) {
            System.out
                  .println(registrant.getCartNumber() + "," + registrant.getFullName() + "," + registrant.getEmail()
                        + "," + registrant.getAgeGroup() + "," + registrant.getYearJoined());
         }
      }
      System.out.println();
   }
   
}
