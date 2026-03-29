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
public class CsvClubBoatList extends BaseDoneTracker<CsvClubBoatList> {

   @CsvBindByPosition(position = 0)
   private String cartNumber;

   @CsvBindByPosition(position = 1)
   private String email;

   @CsvBindByPosition(position = 2)
   private String date;

   // PWM TODO: pwm_writeOptionalOther
   // PWM TODO: ALSO - delete all generated files from dirs, the run project test
   // Which will detect missing files.

   @Override
   protected StatefulBeanToCsvBuilder<? extends BaseDoneTracker<CsvClubBoatList>> pwm2_getStatefulBeanToCsvBuilder(
         Writer writerPwm) {
      return new StatefulBeanToCsvBuilder<CsvClubBoatList>(writerPwm);
   }

   @SuppressWarnings("unchecked")
   @Override
   protected void pwm2_writeOne(BaseDoneTracker<CsvClubBoatList> pwm,
         StatefulBeanToCsv<? extends BaseDoneTracker<CsvClubBoatList>> sbc)
         throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

      ((StatefulBeanToCsv<CsvClubBoatList>) sbc).write((CsvClubBoatList) pwm);
   }

   @Override
   protected BaseDoneTracker<CsvClubBoatList> pwm_build(TmpBaseCsvRow full, String inDate) {
      return CsvClubBoatList.builder().date(inDate).email(full.getEmail()).cartNumber(full.getCartNumber()).build();
   }

   // PWM - why does this one have dataDir, but getDoneCsvFilename() does NOT?
   @Override
   protected String getTodoCsvFilePath(String dataDir) {
      return dataDir + "ClubBoatList-todo.csv";
   }

   @Override
   protected String getDoneCsvFilename() {
      return "ClubBoatList-done.csv";
   }

   @Override
   protected List<? extends TmpBaseCsvRow> filterRelevantRegistrantRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return Util.getAllMembersWithClubBoatAccessRows(allRegistrantRows);
   }

   @Override
   protected void optionalWriteOtherAll(List<? extends TmpBaseCsvRow> all, String dataDir)
         throws IOException {

      //
      Set<String> alreadyWritten = new HashSet<String>();

      BufferedWriter writer = new BufferedWriter(new FileWriter(dataDir + "ClubBoatList-Full.txt"));

      ZonedDateTime now = ZonedDateTime.now();
      String formatted = now.format(DateTimeFormatter.RFC_1123_DATE_TIME);
      writer.write("REMINDER: Use \"Bcc\" when emailing these members, *not* \"Cc\" or \"To\".\n");
      writer.write("\n");
      writer.write("Club Boat access email list as of " + formatted + ":\n");
      writer.write("\n");
      for (TmpBaseCsvRow member : all) {

         if (!alreadyWritten.contains(member.getEmail())) {
            writer.write(member.getEmail() + "\n");
            alreadyWritten.add(member.getEmail());
         }
      }
      writer.close();

   }

   protected void optionalWriteOtherTodos(ArrayList<BaseDoneTracker<CsvClubBoatList>> todoPwms, String dataDir)
         throws IOException {
      System.out.println("New Club Boat members to notify the manager about: " + todoPwms.size());
   }


}
