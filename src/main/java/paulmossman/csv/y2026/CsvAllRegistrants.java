package paulmossman.csv.y2026;

import java.io.IOException;
import java.util.List;

import paulmossman.csv.base.CsvRow2025toFuture;
import paulmossman.csv.base.TmpBaseCsvRow;

import com.opencsv.bean.CsvBindByPosition;

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
public class CsvAllRegistrants extends CsvRow2025toFuture {

   public static CsvAllRegistrants get() {
      return new CsvAllRegistrants();
      // PWM return CsvAllRegistrants.builder().build();
   }

   @CsvBindByPosition(position = 0)
   private String cartNumber;

   @CsvBindByPosition(position = 3)
   private String membershipPrivilege;

   @CsvBindByPosition(position = 15)
   private String homePhone;

   @CsvBindByPosition(position = 16)
   private String cellPhone;

   @CsvBindByPosition(position = 17)
   private String clubBoatAccessYA;

   @CsvBindByPosition(position = 19)
   private String clubBoatAccessFull;

   @CsvBindByPosition(position = 6)
   private String firstHalfInitiationPayment;

   @CsvBindByPosition(position = 21)
   private String rawAdditionalBoatsSail;

   @CsvBindByPosition(position = 23)
   private String rawAdditionalBoatsCKSup;

   @CsvBindByPosition(position = 25)
   private String newCrewLateFeeExemption;

   @CsvBindByPosition(position = 27)
   private String newYoungAdultLateFeeExemption;

   @CsvBindByPosition(position = 29)
   private String newFullLateFeeExemption;

   @CsvBindByPosition(position = 31)
   private String lateFee;

   @CsvBindByPosition(position = 36)
   private String name;

   @CsvBindByPosition(position = 38)
   private String firstName;

   @CsvBindByPosition(position = 40)
   private String lastName;

   @CsvBindByPosition(position = 41)
   private String email;

   @CsvBindByPosition(position = 42)
   private String ageGroup;

   @CsvBindByPosition(position = 43)
   private String yearJoined;

   @CsvBindByPosition(position = 51)
   private String mooringType;

   @CsvBindByPosition(position = 52)
   private String boatType;

   @CsvBindByPosition(position = 53)
   private String boatTypeOther;

   @CsvBindByPosition(position = 56)
   private String stickerNumber;
   
   @CsvBindByPosition(position = 57)
   private String rawQuestionAdultSailTraining;

   @CsvBindByPosition(position = 58)
   private String rawQuestionAdultSailRacing;

   @CsvBindByPosition(position = 59)
   private String rawQuestionYouthSailTraining;

   @CsvBindByPosition(position = 60)
   private String rawQuestionJuniorSailRacing;

   @CsvBindByPosition(position = 61)
   private String rawQuestionRacingThisYear;

   @CsvBindByPosition(position = 62)
   private String rawQuestionServeBoardOfDirectors;

   @CsvBindByPosition(position = 63)
   private String rawQuestionMentorNewerSailor;

   @CsvBindByPosition(position = 64)
   private String rawQuestionCarpentryOrBuildingMaintenanceSkill;

   @CsvBindByPosition(position = 65)
   private String rawQuestionBoatRepairExperience;

   @CsvBindByPosition(position = 66)
   private String rawQuestionRunOnWaterEvent;

   @CsvBindByPosition(position = 67)
   private String rawQuestionRunSocialEvent;

   @Override
   public List<TmpBaseCsvRow> loadFromCsv() throws IOException {
      return loadCsvDataAsBase(CsvAllRegistrants.class);
   }

   public TmpBaseCsvRow objectGet() {
      return CsvAllRegistrants.builder().build();
   }

}
