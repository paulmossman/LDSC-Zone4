package paulmossman;

import java.util.ArrayList;
import java.util.List;

import paulmossman.csv.base.TmpBaseCsvRow;

// 2024 is the first year I started checking for and fixing anomalies.
public class Anomalies {

   public static void check(List<? extends TmpBaseCsvRow> allRegistrantRows) {

      boolean found = false;

      // Sort the list by Cart Number.
      Util.sortByCartNumber(allRegistrantRows);

      if (printCartsWithTooManyBoats(allRegistrantRows).size() != 0) {
         found = true;
      }

      if (printLateFeeExemptionsWithoutLateFee(allRegistrantRows)) {
         found = true;
      }

      // Too many!
//      if (printBoatsWithoutStickerNumber(allRegistrantRows)) {
//         found = true;
//      }

      // PWM - other checks

      // TODO: Crew / Alumni with Mooring Type != "No Boat"
      // PWM additional unit test - Crew with a Boat.
      // PWM additional unit test - Alumni with a Boat.

      // PWM Full or Young Adult with No Boat and no Club Boat add-on.... (Some are
      // legit, i.e. a friend, but most no...)
      // private static boolean printNoApparentBoatToSail(List<? extends
      // TmpBaseCsvRow> allRegistrantRows) {

      // TODO: View by Cart.... The "Full" and "Young Adult" Carts need to have either
      // a Boat, or Club Boat Access. --> Though, maybe some exceptions????? Suggest
      // crew membership???

      // TODO: “Adult sailing school $50 discount.” --> Detect use. Also track and
      // confirm each individual? (just like new member welcome, and mailing list...)
      // i.e. handled like the "TODO"/"Done" CSVs. So not in the Anomalies class....

      if (!found) {
         System.out.println("(No anomalies found.)");
         System.out.println();
      }
   }

   private static boolean printBoatsWithoutStickerNumber(List<? extends TmpBaseCsvRow> allRegistrantRows) {

      int found = 0;

      for (TmpBaseCsvRow loopMember : allRegistrantRows) {

         if (!loopMember.getBoatType().isBlank() && !loopMember.getBoatTypeOther().equals("No Boat")
               && loopMember.getStickerNumberAsInteger() == -1) {

            if (found == 0) {
               System.out.println("Boats without a Sticker Number:");
            }

            System.out.println(" - " + loopMember.getCartNumber() 
                  + " (" + loopMember.getEmail() + " " + loopMember.getFullName() + "): "
                  + loopMember.getBoatType() + " - " + loopMember.getBoatTypeOther() + " - "
                  + loopMember.getStickerNumber());
            found++;
         }
      }
      if (found > 0) {
         System.out.println("Total: " + found);
         System.out.println();
         return true;
      }

      return false;
   }

   /*
    * It's a little complicated, but the short version is that it's how we ensure
    * that the previous season's members who register late pay the late fee, but
    * new members do not pay the late fee.
    * 
    * The Late Fee is applied to all registrations after April 1st, except those
    * that have selected the New Member Initiation Fee.  (i.e. After April 1st all
    * registrations need to select either the Late Fee or the New Member Initiation
    * Fee.)
    * 
    * But the New Member Initiation Fee only applies to new Full members, not new
    * Young Adult members or new Crew members.  So new Young Adult and Crew members
    * must select the Late Fee.  The two Late Fee exemptions allow them to offset
    * the Late Fee, so new Young Adult and Crew members in effect pay $0 for
    * registering "late".
    * 
    * This should not ever have any findings, because if the form is set up
    * correctly the exemptions can't be selected until the Late Fee is mandatory.
    */
   private static boolean printLateFeeExemptionsWithoutLateFee(List<? extends TmpBaseCsvRow> allRegistrantRows) {

      boolean foundYoungAdult = false;
      for (TmpBaseCsvRow loopMember : allRegistrantRows) {
         if (loopMember.isNewYoungAdultLateFeeExemption() && !loopMember.isLateFee()) {
            if (!foundYoungAdult) {
               System.out.println("Young Adult LFE, without LF:");
            }

            System.out.println(" - " + loopMember.getEmail());
            foundYoungAdult = true;
         }
      }
      if (foundYoungAdult) {
         System.out.println();
      }

      boolean foundCrew = false;
      for (TmpBaseCsvRow loopMember : allRegistrantRows) {
         if (loopMember.isNewCrewLateFeeExemption() && !loopMember.isLateFee()) {
            if (!foundCrew) {
               System.out.println("Crew LFE, without LF:");
            }
            System.out.println(" - " + loopMember.getEmail());
            foundCrew = true;
         }
      }
      if (foundCrew) {
         System.out.println();
      }

      return foundYoungAdult || foundCrew;
   }

