package paulmossman.registration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import paulmossman.Util;
import paulmossman.csv.base.TmpBaseCsvRow;
public class Registration {

   private final String cartNumber;
   private final List<TmpBaseCsvRow> members;

   public boolean isNew() {
      return members.get(0).isNewMember();
   }

   public Registration(String cartNumber) {
      this.cartNumber = cartNumber;
      this.members = new ArrayList<TmpBaseCsvRow>();
   }

   public Registration(String cartNumber, List<TmpBaseCsvRow> members) {
      this.cartNumber = cartNumber;
      this.members = members;
   }

   public String getCartNumber() {
      return cartNumber;
   }

   public List<TmpBaseCsvRow> getMembers() {
      return members;
   }

   public List<TmpBaseCsvRow> getAdultMembers() {
      return members.stream().filter(TmpBaseCsvRow::isAdult).collect(Collectors.toList());
   }

   public boolean hasKeelboat() {
      return members.stream().anyMatch(TmpBaseCsvRow::isMooringTypeKeelboat);
   }

   public List<TmpBaseCsvRow> getKeelboatMembers() {
      return members.stream().filter(TmpBaseCsvRow::isMooringTypeKeelboat).collect(Collectors.toList());
   }

   public TmpBaseCsvRow getOnlyKeelboatMember() {
      List<TmpBaseCsvRow> keelboatMembers = getKeelboatMembers();
      if (keelboatMembers.size() != 1) {
         throw new IllegalStateException(
               "Expected exactly 1 keelboat row for cart " + cartNumber + ", but found " + keelboatMembers.size());
      }
      return keelboatMembers.get(0);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (!(o instanceof Registration))
         return false;
      Registration that = (Registration) o;
      return Objects.equals(cartNumber, that.cartNumber);
   }

   @Override
   public int hashCode() {
      return Objects.hash(cartNumber);
   }

   public boolean overlaps(Registration other) {
      List<String> thisAdultEmails = this.getAdultEmails();
      List<String> otherAdultEmails = other.getAdultEmails();
      
      for (String email : thisAdultEmails) {
         if (otherAdultEmails.contains(email)) {
            return true;
         }
      }
      return false;
   }

   public List<String> getAdultEmails() {
      return getAdultMembers().stream().map(TmpBaseCsvRow::getEmail).collect(Collectors.toList());
   }

   public static List<Registration> getRegistrationsFromMemberRows(List<? extends TmpBaseCsvRow> rows) {
      Map<String, List<TmpBaseCsvRow>> grouped = rows.stream()
            .collect(Collectors.groupingBy(TmpBaseCsvRow::getCartNumber));

      return grouped.entrySet().stream().map(entry -> new Registration(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
   }

   public static List<Registration> getRegistrationsByYear(int year) throws IOException {
      List<? extends TmpBaseCsvRow> yearMembersRows = Util.getAllRegistrantsByYear(year);
      return Registration.getRegistrationsFromMemberRows(yearMembersRows);
   }

   public void addIfAdult(TmpBaseCsvRow registrant) {
      if(registrant.isAdult()) {
         members.add(registrant);
      }
   }
}
