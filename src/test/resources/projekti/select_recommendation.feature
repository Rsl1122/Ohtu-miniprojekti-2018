Feature: Users can select a specific recommendation

  Scenario: user can select a recommendation to see its description
    Given    language has been selected
    And      some book recommendations have been created
    And      command select is selected
    When     existing recommendation id "1" is entered
    And      command return is entered
    And      the app processes the input
    Then     system will respond with "1. Reetta: Great Book, ISBN: 111122222, URL: -"
    Then     system will respond with "Description: hyva kirja"

  Scenario: user gets a warning message when trying to select a nonexisting recommendation
    Given   language has been selected
    And     some book recommendations have been created
    And     command select is selected
    When    nonexisting recommendation id "43" is entered
    And     command return is entered
    And     the app processes the input
    Then    system will respond with "No recommendation found"

  Scenario: user can edit a selected recommendation
    Given   language has been selected
    And     some book recommendations have been created
    And     command select is selected
    When    existing recommendation id "1" is entered
    And     command edit is entered
    And     new author "New Author" is entered
    And     new title "Greatest Book" is entered
    And     new ISBN "77777711133" is entered
    And     new url "http://www.faketestfaketestfaketesturl.com" is entered
    And     new description "new description" is entered
    And     affirmative response is given when asked for confirmation
    And     command return is entered
    And     the app processes the input
    Then    system will respond with "1. New Author: Greatest Book, ISBN: 77777711133, URL: http://www.faketestfaketestfaketesturl.com"
    Then     system will respond with "Description: new description"

  Scenario: user can delete selected recommendation
    Given language has been selected
    And   some book recommendations have been created
    And   command select is selected
    When  existing recommendation id "2" is entered
    And   command delete is selected
    And   affirmative response is given when asked for confirmation
    And   the app processes the input
    Then  system will respond with "recommendation successfully deleted"
