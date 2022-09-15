package com.svydovets.bibirnate.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SqlGenerator {

    public static String getRandomFirstName() {
        var firstNames = getFirstNames();
        var rand = new Random();
        return firstNames.get(rand.nextInt(firstNames.size()));
    }

    public static String getRandomLastName() {
        var endsEmail = getEndsOfEmail();
        var rand = new Random();
        return endsEmail.get(rand.nextInt(endsEmail.size()));
    }

    public static String getRandomEmailEnd() {
        var lastNames = getLastNames();
        var rand = new Random();
        return lastNames.get(rand.nextInt(lastNames.size()));
    }

    public static int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
          .findFirst()
          .getAsInt();
    }

    public static List<String> generateInsertQueriesToPersons(int amount) {
        List<String> result = new ArrayList<>();
        var rand = new Random();
        var firstNames = getFirstNames();
        var lastNames = getLastNames();
        var endsEmail = getEndsOfEmail();

        for (int i = 0; i < amount; i++) {
            var firstName = firstNames.get(rand.nextInt(firstNames.size()));
            var lastName = lastNames.get(rand.nextInt(lastNames.size()));
            var age = getRandomNumberUsingInts(15, 85);
            var randomInt = getRandomNumberUsingInts(1, 99);
            var email = String.format("%s%s.%s_%s%s", firstName.toLowerCase(Locale.ROOT), randomInt,
              lastName.toLowerCase(), age, endsEmail.get(rand.nextInt(endsEmail.size())));

            result.add(
              String.format("insert into persons (first_name, last_name, email, age) values ('%s', '%s', '%s', %s);",
                firstName, lastName, email, age));
        }

        return result;
    }

    public static List<String> generateInsertQueriesToNotes(int amount) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            var randomInt = getRandomNumberUsingInts(2, 9000);
            result.add(
              String.format("insert into notes (body, person_id) values ('%s', %s);", "note " + randomInt , randomInt));
        }

        return result;
    }

    private static List<String> getFirstNames() {
        return List.of("William", "Abraham", "George", "Thomas", "Charles", "Karl", "Julius", "Martin",
          "Albert", "Christopher", "Isaac", "Theodore", "Wolfgang", "Ludwig", "Leonardo", "Carl", "Ronald", "Benjamin",
          "Benjamin", "Winston", "Genghis", "James", "Friedrich", "Alexander", "Galileo", "Sigmund", "Mohandas",
          "Woodrow", "Johann", "Oliver", "James", "Gautama", "Mark", "Edgar", "Allan", "Joseph", "Adam", "David",
          "Immanuel", "Saint", "Andrew", "Pol", "Elvis", "William", "Augustine", "Vincent", "Nicolaus", "Robert",
          "Oscar", "Francis", "Louis", "Arthur", "Nikola", "Paul", "Harry", "Joan", "Dante", "Otto", "Grover");
    }

    private static List<String> getLastNames() {
        return List.of("Shakespeare", "Lincoln", "Washington", "Jefferson", "Darwin", "Marx", "Caesar", "Luther",
          "Einstein", "Columbus", "Newton", "Roosevelt", "Mozart", "Beethoven", "Vinci", "Linnaeus", "Reagan",
          "Dickens", "Franklin", "Churchill", "Khan", "England", "Nietzsche", "Roosevelt", "Freud", "Hamilton",
          "Gandhi", "Wilson", "Bach", "Galilei", "Cromwell", "Madison", "Buddha", "Twain", "Poe", "Smith", "Kant",
          "Cook", "Adams", "Peter", "Jackson", "Presley", "Conqueror", "Kennedy", "Hippo", "Gogh", "Copernicus", "Lee",
          "Wilde", "Cicero", "Rousseau", "Bacon", "Nixon", "Goethe", "Aquinas", "Tesla", "Descartes", "Truman",
          "Alighieri", "Bismarck", "Calvin", "Locke", "Cleveland");
    }

    private static List<String> getEndsOfEmail() {
        return List.of("@gmail.com", "@yahoo.com", "@ukr.net", "@globallogic.com", "@epam.com", "@test.com",
          "@olx.ua", "@president.gov.ua", "@gov.ua", "@hp.com", "@genesis.com", "@dell.com", "@nix.ua");
    }

}
