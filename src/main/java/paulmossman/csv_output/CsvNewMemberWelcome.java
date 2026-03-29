package paulmossman.csv_output;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class CsvNewMemberWelcome extends BaseDoneTracker<CsvNewMemberWelcome> {

   @CsvBindByPosition(position = 0)
   private String cartNumber;

   @CsvBindByPosition(position = 1)
   private String email;

   @CsvBindByPosition(position = 2)
   private String date;

   @CsvBindByPosition(position = 3)
   private String firstName;

   @CsvBindByPosition(position = 4)
   private Boolean clubBoatAccess;

   // Filter to only new members.
   @Override
   protected List<? extends TmpBaseCsvRow> filterRelevantRegistrantRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {
      return Util.getNewMemberRows(allRegistrantRows);
   }

   @Override
   protected BaseDoneTracker<CsvNewMemberWelcome> pwm_build(TmpBaseCsvRow full, String inDate) {
      // @formatter:off
      return CsvNewMemberWelcome.builder()
            .date(inDate)
            .email(full.getEmail())
            .cartNumber(full.getCartNumber())
            .firstName(full.getFirstName())
            .clubBoatAccess(full.hasClubBoatAccess())
            .build();
      // @formatter:on
   }

   @Override
   protected void optionalWriteOtherTodos(ArrayList<BaseDoneTracker<CsvNewMemberWelcome>> todoPwms, String dataDir)
         throws IOException {

      // This file is ONLY the emails - PWM....
      BufferedWriter writer = new BufferedWriter(new FileWriter(dataDir + "NewMemberWelcome-todo.txt"));

      // Cast to subclass
      Map<String, List<CsvNewMemberWelcome>> cartMemberMap = todoPwms.stream().map(item -> (CsvNewMemberWelcome) item)
            .collect(Collectors.groupingBy(CsvNewMemberWelcome::getCartNumber));

      cartMemberMap.forEach((cartNumber, members) -> {
         CsvNewMemberWelcome first = members.get(0);
         try {
            writer.write("Cart Number: " + cartNumber + "  ClubBoatAccess: " + first.getClubBoatAccess() + "\n");
         
            writer.write("   Emails: ");
            for (CsvNewMemberWelcome member : members) {
               writer.write(member.getEmail() + ",");
            }
            writer.write("\n");

            writer.write("   First names: ");
            for (CsvNewMemberWelcome member : members) {
               writer.write(member.getFirstName() + ", ");
            }
            writer.write("\n\n");

         } catch (IOException e) {
         }
      });

      writer.close();

      System.out.println("New members to be welcomed: " + todoPwms.size());
   }

   @Override
   protected StatefulBeanToCsvBuilder<? extends CsvNewMemberWelcome> pwm2_getStatefulBeanToCsvBuilder(
         Writer writerPwm) {
      return new StatefulBeanToCsvBuilder<CsvNewMemberWelcome>(writerPwm);
   }

   @SuppressWarnings("unchecked")
   @Override
   protected void pwm2_writeOne(BaseDoneTracker<CsvNewMemberWelcome> pwm,
         StatefulBeanToCsv<? extends BaseDoneTracker<CsvNewMemberWelcome>> sbc)
         throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {

      ((StatefulBeanToCsv<CsvNewMemberWelcome>) sbc).write((CsvNewMemberWelcome) pwm);
   }

   @Override
   protected String getTodoCsvFilePath(String dataDir) {
      return dataDir + "NewMemberWelcome-todo.csv";
   }

   @Override
   protected String getDoneCsvFilename() {
      return "NewMemberWelcome-done.csv";
   }

}