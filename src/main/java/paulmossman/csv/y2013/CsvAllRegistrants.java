package paulmossman.csv.y2013;

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

// TODO - why both???
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

   @CsvBindByPosition(position = 16)
   private String registrationDate;

   @CsvBindByPosition(position = 3)
   private String membershipPrivilege;

   @CsvBindByPosition(position = 33)
   private String ageGroup;

}