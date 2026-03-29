package paulmossman.csv.y2023;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import paulmossman.csv.base.CsvRow2000to2024;
import paulmossman.csv.base.TmpBaseCsvRow;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CsvAllRegistrants extends CsvRow2000to2024 {

   public enum MembershipPriviledge {
      // @formatter:off
      FULL("Full"),
      CREW("Crew"),
      YOUNG_ADULT("Young Adult (18-30)"),
      ALUMNI("Alumni"),
      ORR("Ottawa River Runners canoe training program");
      // @formatter:on

      private final String name;

      private MembershipPriviledge(String s) {
         name = s;
      }

      public String toString() {
         return this.name;
      }
   }

   @CsvBindByPosition(position = 0)
   private String cartNumber;

   @CsvBindByPosition(position = 1)
   private String registrationDate;

   @CsvBindByPosition(position = 2)
   private String membershipPrivilege;

   @CsvBindByPosition(position = 5)
   private String firstHalfInitiationPayment;

   @CsvBindByPosition(position = 9)
   private String clubBoatAccessYA;

   @CsvBindByPosition(position = 11)
   private String clubBoatAccessFull;

   @CsvBindByPosition(position = 13)
   private String rawAdditionalBoatsSail;

   // This option was not available before 2024.
   @Override
   public String getRawAdditionalBoatsCKSup() {
      return "0";
   }

   @CsvBindByPosition(position = 15)
   private String newCrewLateFeeExemption;

   @CsvBindByPosition(position = 17)
   private String newYoungAdultLateFeeExemption;

   @CsvBindByPosition(position = 19)
   private String lateFee;

   @CsvBindByPosition(position = 22)
   private String name;

   @CsvBindByPosition(position = 23)
   private String firstName;

   @CsvBindByPosition(position = 25)
   private String lastName;

   @CsvBindByPosition(position = 31)
   private String homePhone;

   @CsvBindByPosition(position = 33)
   private String email;

   @CsvBindByPosition(position = 36)
   private String ageGroup;

   @CsvBindByPosition(position = 37)
   private String pcoc;

   public boolean hasPcoc() {
      return pcoc.equals("✓"); // PWM - replace this! Zone4Boolean....
   }

   @CsvBindByPosition(position = 45)
   private String mooringType;
   // PWM TODO - put in date range ENUM
   public static final String KEELBOAT_MOORING_TYPE = "Keelboat";
   public static final String MONOHULL_DINGY_MOORING_TYPE = "Monohull";
   public static final String CAT_MOORING_TYPE = "CAT";
   public static final String KCS_MOORING_TYPE = "Kayak/Canoe/SUP";
   public static final String NO_BOAT_MOORING_TYPE = "No Boat";
   public static final String CLUB_BOAT_ONLY_MOORING_TYPE = "Club Boat Only";

   @CsvBindByPosition(position = 46)
   private String boatType;

   @CsvBindByPosition(position = 47)
   private String boatTypeOther;

   @CsvBindByPosition(position = 50)
   private String stickerNumber;

   @CsvBindByPosition(position = 51)
   private String rawQuestionAdultSailTraining;

   @CsvBindByPosition(position = 52)
   private String rawQuestionAdultSailRacing;

   @CsvBindByPosition(position = 53)
   private String rawQuestionYouthSailTraining;

   @CsvBindByPosition(position = 54)
   private String rawQuestionJuniorSailRacing;

   @CsvBindByPosition(position = 55)
   private String rawQuestionRacingThisYear;

   @CsvBindByPosition(position = 56)
   private String rawQuestionServeBoardOfDirectors;

   @CsvBindByPosition(position = 57)
   private String rawQuestionMentorNewerSailor;

   @CsvBindByPosition(position = 58)
   private String rawQuestionCarpentryOrBuildingMaintenanceSkill;

   @CsvBindByPosition(position = 59)
   private String rawQuestionBoatRepairExperience;

   @CsvBindByPosition(position = 60)
   private String rawQuestionRunOnWaterEvent;

   @CsvBindByPosition(position = 61)
   private String rawQuestionRunSocialEvent;

   public static List<CsvAllRegistrants> getAdultMemberRows(List<CsvAllRegistrants> CSVRows) {

      // @formatter:off
         return CSVRows.stream().filter(row -> {
            String ageGroup = row.getAgeGroup();
   
            // PWM - get from the Date Range enum
            if (ageGroup.equals("18-30"))
               return true;
   
            if (ageGroup.equals("31-39"))
               return true;
   
            if (ageGroup.equals("40-49"))
               return true;
   
            if (ageGroup.equals("50-59"))
               return true;
   
            if (ageGroup.equals("60-69"))
               return true;
   
            if (ageGroup.equals("70"))
               return true;
   
            if (ageGroup.equals("Prefer not to say but over 18"))
               return true;
   
            return false;
         }).collect(Collectors.toList());
   
         // @formatter:on
   }

   public static List<CsvAllRegistrants> getOrrMemberRows(List<CsvAllRegistrants> CSVRows) {

      // @formatter:off
      return CSVRows.stream()
            .filter(row -> row.getMembershipPrivilege().equals(MembershipPriviledge.ORR.toString()))
            .collect(Collectors.toList());
      // @formatter:on
   }

   public static List<CsvAllRegistrants> getMemberRowsExcludingOrr(List<CsvAllRegistrants> CSVRows) {

      // @formatter:off
         return CSVRows.stream()
               .filter(row -> !row.getMembershipPrivilege().equals(MembershipPriviledge.ORR.toString()))
               .collect(Collectors.toList());
         // @formatter:on
   }

   // TODO - remove....
   public static List<CsvAllRegistrants> getYouthMemberRows(List<CsvAllRegistrants> CSVRows) {

      // @formatter:off
         return CSVRows.stream().filter(row -> {
            String ageGroup = row.getAgeGroup();
   
            if (ageGroup.equals("< 8 years old"))
               return true;
   
            if (ageGroup.equals("9-14 years old"))
               return true;
   
            if (ageGroup.equals("15-18"))
               return true;
   
            if (ageGroup.equals("Prefer not to say but under 18"))
               return true;
   
            return false;
         }).collect(Collectors.toList());
   
         // @formatter:on
   }

   public static List<CsvAllRegistrants> getNewMemberRows(List<CsvAllRegistrants> CSVRows) {

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

   public static List<CsvAllRegistrants> getNewFullMemberRows(List<CsvAllRegistrants> CSVRows) {

      // @formatter:off
      return CSVRows.stream().filter(row -> row.getFirstHalfInitiationPayment().equals("✓")).collect(Collectors.toList());
      // @formatter:on
   }

   public static List<CsvAllRegistrants> getNewFullMemberWithClubBoatAccessRows(List<CsvAllRegistrants> CSVRows) {

      return getNewFullMemberRows(CSVRows).stream().filter(row -> row.getClubBoatAccessFull().equals("✓"))
            .collect(Collectors.toList());
   }

   public static List<CsvAllRegistrants> getKeelboatRows(List<CsvAllRegistrants> CSVRows) {

      // PWM - this might be a bug, majik value changed
      return CSVRows.stream().filter(row -> row.getMooringType().equals("Keelboat")).collect(Collectors.toList());
   }

   public static List<CsvAllRegistrants> getAllMemberWithClubBoatAccessRows(List<CsvAllRegistrants> CSVRows) {

      Stream<CsvAllRegistrants> full = CSVRows.stream().filter(row -> row.getClubBoatAccessFull().equals("✓"));

      Stream<CsvAllRegistrants> ya = CSVRows.stream().filter(row -> row.getClubBoatAccessYA().equals("✓"));

      return Stream.concat(full, ya).collect(Collectors.toList());
   }

   public static CsvAllRegistrants get() {
      return CsvAllRegistrants.builder().build();
   }


   // EVERYTHING BELOW HERE IS FOR MainOld.java COMPATIBILITY, EVENTUALLY DELETE.
   public static List<CsvAllRegistrants> static_loadFromCsv() throws IOException {
      String CSV_FILE_PATH = get().getDataDir() + "All Registrants - " + get().simple_getYearString()
            + " Membership Registration Form.csv";

      try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));) {
   // @formatter:off
   CsvToBean<CsvAllRegistrants> csvToBean = new CsvToBeanBuilder<CsvAllRegistrants>(reader)
         .withType(CsvAllRegistrants.class)
         .withSkipLines(1)
         .withIgnoreLeadingWhiteSpace(true)
         .build();
   // @formatter:on

         List<CsvAllRegistrants> allRegistrantRows = csvToBean.parse();

         return allRegistrantRows;
      }
   }

   protected String getAllRegistrantsFilenameAfterYear() {
      return " Membership Registration Form.csv";
   }

   @Override
   public List<TmpBaseCsvRow> loadFromCsv() throws IOException {
      return loadCsvDataAsBase(CsvAllRegistrants.class);
   }

   @Override
   public TmpBaseCsvRow objectGet() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getYearJoined() {
      return "PWM TODO yearJoined";
   }

}
