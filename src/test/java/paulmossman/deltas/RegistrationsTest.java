package paulmossman.deltas;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import paulmossman.csv.base.CsvRow2025toFuture;
import paulmossman.csv.y2025.CsvAllRegistrants;
import paulmossman.deltas.Registration;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class RegistrationsTest {

   private String adultAgeGroup;
   private String youthAgeGroup;

   public RegistrationsTest() {

      CsvRow2025toFuture ref = CsvAllRegistrants.builder().build();
      adultAgeGroup = ref.getAdultAgeGroups().stream().findFirst().get();
      youthAgeGroup = ref.getYouthAgeGroups().stream().findFirst().get();
   }

   private CsvAllRegistrants getAdult(String email) {
      return CsvAllRegistrants.builder().ageGroup(adultAgeGroup).email(email).build();
   }

   private CsvAllRegistrants getYouth(String email) {
      return CsvAllRegistrants.builder().ageGroup(youthAgeGroup).email(email).build();
   }

   @Test
   public void basicOverlapYes() throws IOException {

      Registration r1 = new Registration("cart1");
      r1.addIfAdult(getAdult("bob@example.com"));

      Registration r2 = new Registration("cart2");
      r2.addIfAdult(getAdult("bob@example.com"));

      assertTrue(r1.overlaps(r2));
      assertTrue(r2.overlaps(r1));
   }

   @Test
   public void basicOverlapNo() throws IOException {

      Registration r1 = new Registration("cart1");
      r1.addIfAdult(getAdult("bob@example.com"));
      r1.addIfAdult(getYouth("alice@example.com"));
 
      Registration r2 = new Registration("cart2");
      r2.addIfAdult(getAdult("alice@example.com"));

      assertFalse(r1.overlaps(r2));
      assertFalse(r2.overlaps(r1));
   }
}
