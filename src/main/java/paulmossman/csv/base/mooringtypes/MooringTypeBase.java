package paulmossman.csv.base.mooringtypes;

public abstract class MooringTypeBase {

   public abstract String MOORING_TYPE_KEELBOAT();

   public String MOORING_TYPE_MONOHULL_DINGY() {
      return "Monohull";
   }

   public String MOORING_TYPE_CAT() {
      return "CAT";
   }

   public String MOORING_TYPE_KCS() {
      return "Kayak/Canoe/SUP";
   }

   public String MOORING_TYPE_NO_BOAT() {
      return "No Boat";
   }

   public String MOORING_TYPE_CLUB_BOAT_ONLY() { // Note: this option was removed in 2025
      return "Club Boat Only";
   }
}
