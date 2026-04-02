package paulmossman.deltas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import paulmossman.Util;
import paulmossman.csv.base.CsvRow2025toFuture;
import paulmossman.csv.base.TmpBaseCsvRow;
import paulmossman.csv.y2025.CsvAllRegistrants;
import paulmossman.registration.Registration;
import org.junit.jupiter.api.Test;

public class DeltaTest {

   private String adultAgeGroup;

   public DeltaTest() {

      CsvRow2025toFuture ref = CsvAllRegistrants.builder().build();
      adultAgeGroup = ref.getAdultAgeGroups().stream().findFirst().get();
   }

   private CsvAllRegistrants getAdult(String email, String name, String cartNumber) {
      return CsvAllRegistrants.builder().email(email).firstName(name).cartNumber(cartNumber)
            .build();
   }

   @Test
   public void basicFindMembersWhoDidNotReturn() throws IOException {

      // ** Previous
      List<TmpBaseCsvRow> previous = new ArrayList<TmpBaseCsvRow>();

      CsvAllRegistrants returningMember = getAdult("bob@returningFamily.com", "Bob", "returningFamily");
      previous.add(returningMember);
      previous.add(getAdult("alice@returningFamily.com", "Alice", "returningFamily"));

      // This member won't return
      CsvAllRegistrants memberThatWillNotReturn = getAdult("jane@notReturningFamily.com", "Jane", "notReturningFamily");
      previous.add(memberThatWillNotReturn);

      // ** Subsequent
      List<TmpBaseCsvRow> subsequent = new ArrayList<TmpBaseCsvRow>();

      // Bob returns, but a different other family member.
      subsequent.add(returningMember);
      subsequent.add(getAdult("mary@returningFamily.com", "Mary", "returningFamily"));

      // A new member
      subsequent.add(getAdult("ella@newReturningFamily.com", "Ella", "newFamily"));

      List<Registration> didNotReturn = Delta.findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(
            previous,
            subsequent);

      assertNotNull(didNotReturn);
      assertEquals(1, didNotReturn.size());
      List<TmpBaseCsvRow> didNotReturnPeople = didNotReturn.get(0).getMembers();
      assertEquals(1, didNotReturnPeople.size());

      assertEquals(memberThatWillNotReturn.getEmail(), didNotReturnPeople.get(0).getEmail());
      sizeTotalCheck(previous, didNotReturn,
            Delta.findRegistrationsFromPreviousThatAreInSubsequentWrapper(previous, subsequent));
   }

   @Test
   public void testUnsortedInputStillWorks() throws IOException {
      List<TmpBaseCsvRow> previous = Arrays.asList(getAdult("b@x.com", "Bob", "c2"), getAdult("a@x.com", "Alice", "c1"),
            getAdult("j@x.com", "Jane", "c3"));

      List<TmpBaseCsvRow> subsequent = Arrays.asList(getAdult("a@x.com", "Alice", "x1"));

      List<Registration> didNotReturn = Delta.findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(previous,
            subsequent);

      Set<String> missingCartNumbers = new HashSet<>();
      for (Registration r : didNotReturn) {
         missingCartNumbers.add(r.getCartNumber());
      }

      assertEquals(Set.of("c2", "c3"), missingCartNumbers);
      sizeTotalCheck(previous, didNotReturn,
            Delta.findRegistrationsFromPreviousThatAreInSubsequentWrapper(previous, subsequent));
   }

   @Test
   public void testNullEmailsAndNamesHandledGracefully() throws IOException {
      List<TmpBaseCsvRow> previous = Arrays.asList(getAdult(null, null, "nullFam"),
            getAdult("real@x.com", "Real", "realFam"));

      List<TmpBaseCsvRow> subsequent = Arrays.asList(getAdult("real@x.com", "Real", "otherCart"));

      List<Registration> didNotReturn = Delta.findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(previous,
            subsequent);
      assertEquals(1, didNotReturn.size());
      assertEquals("nullFam", didNotReturn.get(0).getCartNumber());
      sizeTotalCheck(previous, didNotReturn,
            Delta.findRegistrationsFromPreviousThatAreInSubsequentWrapper(previous, subsequent));
   }

