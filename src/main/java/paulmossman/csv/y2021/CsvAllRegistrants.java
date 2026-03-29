package paulmossman.csv.y2021;

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

   @CsvBindByPosition(position = 17)
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

   @CsvBindByPosition(position = 18)
   private String name;

   @CsvBindByPosition(position = 20)
   private String firstName;

   @CsvBindByPosition(position = 22)
   private String lastName;

   @CsvBindByPosition(position = 28)
   private String homePhone;

   @CsvBindByPosition(position = 30)
   private String email;

   @CsvBindByPosition(position = 33)
   private String ageGroup;

   @CsvBindByPosition(position = 34)
   private String yearJoined;

   @CsvBindByPosition(position = 35)
   private String pcoc;

   @CsvBindByPosition(position = 47)
   private String mooringType;

   public String getBoatType() {
      return NOT_IMPLEMENTED;
   }

   public String getBoatTypeOther() {
      return NOT_IMPLEMENTED;
   }

   @CsvBindByPosition(position = 46)
   private String stickerNumber;

   @CsvBindByPosition(position = 52)
   private String rawQuestionAdultSailTraining;

   @CsvBindByPosition(position = 53)
   private String rawQuestionAdultSailRacing;

   @CsvBindByPosition(position = 54)
   private String rawQuestionYouthSailTraining;

   @CsvBindByPosition(position = 55)
   private String rawQuestionJuniorSailRacing;

   @CsvBindByPosition(position = 56)
   private String rawQuestionRacingThisYear;

   @CsvBindByPosition(position = 57)
   private String rawQuestionServeBoardOfDirectors;

   @CsvBindByPosition(position = 58)
   private String rawQuestionMentorNewerSailor;

   @CsvBindByPosition(position = 59)
   private String rawQuestionCarpentryOrBuildingMaintenanceSkill;

   @CsvBindByPosition(position = 60)
   private String rawQuestionBoatRepairExperience;

   @CsvBindByPosition(position = 61)
   private String rawQuestionRunOnWaterEvent;

   @CsvBindByPosition(position = 62)
   private String rawQuestionRunSocialEvent;
}
