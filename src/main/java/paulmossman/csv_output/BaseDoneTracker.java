package paulmossman.csv_output;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import paulmossman.csv.base.TmpBaseCsvRow;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public abstract class BaseDoneTracker<T extends BaseDoneTracker<T>> {


   public void generateTodos(List<? extends TmpBaseCsvRow> allRegistrantRows, String dataDir) throws Exception {
      generateTodos(allRegistrantRows, dataDir, false);
   }

   public void generateTodos(List<? extends TmpBaseCsvRow> allRegistrantRows, String dataDir,
         boolean keepDuplicates) throws Exception {

      List<? extends TmpBaseCsvRow> filteredRelevantRegistrantRows = filterRelevantRegistrantRows(allRegistrantRows);

      optionalWriteOtherAll(filteredRelevantRegistrantRows, dataDir);

      // All the emails that have already been done.
      Set<String> doneEmails = loadFromDoneCsv(dataDir)
            .stream().map(BaseDoneTracker::getEmail).collect(Collectors.toSet());

      // Collect the Registrant Rows that are **not** done yet. If there are
      // multiple with the same email address, keep only the first.
      List<? extends TmpBaseCsvRow> todoRegistrantRows = keepDuplicates
            ? filteredRelevantRegistrantRows.stream().filter(r -> !doneEmails.contains(r.getEmail()))
                  .collect(Collectors.toList())
            : filteredRelevantRegistrantRows.stream().filter(r -> !doneEmails.contains(r.getEmail()))
                  .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TmpBaseCsvRow::getEmail))),
                        ArrayList::new));
      
      // YYYY-MM-DD
      String date = java.time.LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

      // Map each to a Done Tracker object.
      ArrayList<BaseDoneTracker<T>> todoPwms = new ArrayList<>();
      for (TmpBaseCsvRow full : todoRegistrantRows) {
         todoPwms.add(pwm_build(full, date));
      }

      // This file has all the CSV columns
      Writer writerPwm = new FileWriter(getTodoCsvFilePath(dataDir));

      StatefulBeanToCsv<? extends BaseDoneTracker<T>> sbc = pwm2_getStatefulBeanToCsvBuilder(writerPwm)
            .withSeparator(CSVWriter.DEFAULT_SEPARATOR).withApplyQuotesToAll(false).build();

      // Write each one.
      for (BaseDoneTracker<T> pwm : todoPwms) {
         pwm2_writeOne(pwm, sbc);
      }

      writerPwm.close();

      // PWM - optional others
      optionalWriteOtherTodos(todoPwms, dataDir);
   }

   protected void optionalWriteOtherTodos(ArrayList<BaseDoneTracker<T>> todoPwms, String dataDir) throws IOException {
      // No by default, though subclasses can override.
   }

   protected void optionalWriteOtherAll(List<? extends TmpBaseCsvRow> all, String dataDir) throws IOException {
      // No by default, though subclasses can override.
   }

   protected abstract StatefulBeanToCsvBuilder<? extends BaseDoneTracker<T>> pwm2_getStatefulBeanToCsvBuilder(
         Writer writerPwm);

   protected abstract void pwm2_writeOne(BaseDoneTracker<T> pwm, StatefulBeanToCsv<? extends BaseDoneTracker<T>> sbc)
         throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException;

   protected abstract BaseDoneTracker<T> pwm_build(TmpBaseCsvRow full, String inDate);

   // PWM TODO: instead of passing IN dataDir, just make this return the core
   // string
   // and then we'll add dataDir and .csv....
   protected abstract String getTodoCsvFilePath(String dataDir);

   protected abstract String getDoneCsvFilename();

   protected abstract String getEmail();

   protected abstract List<? extends TmpBaseCsvRow> filterRelevantRegistrantRows(
         List<? extends TmpBaseCsvRow> allRegistrantRows);

   private List<BaseDoneTracker<T>> loadFromDoneCsv(String dataDir) throws IOException {

      @SuppressWarnings("unchecked")
      CsvToBean<BaseDoneTracker<T>> csvToBean = new CsvToBeanBuilder<BaseDoneTracker<T>>(getDoneCsvReader(dataDir))
            .withType((Class<T>) this.getClass()).withIgnoreLeadingWhiteSpace(true).build();

      return csvToBean.parse();
   }

   private Reader getDoneCsvReader(String dataDir) throws IOException {

      Path path = Paths.get(dataDir + getDoneCsvFilename());

      // Does the file already exist?
      if (!path.toFile().exists()) {
         // No, so create an empty file.
         Files.createFile(path);
      }

      return Files.newBufferedReader(path);
   }
}
