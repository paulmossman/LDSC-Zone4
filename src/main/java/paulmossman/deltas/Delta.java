package paulmossman.deltas;

import java.io.IOException;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import paulmossman.Util;
import paulmossman.csv.base.TmpBaseCsvRow;
import paulmossman.registration.Registration;

public class Delta {

   public static void main(String[] args) throws Exception {

      String year;
      if (args.length == 0) {
         year = String.valueOf(Year.now().getValue());
      } else {
         year = args[0];
      }

      int yearInt = Integer.parseInt(year);

      printRegistrationsWhoDidNotReturn(yearInt);
   }

   public static List<String> getUniqueAdultEmails(List<Registration> didNotReturnRegistrations) {
      return didNotReturnRegistrations.stream().flatMap(reg -> reg.getMembers().stream()).filter(TmpBaseCsvRow::isAdult)
            .map(TmpBaseCsvRow::getEmail).filter(Objects::nonNull).map(email -> email.trim().toLowerCase()).distinct()
            .sorted().collect(Collectors.toList());
   }

   public static List<Registration> getRegistrationsWhoDidNotReturn(int year) throws IOException{
      List<? extends TmpBaseCsvRow> subsequent = Util.getAllRegistrantsByYear(year);
      int previousYear = year - 1;
      List<? extends TmpBaseCsvRow> previous = Util.getAllRegistrantsByYear(previousYear);
      if (previous == null) {
         System.err.printf("Error: Cannot load year %d.\n", previousYear);
         return null;
      }

      return findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(previous, subsequent);
   }
   
   public static void printRegistrationsWhoDidNotReturn(int year) throws IOException {

      int previousYear = year - 1;

      List<Registration> didNotReturnRegistrations = getRegistrationsWhoDidNotReturn(year);

      System.out.printf("Did not return in %d:\n", year);
      for (Registration r : didNotReturnRegistrations) {
         System.out.println("  Previous Cart #: " + r.getCartNumber());
         for (TmpBaseCsvRow person : r.getMembers()) {
            System.out.printf("    - %s %s%s\n", person.getFullName(), person.isAdult() ? "" : "(youth) ",
                  person.getEmail());
         }
      }

      System.out.println();
      List<String> emails = getUniqueAdultEmails(didNotReturnRegistrations);
      System.out.printf("Did not return in %d - Unique adult emails (%d):\n", year, emails.size());
      for (String email : emails) {
         System.out.println(email);
      }
      System.out.println();
      System.out.printf("(Members in %d, did not return in %d.)\n", previousYear, year);
   }

   public static List<Registration> findRegistrationsFromPreviousThatAreInSubsequentWrapper(
         List<? extends TmpBaseCsvRow> previous, List<? extends TmpBaseCsvRow> subsequent) {

      List<Registration> previousRegistrations = Registration.getRegistrationsFromMemberRows(previous);
      List<Registration> subsequentRegistrations = Registration.getRegistrationsFromMemberRows(subsequent);

      return findRegistrationsFromPreviousThatAreInSubsequent(previousRegistrations, subsequentRegistrations);
   }

   public static List<Registration> findRegistrationsFromPreviousThatAreInSubsequent(
         List<Registration> previousRegistrations, List<Registration> subsequentRegistrations) {
      return filterRegistrationsBasedOnSubsequentMatch(previousRegistrations, subsequentRegistrations, true);
   }



   public static List<Registration> findRegistrationsFromPreviousThatAreNotInSubsequentWrapper(
         List<? extends TmpBaseCsvRow> previous,
         List<? extends TmpBaseCsvRow> subsequent) {

      List<Registration> previousRegistrations = Registration.getRegistrationsFromMemberRows(previous);
      List<Registration> subsequentRegistrations = Registration.getRegistrationsFromMemberRows(subsequent);

      return findRegistrationsFromPreviousThatAreNotInSubsequent(previousRegistrations, subsequentRegistrations);
   }

   public static List<Registration> findRegistrationsFromPreviousThatAreNotInSubsequent(
         List<Registration> previousRegistrations, List<Registration> subsequentRegistrations) {
      return filterRegistrationsBasedOnSubsequentMatch(previousRegistrations, subsequentRegistrations, false);
   }

   private static List<Registration> filterRegistrationsBasedOnSubsequentMatch(List<Registration> previousRegistrations,
         List<Registration> subsequentRegistrations, boolean includeMatches) {

      Set<String> subsequentEmails = new HashSet<>();
      Set<String> subsequentNames = new HashSet<>();

      for (Registration reg : subsequentRegistrations) {
         for (TmpBaseCsvRow row : reg.getMembers()) {
            if (row.getEmail() != null)
               subsequentEmails.add(row.getEmail().trim().toLowerCase());
            if (row.getFullName() != null)
               subsequentNames.add(row.getFullName().trim().toLowerCase());
         }
      }

      List<Registration> result = new ArrayList<>();

      for (Registration reg : previousRegistrations) {
         boolean hasMatch = false;

         for (TmpBaseCsvRow row : reg.getMembers()) {
            String email = row.getEmail() != null ? row.getEmail().trim().toLowerCase() : null;
            String name = row.getFullName() != null ? row.getFullName().trim().toLowerCase() : null;

            if ((email != null && subsequentEmails.contains(email))
                  || (name != null && subsequentNames.contains(name))) {
               hasMatch = true;
               break;
            }
         }

         if (hasMatch == includeMatches) {
            result.add(reg);
         }
      }

      return result;
   }
}
