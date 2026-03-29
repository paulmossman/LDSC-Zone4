package paulmossman.csv.y2020;

import java.io.IOException;
import java.util.List;

import paulmossman.csv.base.CsvRow2000to2024;
import paulmossman.csv.base.TmpBaseCsvRow;

import com.opencsv.bean.CsvBindByPosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CsvAllRegistrants extends CsvRow2000to2024 {

   public CsvAllRegistrants() {
      this.year = 2021;
   }

   public static CsvAllRegistrants get() {
      return CsvAllRegistrants.builder().build();
   }

// PWM - why both???
   public TmpBaseCsvRow objectGet() {
      return CsvAllRegistrants.builder().build();
   }

   @Override
   public List<TmpBaseCsvRow> loadFromCsv() throws IOException {
      return loadCsvDataAsBase(CsvAllRegistrants.class);
   }

   protected String getAllRegistrantsFilenameAfterYear() {
      return " Membership Registration Form.csv";
   }

   @CsvBindByPosition(position = 0)
   private String cartNumber;

   @CsvBindByPosition(position = 19)
   private String registrationDate;

   @CsvBindByPosition(position = 3)
   private String membershipPrivilege;

   @CsvBindByPosition(position = 5)
   private String firstHalfInitiationPayment;

   @CsvBindByPosition(position = 9)
   private String clubBoatAccessFull;

   public String getClubBoatAccessYA() {
      return NOT_IMPLEMENTED;
   }

   @CsvBindByPosition(position = 11)
   private String rawAdditionalBoatsSail;

   @CsvBindByPosition(position = 13)
   private String lateFee;

   public String getNewCrewLateFeeExemption() {
      return NOT_IMPLEMENTED;
   }

   public String getNewYoungAdultLateFeeExemption() {
      return NOT_IMPLEMENTED;
   }

   @CsvBindByPosition(position = 20)
   private String name;

   @CsvBindByPosition(position = 22)
   private String firstName;

   @CsvBindByPosition(position = 24)
   private String lastName;

   @CsvBindByPosition(position = 30)
   private String homePhone;

   @CsvBindByPosition(position = 32)
   private String email;

   @CsvBindByPosition(position = 34)
   private String ageGroup;

   @CsvBindByPosition(position = 36)
   private String yearJoined;

   @CsvBindByPosition(position = 37)
   private String pcoc;

   @CsvBindByPosition(position = 49)
   private String mooringType;

   public String getBoatType() {
      return NOT_IMPLEMENTED;
   }

   public String getBoatTypeOther() {
      return NOT_IMPLEMENTED;
   }

   @CsvBindByPosition(position = 48)
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
}
