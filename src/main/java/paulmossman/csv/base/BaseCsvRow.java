package paulmossman.csv.base;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import paulmossman.Util;
import paulmossman.csv.base.mooringtypes.MooringType2000to2023;
import paulmossman.csv.base.mooringtypes.MooringType2024toFuture;
import paulmossman.csv.base.mooringtypes.MooringTypeBase;

public abstract class BaseCsvRow {

   protected int year = -1;

   final public String simple_getYearString() {
      return String.format("%d", getYear());
   }

   final public int getYear() {

      if (year == -1) {
         String fullPackage = this.getClass().getPackage().getName();
         int lastDot = fullPackage.lastIndexOf('.');
         String lastComponent = (lastDot != -1) ? fullPackage.substring(lastDot + 1) : fullPackage;

         year = Integer.parseInt(lastComponent.substring(1)); // drop first character
      }

      return year;
   }

   protected final MooringTypeBase get_MOORING_TYPE_base() {
      if (getYear() <= 2023) {
         return new MooringType2000to2023();
      }
      return new MooringType2024toFuture();
   }

   protected final String ZONE4_BOOLEAN_STRING_YES() {

      if (getYear() >= 2024) {
         // NOTE: Starting in 2024 checkboxes are now "1" for TRUE and "" for FALSE
         return "1";
      }
      else if (getYear() <= 2021) {
         return "TRUE";
      }
      return "✓";
      // TODO: Remove all "✓" from codebase and replace with this method.
   }

   public String getDataDir() throws IOException {
      return Util.getZone4DataDir() + "/" + simple_getYearString() + "/";
   }

   public String getAllRegistrantsFilePath() throws IOException {
      return getDataDir() + "All Registrants - " + simple_getYearString() + getAllRegistrantsFilenameAfterYear();
   }

   // Since 2024 this. For 2023 and earlier, see the concrete class implementation.
   protected String getAllRegistrantsFilenameAfterYear() {
      return " Membership Registration.csv";
   }

   public Reader getAllRegistrantsFileReader() throws IOException {

      String pwm = getAllRegistrantsFilePath();

      return Files.newBufferedReader(Paths.get(getAllRegistrantsFilePath()));
   }

   public String getSanityTestDataDir() throws IOException {
      // PWM - use data dir here too.....
      return Util.getZone4DataDir() + "/sanity-test-" + simple_getYearString() + "-copy/";
   }

   // EVERYTHING BELOW HERE IS FOR MainOld.java COMPATIBILITY, EVENTUALLY DELETE.
   public static String getYearString(BaseCsvRow tmpRow) {
      return tmpRow.simple_getYearString();
   }
}
