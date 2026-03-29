package paulmossman.deltas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import paulmossman.csv.base.CsvRow2025toFuture;
import paulmossman.csv.y2025.CsvAllRegistrants;
import org.junit.jupiter.api.Test;

public class UtilTest {
   
   private String adultAgeGroup;
   
   public UtilTest() {

      CsvRow2025toFuture ref = CsvAllRegistrants.builder().build();
      adultAgeGroup = ref.getAdultAgeGroups().stream().findFirst().get();
   }

   private CsvAllRegistrants getAdult(String email, String cartNumber) {
      return CsvAllRegistrants.builder().ageGroup(adultAgeGroup).email(email).cartNumber(cartNumber).build();
   }
  
   @Test
   public void basicFindMembersWhoDidNotReturn() throws IOException {

      // ** Previous
      List<CsvAllRegistrants> allRegistrantRowsPrevious = new ArrayList<CsvAllRegistrants>();

      CsvAllRegistrants returningMember = getAdult("bob@returningFamily.com", "returningFamily");
      allRegistrantRowsPrevious.add(returningMember);
      allRegistrantRowsPrevious.add(getAdult("alice@returningFamily.com", "returningFamily"));

      // This member won't return
      CsvAllRegistrants memberThatWillNotReturn = getAdult("jane@notReturningFamily.com", "notReturningFamily");
      allRegistrantRowsPrevious.add(memberThatWillNotReturn);

      // ** Subsequent
      List<CsvAllRegistrants> allRegistrantRowsSubsequent = new ArrayList<CsvAllRegistrants>();

      // Bob returns, but a different other family member.
      allRegistrantRowsSubsequent.add(returningMember);
      allRegistrantRowsSubsequent.add(getAdult("mary@returningFamily.com", "returningFamily"));

      // A new member
      allRegistrantRowsSubsequent.add(getAdult("ella@newReturningFamily.com", "newFamily"));


      List<String> didNotReturnEmails = Util.findMembersWhoDidNotReturn(allRegistrantRowsPrevious, allRegistrantRowsSubsequent);

      assertNotNull(didNotReturnEmails);
      assertEquals(1, didNotReturnEmails.size());
      assertEquals(memberThatWillNotReturn.getEmail(), didNotReturnEmails.get(0));
   }

   @Test
   public void testMultipleCartsMixedReturns() {
      List<CsvAllRegistrants> previous = new ArrayList<>();
      List<CsvAllRegistrants> current = new ArrayList<>();

      previous.add(getAdult("a@one.com", "returns1"));
      previous.add(getAdult("b@one.com", "returns1")); // Returns

      previous.add(getAdult("c@two.com", "leaves1")); // Does not return

      previous.add(getAdult("d@three.com", "returns2")); // Returns
      previous.add(getAdult("e@three.com", "returns2")); // Does not return

      previous.add(getAdult("f@four.com", "returns3")); // Does not return

      previous.add(getAdult("g@five.com", "cart5")); // Returns

      // Current
      current.add(getAdult("b@one.com", "returns1"));
      current.add(getAdult("d@three.com", "returns2"));
      current.add(getAdult("g@five.com", "returns3"));

      List<String> didNotReturn = Util.findMembersWhoDidNotReturn(previous, current);
      assertEquals(2, didNotReturn.size());
      List<String> expected = List.of("c@two.com", "f@four.com");
      for (String email : expected) {
         assert (didNotReturn.contains(email));
      }
   }

   @Test
   public void testAllMembersReturn() {
      List<CsvAllRegistrants> previous = new ArrayList<>();
      List<CsvAllRegistrants> current = new ArrayList<>();

      previous.add(getAdult("a@alpha.com", "returns1"));
      previous.add(getAdult("b@beta.com", "returns2"));

      current.add(getAdult("a@alpha.com", "returns1"));
      current.add(getAdult("b@beta.com", "returns2"));

      List<String> didNotReturn = Util.findMembersWhoDidNotReturn(previous, current);
      assertEquals(0, didNotReturn.size());
   }

   @Test
   public void testNoneReturn() {
      List<CsvAllRegistrants> previous = new ArrayList<>();
      List<CsvAllRegistrants> current = new ArrayList<>();

      previous.add(getAdult("x@x.com", "leaves1"));
      previous.add(getAdult("y@y.com", "leaves2"));

      current.add(getAdult("a@a.com", "new")); // completely new

      List<String> didNotReturn = Util.findMembersWhoDidNotReturn(previous, current);
      assertEquals(2, didNotReturn.size());
      List<String> expected = List.of("x@x.com", "y@y.com");
      for (String email : expected) {
         assert (didNotReturn.contains(email));
      }
   }

   @Test
   public void testInputNotSorted() {
      List<CsvAllRegistrants> previous = new ArrayList<>();
      List<CsvAllRegistrants> current = new ArrayList<>();

      // Out of order carts
      previous.add(getAdult("c@foo.com", "z"));
      previous.add(getAdult("a@foo.com", "x"));
      previous.add(getAdult("b@foo.com", "y"));

      current.add(getAdult("a@foo.com", "x"));
      current.add(getAdult("b@foo.com", "y"));

      List<String> didNotReturn = Util.findMembersWhoDidNotReturn(previous, current);
      assertEquals(1, didNotReturn.size());
      assertEquals("c@foo.com", didNotReturn.get(0));
   }
}
