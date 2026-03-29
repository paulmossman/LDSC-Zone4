package paulmossman.csv.y2022;

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

   @CsvBindByPosition(position = 0)
   private String cartNumber;

   @CsvBindByPosition(position = 1)
   private String registrationDate;

   @CsvBindByPosition(position = 2)
   private String membershipPrivilege;

   @CsvBindByPosition(position = 4)
   private String firstHalfInitiationPayment;

   @CsvBindByPosition(position = 8)
   private String clubBoatAccessYA;

   @CsvBindByPosition(position = 10)
   private String clubBoatAccessFull;

   @CsvBindByPosition(position = 12)
   private String rawAdditionalBoatsSail;

   @CsvBindByPosition(position = 14)
   private String newCrewLateFeeExemption;

   @CsvBindByPosition(position = 16)
   private String newYoungAdultLateFeeExemption;

   @CsvBindByPosition(position = 18)
   private String lateFee;

   @CsvBindByPosition(position = 21)
   private String name;

   @CsvBindByPosition(position = 22)
   private String firstName;

   @CsvBindByPosition(position = 24)
   private String lastName;

   @CsvBindByPosition(position = 310)
   private String homePhone;

   @CsvBindByPosition(position = 32)
   private String email;

   @CsvBindByPosition(position = 35)
   private String ageGroup;

   @CsvBindByPosition(position = 36)
   private String yearJoined;

   @CsvBindByPosition(position = 37)
   private String pcoc;

   @CsvBindByPosition(position = 44)
   private String mooringType;

   @CsvBindByPosition(position = 45)
   private String boatType;

   @CsvBindByPosition(position = 46)
   private String boatTypeOther;

   @CsvBindByPosition(position = 49)
   private String stickerNumber;

   @CsvBindByPosition(position = 54)
   private String rawQuestionAdultSailTraining;

   @CsvBindByPosition(position = 55)
   private String rawQuestionAdultSailRacing;

   @CsvBindByPosition(position = 56)
   private String rawQuestionYouthSailTraining;

   @CsvBindByPosition(position = 57)
   private String rawQuestionJuniorSailRacing;

   @CsvBindByPosition(position = 58)
   private String rawQuestionRacingThisYear;

   @CsvBindByPosition(position = 59)
   private String rawQuestionServeBoardOfDirectors;

   @CsvBindByPosition(position = 60)
   private String rawQuestionMentorNewerSailor;

   @CsvBindByPosition(position = 61)
   private String rawQuestionCarpentryOrBuildingMaintenanceSkill;

   @CsvBindByPosition(position = 62)
   private String rawQuestionBoatRepairExperience;

   @CsvBindByPosition(position = 63)
   private String rawQuestionRunOnWaterEvent;

   @CsvBindByPosition(position = 64)
   private String rawQuestionRunSocialEvent;

   // PWM-remove this
   private static final String CSV_FILE_PATH = "./data/2022/All Registrants - 2022 Membership Registration Form.csv";

   @Override
   public List<TmpBaseCsvRow> loadFromCsv() throws IOException {
      return loadCsvDataAsBase(CsvAllRegistrants.class);
   }

   public static List<CsvAllRegistrants> getAdultMemberRows(List<CsvAllRegistrants> CSVRows) {

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

      try (Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH));) {
         // @formatter:off
         CsvToBean<CsvAllRegistrants> csvToBean = new CsvToBeanBuilder<CsvAllRegistrants>(reader)
               .withType(CsvAllRegistrants.class)
               .withSkipLines(1)
               .withIgnoreLeadingWhiteSpace(true)
               .build();

          // @formatter:on

         List<CsvAllRegistrants> allRegistrantRows = csvToBean.parse();

         // @formatter:on

         return allRegistrantRows;
      }
   }

   public TmpBaseCsvRow objectGet() {
      return CsvAllRegistrants.builder().build();
   }

   protected String getAllRegistrantsFilenameAfterYear() {
      return " Membership Registration Form.csv";
   }

}

