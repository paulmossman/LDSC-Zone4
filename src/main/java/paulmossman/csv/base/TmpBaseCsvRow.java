package paulmossman.csv.base;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Set;

import paulmossman.csv.base.mooringtypes.MooringTypeBase;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public abstract class TmpBaseCsvRow extends BaseCsvRow {

   // PWM TODO: Eventually move all this to BaseCsvRow

   public static final String NOT_IMPLEMENTED = "Not implemented";
   
   public abstract String getMooringType();

   public boolean isMooringTypeSail() {
      return isMooringTypeKeelboat() || isMooringTypeMonohullDinghy() || isMooringTypeCat();
   }

   public boolean isMooringTypeKeelboat() {
      return getMooringType().equals(get_MOORING_TYPE_base().MOORING_TYPE_KEELBOAT());
   }

   public boolean isMooringTypeMonohullDinghy() {
      return getMooringType().equals(get_MOORING_TYPE_base().MOORING_TYPE_MONOHULL_DINGY());
   }

   public boolean isMooringTypeCat() {
      return getMooringType().equals(get_MOORING_TYPE_base().MOORING_TYPE_CAT());
   }

   public boolean isMooringTypeKCS() {
      return getMooringType().equals(get_MOORING_TYPE_base().MOORING_TYPE_KCS());
   }

   public boolean isMooringTypeNoBoat() {
      return getMooringType().equals(get_MOORING_TYPE_base().MOORING_TYPE_NO_BOAT());
   }

   public boolean isMooringTypeClubBoatOnly() {
      return getMooringType().equals(get_MOORING_TYPE_base().MOORING_TYPE_CLUB_BOAT_ONLY());
   }

   private MooringTypeBase MOORING_TYPES = null;

   private MooringTypeBase singleton_MOORING_TYPES() {
      if (MOORING_TYPES == null) {
         MOORING_TYPES = get_MOORING_TYPE_base();
      }
      return MOORING_TYPES;
   }

   public String MOORING_TYPE_KEELBOAT() {
      return singleton_MOORING_TYPES().MOORING_TYPE_KEELBOAT();
   }

   public String MOORING_TYPE_MONOHULL_DINGY() {
      return singleton_MOORING_TYPES().MOORING_TYPE_MONOHULL_DINGY();
   }

   public String MOORING_TYPE_CAT() {
      return singleton_MOORING_TYPES().MOORING_TYPE_CAT();
   }

   // Note: This Mooring Type option was added in 2023/2024???
   public String MOORING_TYPE_KCS() {
      return singleton_MOORING_TYPES().MOORING_TYPE_KCS();
   }

   public String MOORING_TYPE_NO_BOAT() {
      return singleton_MOORING_TYPES().MOORING_TYPE_NO_BOAT();
   }

   // Note: This Mooring Type option was removed in 2025.
   public String MOORING_TYPE_CLUB_BOAT_ONLY() {
      return singleton_MOORING_TYPES().MOORING_TYPE_CLUB_BOAT_ONLY();
   }

   public String toString() {
      return getCartNumber() + " - " + getEmail();
   }

   public boolean isFullPriviledge() {
      String fullString = "Full";
      if (getYear() <= 2021) {
         fullString = "Family";
      }
      return getMembershipPrivilege().equals(fullString);
   }

   public boolean isCrewPriviledge() {
      return getMembershipPrivilege().equals("Crew");
   }

   public boolean isYoungAdultPriviledge() {
      String youngString = "Young Adult (18-30)";
      if (getYear() <= 2021) {
         youngString = "Young (18-30)";
      }
      return getMembershipPrivilege().equals(youngString);
   }

   public boolean isAlumniPriviledge() {
      String alumniString = "Alumni";
      if (getYear() <= 2021) {
         alumniString = "Alumni/Social";
      }
      return getMembershipPrivilege().equals(alumniString);
   }

   public int getAdditionalBoatsSail() {
      if (getRawAdditionalBoatsSail().isEmpty()) {
         return 0;
      }
      // Note: Fragile... Assumes # of boats is a single character.
      return Integer.parseInt(getRawAdditionalBoatsSail().substring(0, 1));
   }

   public abstract String getYearJoined();

   public int getAdditionalBoatsCKSup() {
      if (getRawAdditionalBoatsCKSup().isEmpty()) {
         return 0;
      }
      // Note: Fragile... Assumes # of boats is a single character.
      return Integer.parseInt(getRawAdditionalBoatsCKSup().substring(0, 1));
   }

   // BEGIN: The CSV column values.
   public abstract String getCartNumber();

   public abstract String getEmail();

   public abstract String getName();

   public abstract String getFirstName();

   public abstract String getLastName();

   public abstract String getFirstHalfInitiationPayment();

   public abstract String getAgeGroup();

   public abstract String getMembershipPrivilege();

   public abstract String getClubBoatAccessYA();

   public abstract String getClubBoatAccessFull();

   public abstract String getLateFee();

   public abstract String getHomePhone();

   public abstract String getNewCrewLateFeeExemption();

   public abstract String getNewYoungAdultLateFeeExemption();

   public abstract String getRawAdditionalBoatsSail();

   public abstract String getRawAdditionalBoatsCKSup();

   public abstract String getBoatType();

   public abstract String getBoatTypeOther();

   public abstract String getStickerNumber();

   public abstract String getRawQuestionAdultSailTraining();
   
   public abstract String getRawQuestionAdultSailRacing();

   public abstract String getRawQuestionYouthSailTraining();

   public abstract String getRawQuestionJuniorSailRacing();

   public abstract String getRawQuestionRacingThisYear();

   public abstract String getRawQuestionServeBoardOfDirectors();

   public abstract String getRawQuestionMentorNewerSailor();

   public abstract String getRawQuestionCarpentryOrBuildingMaintenanceSkill();

   public abstract String getRawQuestionBoatRepairExperience();

   public abstract String getRawQuestionRunOnWaterEvent();

   public abstract String getRawQuestionRunSocialEvent();
   
   // END: The CSV column values.

   public boolean questionAdultSailTraining() {
      return isZone4BooleanTrue(getRawQuestionAdultSailTraining());
   }

   public boolean questionAdultSailRacing() {
      return isZone4BooleanTrue(getRawQuestionAdultSailRacing());
   }

   public boolean questionYouthSailTraining() {
      return isZone4BooleanTrue(getRawQuestionYouthSailTraining());
   }

   public boolean questionJuniorSailRacing() {
      return isZone4BooleanTrue(getRawQuestionJuniorSailRacing());
   }

   public boolean questionRacingThisYear() {
      return isZone4BooleanTrue(getRawQuestionRacingThisYear());
   }

   public boolean questionServeBoardOfDirectors() {
      return isZone4BooleanTrue(getRawQuestionServeBoardOfDirectors());
   }

   public boolean questionMentorNewerSailor() {
      return isZone4BooleanTrue(getRawQuestionMentorNewerSailor());
   }

   public boolean questionCarpentryOrBuildingMaintenanceSkill() {
      return isZone4BooleanTrue(getRawQuestionCarpentryOrBuildingMaintenanceSkill());
   }

   public boolean questionBoatRepairExperience() {
      return isZone4BooleanTrue(getRawQuestionBoatRepairExperience());
   }

   public boolean questionRunOnWaterEvent() {
      return isZone4BooleanTrue(getRawQuestionRunOnWaterEvent());
   }

   public boolean questionRunSocialEvent() {
      return isZone4BooleanTrue(getRawQuestionRunSocialEvent());
   }

   // Returns -1 if not a valid integer.
   public int getStickerNumberAsInteger() {
      try {
         return Integer.parseInt(getStickerNumber());
      } catch (NumberFormatException e) {
         return -1;
      }
   }

   public boolean isYouth() {
      return getYouthAgeGroups().contains(getAgeGroup());
   }

   public boolean isAdult() {
      return getAdultAgeGroups().contains(getAgeGroup());
   }

   public boolean hasClubBoatAccess() {

      return isZone4BooleanTrue(getClubBoatAccessYA()) || isZone4BooleanTrue(getClubBoatAccessFull());
   }

   public String getFullName() {
      return getFirstName() + " " + getLastName();
   }

   public boolean isNewFullMember() {
      // Some new Young Adult members choose to pay the Initiation Fee,
      // even though they don't have to yet.  Exclude those.
      return isFullPriviledge() && isFirstHalfInitiationPayment();
   }

   public boolean isLateFee() {
      return isZone4BooleanTrue(getLateFee());
   }

   public boolean isNewCrewLateFeeExemption() {
      return isZone4BooleanTrue(getNewCrewLateFeeExemption());
   }

   public boolean isNewCrewMember() {

      if (!isCrewPriviledge()) {
         return false;
      }

      // New Crew members could choose to pay the Initiation Fee,
      // even though they don't have to yet.
      return isNewCrewLateFeeExemption() || isFirstHalfInitiationPayment();
   }

   public boolean isNewYoungAdultLateFeeExemption() {
      return isZone4BooleanTrue(getNewYoungAdultLateFeeExemption());
   }

   public boolean isNewYoungAdultMember() {

      if(! isYoungAdultPriviledge()) {
         return false;
      }

      // New Young Adult members could choose to pay the Initiation Fee,
      // even though they don't have to yet.
      return isNewYoungAdultLateFeeExemption() || isFirstHalfInitiationPayment();
   }

   public boolean isFirstHalfInitiationPayment() {
      return isZone4BooleanTrue(getFirstHalfInitiationPayment());
   }

   // Any eligible type (Full, Young Adult, or Crew)
   public boolean isNewMember() {
      return isNewFullMember() || isNewCrewMember() || isNewYoungAdultMember();
   }

   public boolean isZone4BooleanTrue(String string) {

      return ZONE4_BOOLEAN_STRING_YES().equalsIgnoreCase(string);
   }

   public abstract Set<String> getYouthAgeGroups();

   public abstract Set<String> getAdultAgeGroups();

   /**
    * Generic method to load CSV data for subclasses of TmpBaseCsvRow.
    *
    * @param <T>   the type of the subclass
    * @param clazz the class of the subclass
    * @return a list of parsed objects
    * @throws IOException if there's an issue reading the file
    */
   protected <T extends TmpBaseCsvRow> List<T> loadCsvData(Class<T> clazz) throws IOException {
      try (Reader reader = getAllRegistrantsFileReader()) {
         CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader).withType(clazz).withSkipLines(1)
               .withIgnoreLeadingWhiteSpace(true).build();
         return csvToBean.parse();
      }
   }

   @SuppressWarnings("unchecked")
   protected <T extends TmpBaseCsvRow> List<TmpBaseCsvRow> loadCsvDataAsBase(Class<T> clazz) throws IOException {
      List<T> result = loadCsvData(clazz); // Safe due to generics
      return (List<TmpBaseCsvRow>) (List<?>) result; // Cast via wildcard
   }
   public abstract List<TmpBaseCsvRow> loadFromCsv() throws IOException;

   public abstract TmpBaseCsvRow objectGet();

   public static TmpBaseCsvRow get() {
      // PWM fix .....
      // String pwm =
     
      return null; // CsvAllRegistrants.builder().build();
   }
}

