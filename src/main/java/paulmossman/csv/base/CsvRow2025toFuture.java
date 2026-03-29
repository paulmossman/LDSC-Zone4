package paulmossman.csv.base;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class CsvRow2025toFuture extends TmpBaseCsvRow {

   public Set<String> getYouthAgeGroups() {
      return new HashSet<String>(Arrays.asList("under 8", "8-14", "15-17", "Prefer not to say but under 18"));
   }

   public Set<String> getAdultAgeGroups() {
      return new HashSet<String>(
            Arrays.asList("18-30", "31-39", "40-49", "50-59", "60-69", "over 69", "Prefer not to say but 18 or older"));
   }
}
