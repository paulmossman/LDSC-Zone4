package paulmossman.csv.base;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class CsvRow2000to2024 extends TmpBaseCsvRow {

   public Set<String> getYouthAgeGroups() {
      return new HashSet<String>(
            Arrays.asList("< 8 years old", "9-14 years old", "15-18", "Prefer not to say but under 18"));
   }

   public Set<String> getAdultAgeGroups() {
      return new HashSet<String>(
            Arrays.asList("18-30", "31-39", "40-49", "50-59", "60-69", "70", "Prefer not to say but over 18"));
   }

   public String getRawAdditionalBoatsCKSup() {
      // This was introduced in later years.
      return "0";
   }

}