   // Print the report, but also return the string IDs of the flagged Carts.

   // PWM TODO: Detect paying for KCS, but then using that spot for a
   // FullPriceBoat. --> Start with a unit test that fails. Will probably
   // still need to be ONE method, because you need to know if the FULL 1
   // included boat counts towards a Sail vs. KCS.

   // PWM - another unit test --> Make sure the FINAL cart has an anomaly,
   // which tests "Evaluate the final cart."

   public static List<String> printCartsWithTooManyBoats(
         List<? extends TmpBaseCsvRow> allRegistrantRows) {

      List<String> tooManyBoatsCartNumbers = new ArrayList<String>();

      TmpBaseCsvRow currentCartMember = null;
      int cartBoatCount = 0;
      for (TmpBaseCsvRow loopMember : allRegistrantRows) {

         // Is there already a cart being evaluated?
         if (currentCartMember != null) {

            // Is loopMember in this Cart, or a new Cart?
            if (!currentCartMember.getCartNumber().equals(loopMember.getCartNumber())) {
               // New Cart, so evaluate the current cart.
               evaluateAndPrintIfSingleCartHasTooManyBoats(cartBoatCount, currentCartMember,
                     tooManyBoatsCartNumbers);

               // Reset count for the new Cart.
               cartBoatCount = 0;
            }
         }

         // Update current Cart/Member to this member, regardless of whether it's a new
         // Cart or not.
         currentCartMember = loopMember;

         // Increment the Cart's boat count if there's a boat attached to this Member.
         if (currentCartMember.isMooringTypeSail() || currentCartMember.isMooringTypeKCS()) {
            cartBoatCount++;
         }
      }

      // Evaluate the final cart.
      evaluateAndPrintIfSingleCartHasTooManyBoats(cartBoatCount, currentCartMember, tooManyBoatsCartNumbers);

      if (tooManyBoatsCartNumbers.size() != 0) {
         System.out.println("    Total: " + tooManyBoatsCartNumbers.size());
      }
      return tooManyBoatsCartNumbers;
   }

   // Returns the new boat count for the Cart.
   private static void evaluateAndPrintIfSingleCartHasTooManyBoats(int boatCount, TmpBaseCsvRow currentCartMember,
         List<String> tooManyBoatsCartNumbers) {
      // Note: This does not check if the Cart's privilege level is allowed to buy additional
      // boats, i.e. "Full" membership. But since 2024 the Zone4 form has been set up
      // to only allow "Full" memberships to purchase additional boats.
      if (boatCount > 1 + currentCartMember.getAdditionalBoatsSail() + currentCartMember.getAdditionalBoatsCKSup()) {

         // This Cart has too many boats.
         tooManyBoatsCartNumbers.add(currentCartMember.getCartNumber());

         // Print the header if this is the first one found.
         if (tooManyBoatsCartNumbers.size() == 1) {
            System.out.println("Carts with too many boats:");
         }

         System.out.println(" - " + currentCartMember.getCartNumber() + "  counted boats: " + boatCount
               + "  vs 1 + additional Sail: " + currentCartMember.getAdditionalBoatsSail() + " CKSup: "
               + currentCartMember.getAdditionalBoatsCKSup() + "  Privilege: "
               + currentCartMember.getMembershipPrivilege());
      }
   }
}
