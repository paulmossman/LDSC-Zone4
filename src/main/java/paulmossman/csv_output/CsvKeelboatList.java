package paulmossman.csv_output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import paulmossman.Util;
import paulmossman.csv.base.TmpBaseCsvRow;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

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
public class CsvKeelboatList extends BaseDoneTracker<CsvKeelboatList> {

   @CsvBindByPosition(position = 0)
   private String cartNumber;

   @CsvBindByPosition(position = 1)
   private String email;

   @CsvBindByPosition(position = 2)
   private String firstName;

   @CsvBindByPosition(position = 3)
   private String lastName;

   @CsvBindByPosition(position = 4)
   private String date;

   @Override
   protected StatefulBeanToCsvBuilder<? extends BaseDoneTracker<CsvKeelboatList>> pwm2_getStatefulBeanToCsvBuilder(
         Writer writerPwm) {
      return new StatefulBeanToCsvBuilder<CsvKeelboatList>(writerPwm);
   }

   @SuppressWarnings("unchecked")
   @Override
   protected void pwm2_writeOne(BaseDoneTracker<CsvKeelboatList> pwm,
         StatefulBeanToCsv<? extends BaseDoneTracker<CsvKeelboatList>> sbc)
         throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

      ((StatefulBeanToCsv<CsvKeelboatList>) sbc).write((CsvKeelboatList) pwm);
   }

   @Override
   protected BaseDoneTracker<CsvKeelboatList> pwm_build(TmpBaseCsvRow full, String inDate) {
      // @formatter:off
      return CsvKeelboatList.builder()
            .cartNumber(full.getCartNumber())
            .email(full.getEmail())
            .firstName(full.getFirstName())
            .lastName(full.getLastName())
            .date(inDate)
            .build();
      // @formatter:on
   }

   // PWM - why does this one have dataDir, but getDoneCsvFilename() does NOT?
   @Override
   protected String getTodoCsvFilePath(String dataDir) {
      return dataDir + "KeelboatList-todo.csv";
   }

   @Override
   protected String getDoneCsvFilename() {
      return "KeelboatList-done.csv";
   }

   @Override
   protected List<? extends TmpBaseCsvRow> filterRelevantRegistrantRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return Util.getMooringTypeKeelboatRows(allRegistrantRows);
   }

   @Override
   protected void optionalWriteOtherAll(List<? extends TmpBaseCsvRow> all, String dataDir) throws IOException {

      // PWM - is this required for Keelboats??? I think only required for
      // "duplicates"...
      Set<String> alreadyWritten = new HashSet<String>();

      BufferedWriter writer = new BufferedWriter(new FileWriter(dataDir + "KeelboatList-Full.txt"));

      ZonedDateTime now = ZonedDateTime.now();
      String formatted = now.format(DateTimeFormatter.RFC_1123_DATE_TIME);
      writer.write("Keelboats (" + all.size() + " total) as of " + formatted + ":\n");
      writer.write("\n");
      writer.write("Email list:\n");
      for (TmpBaseCsvRow member : all) {

         if (!alreadyWritten.contains(member.getEmail())) {
            writer.write(member.getEmail() + "\n");
            alreadyWritten.add(member.getEmail());
         }
      }

      writer.write("\n");
      writer.write("Keelboat details:\n");
      for (TmpBaseCsvRow member : all) {


         // @formatter:off
         writer.write(" - " 
            + member.getEmail()
            + " (" + member.getFullName() + ") "
            + member.getBoatType() + " "
            + member.getBoatTypeOther()
            + "\n");
         // @formatter:on
      }
      writer.close();

   }

   protected void optionalWriteOtherTodos(ArrayList<BaseDoneTracker<CsvKeelboatList>> todoPwms, String dataDir)
         throws IOException {
      System.out.println("New Keelboats to notify the Wet Harbourmaster about: " + todoPwms.size());
   }

}
