package paulmossman.csv_output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import paulmossman.csv.base.CsvRow2025toFuture;
import paulmossman.csv.y2025.CsvAllRegistrants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CsvKeelboatListTest {

   private final String adultAgeGroup;
   private final String youthAgeGroup;
   private final String keelboatMooringType;
   private final String noBoatMooringType;

   CsvKeelboatListTest() {
      CsvRow2025toFuture ref = CsvAllRegistrants.builder().build();
      adultAgeGroup = ref.getAdultAgeGroups().stream().findFirst().get();
      youthAgeGroup = ref.getYouthAgeGroups().stream().findFirst().get();
      keelboatMooringType = ref.MOORING_TYPE_KEELBOAT();
      noBoatMooringType = ref.MOORING_TYPE_NO_BOAT();
   }

   private CsvAllRegistrants registrant(String cartNumber, String email, String firstName, String lastName, String ageGroup,
         String mooringType) {
      return registrant(cartNumber, email, firstName, lastName, ageGroup, mooringType, "", "");
   }

   private CsvAllRegistrants registrant(String cartNumber, String email, String firstName, String lastName, String ageGroup,
         String mooringType, String boatType, String boatTypeOther) {
      return CsvAllRegistrants.builder()
            .cartNumber(cartNumber)
            .email(email)
            .firstName(firstName)
            .lastName(lastName)
            .ageGroup(ageGroup)
            .mooringType(mooringType)
            .boatType(boatType)
            .boatTypeOther(boatTypeOther)
            .build();
   }

   @Test
   void generateTodosIncludesAllAdultsFromKeelboatRegistration(@TempDir Path tempDir) throws Exception {
      List<CsvAllRegistrants> allRegistrantRows = List.of(
            registrant("cart-keel", "owner@example.com", "Owner", "One", adultAgeGroup, keelboatMooringType, "CS22",
                  "Blue hull"),
            registrant("cart-keel", "partner@example.com", "Partner", "One", adultAgeGroup, noBoatMooringType),
            registrant("cart-keel", "youth@example.com", "Youth", "One", youthAgeGroup, noBoatMooringType),
            registrant("cart-second", "second@example.com", "Second", "Skipper", adultAgeGroup, keelboatMooringType,
                  "Shark", ""),
            registrant("cart-other", "other@example.com", "Other", "Boat", adultAgeGroup, noBoatMooringType));

      String dataDir = tempDir.toString() + "/";

      CsvKeelboatList.builder().build().generateTodos(allRegistrantRows, dataDir);

      List<String> todoLines = Files.readAllLines(tempDir.resolve("KeelboatList-todo.csv"));
      String fullText = Files.readString(tempDir.resolve("KeelboatList-Full.txt"));

      assertEquals(3, todoLines.size());
      assertTrue(todoLines.stream().anyMatch(line -> line.contains("cart-keel,owner@example.com,Owner,One")));
      assertTrue(todoLines.stream().anyMatch(line -> line.contains("cart-keel,partner@example.com,Partner,One")));
      assertTrue(todoLines.stream().anyMatch(line -> line.contains("cart-second,second@example.com,Second,Skipper")));
      assertTrue(fullText.contains(" - CS22 Blue hull (Cart #: cart-keel)"));
      assertTrue(fullText.contains("   - Owner One (owner@example.com)"));
      assertTrue(fullText.contains("   - Partner One (partner@example.com)"));
      assertTrue(fullText.contains(" - Shark (Cart #: cart-second)"));
      assertTrue(fullText.contains("   - Second Skipper (second@example.com)"));
   }
}