   @Test
   public void testDuplicateEmailsInDifferentRegistrations() throws IOException {
      List<TmpBaseCsvRow> previous = Arrays.asList(getAdult("shared@x.com", "A", "cart1"),
            getAdult("unique@x.com", "U", "cart2"));

      List<TmpBaseCsvRow> subsequent = Arrays.asList(getAdult("shared@x.com", "Z", "newCart"));

      List<Registration> didNotReturn = Delta.findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(previous,
            subsequent);
      assertEquals(1, didNotReturn.size());
      assertEquals("cart2", didNotReturn.get(0).getCartNumber());
      sizeTotalCheck(previous, didNotReturn,
            Delta.findRegistrationsFromPreviousThatAreInSubsequentWrapper(previous, subsequent));
   }

   @Test
   public void testSameNameDifferentEmails() throws IOException {
      List<TmpBaseCsvRow> previous = Arrays.asList(getAdult("a@x.com", "Chris", "fam1"));

      List<TmpBaseCsvRow> subsequent = Arrays.asList(getAdult("b@x.com", "Chris", "fam2"));

      List<Registration> didNotReturn = Delta.findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(previous,
            subsequent);

      // Should match on name
      assertTrue(didNotReturn.isEmpty());
      sizeTotalCheck(previous, didNotReturn,
            Delta.findRegistrationsFromPreviousThatAreInSubsequentWrapper(previous, subsequent));
   }

   @Test
   public void testSameEmailDifferentName() throws IOException {
      List<TmpBaseCsvRow> previous = Arrays.asList(getAdult("same@x.com", "Original", "old"));

      List<TmpBaseCsvRow> subsequent = Arrays.asList(getAdult("same@x.com", "Updated", "new"));

      List<Registration> didNotReturn = Delta.findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(previous,
            subsequent);

      // Should match on email
      assertTrue(didNotReturn.isEmpty());
      sizeTotalCheck(previous, didNotReturn,
            Delta.findRegistrationsFromPreviousThatAreInSubsequentWrapper(previous, subsequent));
   }

   @Test
   public void testNoMatchesAtAll() throws IOException {
      List<TmpBaseCsvRow> previous = Arrays.asList(getAdult("a1@x.com", "A1", "x1"), getAdult("a2@x.com", "A2", "x2"));

      List<TmpBaseCsvRow> subsequent = Arrays.asList(getAdult("b1@x.com", "B1", "y1"),
            getAdult("b2@x.com", "B2", "y2"));

      List<Registration> didNotReturn = Delta.findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(previous,
            subsequent);

      assertEquals(2, didNotReturn.size());
      sizeTotalCheck(previous, didNotReturn,
            Delta.findRegistrationsFromPreviousThatAreInSubsequentWrapper(previous, subsequent));
   }

   @Test
   public void testAllReturned() throws IOException {
      List<TmpBaseCsvRow> previous = Arrays.asList(getAdult("a@x.com", "A", "f1"), getAdult("b@x.com", "B", "f2"));

      List<TmpBaseCsvRow> subsequent = Arrays.asList(getAdult("a@x.com", "A", "new1"),
            getAdult("b@x.com", "B", "new2"));

      List<Registration> didNotReturn = Delta.findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(previous,
            subsequent);

      assertTrue(didNotReturn.isEmpty());
      sizeTotalCheck(previous, didNotReturn,
            Delta.findRegistrationsFromPreviousThatAreInSubsequentWrapper(previous, subsequent));
   }

   private void sizeTotalCheck(List<TmpBaseCsvRow> subsequent, List<Registration> didNotReturn,
         List<Registration> didReturn) {

      assertEquals(Util.getOneRowForEachCart(subsequent).size(), didNotReturn.size() + didReturn.size(),
            "Did and did not total error");
   }
}
