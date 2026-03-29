package paulmossman.csv_output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

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
public class CsvMemberMailingList extends BaseDoneTracker<CsvMemberMailingList> {

   @CsvBindByPosition(position = 0)
   private String email;

   @CsvBindByPosition(position = 1)
   private String firstName;

   @CsvBindByPosition(position = 2)
   private String lastName;

   @CsvBindByPosition(position = 3)
   private String date;

   @Override
   protected String getDoneCsvFilename() {
      return "MailingList-done.csv";
   }

   // PWM collapse? - only called once - or MAYBE NOT - longshot
   @Override
   protected String getTodoCsvFilePath(String dataDir) {
      return dataDir + "MailingList-todo.csv";
   }

   protected List<? extends TmpBaseCsvRow> filterRelevantRegistrantRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {
      // All members get put on the mailing list.
      return allRegistrantRows;
   }

   @Override
   protected BaseDoneTracker<CsvMemberMailingList> pwm_build(TmpBaseCsvRow full, String inDate) {
      return CsvMemberMailingList.builder().date(inDate).email(full.getEmail()).firstName(full.getFirstName())
            .lastName(full.getLastName()).build();
   }

   @Override
   protected void optionalWriteOtherTodos(ArrayList<BaseDoneTracker<CsvMemberMailingList>> todoPwms, String dataDir)
         throws IOException {
      // This file is the MailChimp CSV import format.
      BufferedWriter writer = new BufferedWriter(new FileWriter(dataDir + "MailChimpImport-MailingList.csv"));
      writer.write("Email Address,First Name,Last Name\n");
      for (BaseDoneTracker<CsvMemberMailingList> pwm : todoPwms) {

         CsvMemberMailingList member = (CsvMemberMailingList) pwm;

         writer.write(member.getEmail() + "," + member.getFirstName() + "," + member.getLastName() + "\n");
      }
      writer.close();

      System.out.println("New addresses to add to the Mailing List: " + todoPwms.size());
   }

   @Override
   protected StatefulBeanToCsvBuilder<? extends BaseDoneTracker<CsvMemberMailingList>> pwm2_getStatefulBeanToCsvBuilder(
         Writer writerPwm) {
      return new StatefulBeanToCsvBuilder<CsvMemberMailingList>(writerPwm);
   }

   @SuppressWarnings("unchecked")
   @Override
   protected void pwm2_writeOne(BaseDoneTracker<CsvMemberMailingList> pwm,
         StatefulBeanToCsv<? extends BaseDoneTracker<CsvMemberMailingList>> sbc)
         throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

      ((StatefulBeanToCsv<CsvMemberMailingList>) sbc).write((CsvMemberMailingList) pwm);
   }


}
