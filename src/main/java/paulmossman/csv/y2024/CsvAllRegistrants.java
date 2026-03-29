package paulmossman.csv.y2024;

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

   public static CsvAllRegistrants get() {
      return CsvAllRegistrants.builder().build();
   }

   @CsvBindByPosition(position = 0)
   private String cartNumber;

   @CsvBindByPosition(position = 3)
   private String membershipPrivilege;

   // NOTE: Starting in 2024 checkboxes are now "1" for TRUE and "" for FALSE. (Was
   // "✓" for TRUE.) PWM-2025 pwm-Jan2

   // TODO: Map checkboxes to BOOLEAN ?

   // TODO: Matching functions for membership types (instead of many string
   // compares...)

   @CsvBindByPosition(position = 6)
   private String firstHalfInitiationPayment;

   @CsvBindByPosition(position = 12)
   private String clubBoatAccessYA;

   @CsvBindByPosition(position = 14)
   private String clubBoatAccessFull;

   @CsvBindByPosition(position = 16)
   private String rawAdditionalBoatsSail;

   // PWM-2025
   public int getAdditionalBoatsSail() {
      if (rawAdditionalBoatsSail.isEmpty()) {
         return 0;
      }
      // Note: Fragile... Assumes # of boats is a single character.
      return Integer.parseInt(rawAdditionalBoatsSail.substring(0, 1));
   }

   @CsvBindByPosition(position = 18)
   private String rawAdditionalBoatsCKSup;

   // PWM-2025
   public int getAdditionalBoatsCKSup() {
      if (rawAdditionalBoatsCKSup.isEmpty()) {
         return 0;
      }
      // Note: Fragile... Assumes # of boats is a single character.
      return Integer.parseInt(rawAdditionalBoatsCKSup.substring(0, 1));
   }

   // PWM-2025
   public boolean hasClubBoatAccess() {
      if (clubBoatAccessFull.equals(ZONE4_BOOLEAN_STRING_YES) || clubBoatAccessYA.equals(ZONE4_BOOLEAN_STRING_YES)) {
         return true;
      }
      return false;
   }

   @CsvBindByPosition(position = 20)
   private String newCrewLateFeeExemption;

   @CsvBindByPosition(position = 22)
   private String newYoungAdultLateFeeExemption;

   @CsvBindByPosition(position = 24)
   private String lateFee;

   @CsvBindByPosition(position = 28)
   private String registrationDate;

   @CsvBindByPosition(position = 29)
   private String name;

   @CsvBindByPosition(position = 32)
   private String firstName;

   @CsvBindByPosition(position = 34)
   private String lastName;

   @CsvBindByPosition(position = 40)
   private String homePhone;

   @CsvBindByPosition(position = 42)
   private String email;

   @CsvBindByPosition(position = 43)
   private String ageGroup;

   @CsvBindByPosition(position = 45)
   private String pcoc;

   public boolean hasPcoc() {
      return pcoc.equals(ZONE4_BOOLEAN_STRING_YES);
   }

   // PWM-2025
   public static final String ZONE4_BOOLEAN_STRING_YES = "1";

   // PWM-2025
   @CsvBindByPosition(position = 53)
   private String mooringType;

   // PWM-2025 TODO Delete
   public static final String KEELBOAT_MOORING_TYPE = "Keelboat (additional equipment costs may apply)";
   public static final String MONOHULL_DINGY_MOORING_TYPE = "Monohull";
   public static final String CAT_MOORING_TYPE = "CAT";
   public static final String KCS_MOORING_TYPE = "Kayak/Canoe/SUP";
   public static final String NO_BOAT_MOORING_TYPE = "No Boat";
   // Note: this option was removed in 2025
   public static final String CLUB_BOAT_ONLY_MOORING_TYPE = "Club Boat Only";

   @CsvBindByPosition(position = 54)
   private String boatType;

   @CsvBindByPosition(position = 55)
   private String boatTypeOther;

   @CsvBindByPosition(position = 58)
   private String stickerNumber;

   @CsvBindByPosition(position = 59)
   private String rawQuestionAdultSailTraining;

   @CsvBindByPosition(position = 60)
   private String rawQuestionAdultSailRacing;

   @CsvBindByPosition(position = 61)
   private String rawQuestionYouthSailTraining;

   @CsvBindByPosition(position = 62)
   private String rawQuestionJuniorSailRacing;

   @CsvBindByPosition(position = 63)
   private String rawQuestionRacingThisYear;

   @CsvBindByPosition(position = 64)
   private String rawQuestionServeBoardOfDirectors;

   @CsvBindByPosition(position = 65)
   private String rawQuestionMentorNewerSailor;

   @CsvBindByPosition(position = 66)
   private String rawQuestionCarpentryOrBuildingMaintenanceSkill;

   @CsvBindByPosition(position = 67)
   private String rawQuestionBoatRepairExperience;

   @CsvBindByPosition(position = 68)
   private String rawQuestionRunOnWaterEvent;

   @CsvBindByPosition(position = 69)
   private String rawQuestionRunSocialEvent;

   public static List<CsvAllRegistrants> getAdultMemberRows(List<CsvAllRegistrants> CSVRows) {
// PWM-2025

      // @formatter:off
         return CSVRows.stream().filter(row -> {
            String ageGroup = row.getAgeGroup();
   
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

   // PWM-2025
   public static List<CsvAllRegistrants> getYouthMemberRows(List<CsvAllRegistrants> CSVRows) {

      // PWM-active --> change this to "IS", in the Abstract class (2 since 2024 and
      // 2025 differ..). PLUS
      // Then ONE generic getYouthMemberRows, wich uses the SUbclasses "IS"

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

   // PWM-2025 - can this be moved to main????
   public static List<CsvAllRegistrants> getNewMemberRows(List<CsvAllRegistrants> CSVRows) {

      // @formatter:off
      return Stream.concat(
            getNewFullMemberRows(CSVRows).stream(),
            Stream.concat(
                  CSVRows.stream().filter(row -> row.getNewCrewLateFeeExemption().equals("1")),
                  CSVRows.stream().filter(row -> row.getNewYoungAdultLateFeeExemption().equals("1"))
            )
      ).collect(Collectors.toList());
      // @formatter:on
   }

   // PWM-2025 - can this be moved to main????
   public static List<CsvAllRegistrants> getNewFullMemberRows(List<CsvAllRegistrants> CSVRows) {

      // @formatter:off
      return CSVRows.stream().filter(row -> row.getFirstHalfInitiationPayment().equals("1")).collect(Collectors.toList());
      // @formatter:on
   }

   // PWM-2025 DONE DELETE
   public static List<CsvAllRegistrants> getAllMemberWithClubBoatAccessRows(List<CsvAllRegistrants> CSVRows) {

      Stream<CsvAllRegistrants> full = CSVRows.stream().filter(row -> row.getClubBoatAccessFull().equals("1"));

      Stream<CsvAllRegistrants> ya = CSVRows.stream().filter(row -> row.getClubBoatAccessYA().equals("1"));

      return Stream.concat(full, ya).collect(Collectors.toList());
   }

   // PWM-2025 DONE DELETE
   public static List<CsvAllRegistrants> getNewFullMemberWithClubBoatAccessRows(List<CsvAllRegistrants> CSVRows) {

      return getNewFullMemberRows(CSVRows).stream().filter(row -> row.getClubBoatAccessFull().equals("1"))
            .collect(Collectors.toList());
   }

   public static List<CsvAllRegistrants> getKeelboatRows(List<CsvAllRegistrants> CSVRows) {

      return CSVRows.stream().filter(row -> row.getMooringType().equals(KEELBOAT_MOORING_TYPE))
            .collect(Collectors.toList());
   }

   // PWM-2025
   public String toString() {
      StringBuilder result = new StringBuilder();
      String newLine = System.getProperty("line.separator");

      result.append(this.getClass().getName());
      result.append(" Object {");
      result.append(newLine);

      // determine fields declared in this class only (no fields of superclass)
      java.lang.reflect.Field[] fields = this.getClass().getDeclaredFields();

      // print field names paired with their values
      for (java.lang.reflect.Field field : fields) {
         result.append("  ");
         try {
            result.append(field.getName());
            result.append(": ");
            // requires access to private field:
            result.append(field.get(this));
         } catch (IllegalAccessException ex) {
            System.out.println(ex);
         }
         result.append(newLine);
      }
      result.append("}");

      return result.toString();
   }

   @Override
   public List<TmpBaseCsvRow> loadFromCsv() throws IOException {
      return loadCsvDataAsBase(CsvAllRegistrants.class);
   }

   // EVERYTHING BELOW HERE IS FOR MainOld.java COMPATIBILITY, EVENTUALLY DELETE.
   public static List<CsvAllRegistrants> static_loadFromCsv() throws IOException {
      String CSV_FILE_PATH = get().getDataDir() + "All Registrants - " + get().simple_getYearString()
            + " Membership Registration.csv";

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
