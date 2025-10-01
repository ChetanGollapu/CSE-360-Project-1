package guiFirstAdmin;

import java.sql.SQLException;

import database.Database;
import entityClasses.User;
import javafx.stage.Stage;
import passwordPopUpWindow.Model;
import passwordPopUpWindow.UserNameRecognizer;

public class ControllerFirstAdmin {
    /*-********************************************************************************************
     * Controller attributes
     ********************************************************************************************-*/
    private static String adminUsername = "";
    private static String adminPassword1 = "";
    private static String adminPassword2 = "";
    protected static Database theDatabase = applicationMain.FoundationsMain.database;

    /*-********************************************************************************************
     * UI actions
     ********************************************************************************************-*/

    protected static void setAdminUsername() {
        adminUsername = ViewFirstAdmin.text_AdminUsername.getText();
        if (adminUsername == null) adminUsername = "";
    }

    protected static void setAdminPassword1() {
        adminPassword1 = ViewFirstAdmin.text_AdminPassword1.getText();
        if (adminPassword1 == null) adminPassword1 = "";
        ViewFirstAdmin.label_PasswordsDoNotMatch.setText("");
    }

    protected static void setAdminPassword2() {
        adminPassword2 = ViewFirstAdmin.text_AdminPassword2.getText();
        if (adminPassword2 == null) adminPassword2 = "";
        ViewFirstAdmin.label_PasswordsDoNotMatch.setText("");
    }

    /**
     * Validates username & password via FSMs, enforces match, registers user, navigates.
     */
    @SuppressWarnings("unused")
    protected static void doSetupAdmin(Stage ps, int r) {
        // Normalize input (trimming is allowed and sensible)
        String uname = adminUsername == null ? "" : adminUsername.trim();

        // Guard 1: must start with a letter (fast UX check; FSM will enforce too)
        if (uname.isEmpty() || !Character.isLetter(uname.charAt(0))) {
            ViewFirstAdmin.label_PasswordsDoNotMatch.setText(
                "A UserName must start with an alphabetic character (A-Z, a-z).");
            return;
        }

        // FSM validation: username
        String usernameValidation = UserNameRecognizer.checkForValidUserName(uname);
        if (!usernameValidation.isEmpty()) {
            ViewFirstAdmin.label_PasswordsDoNotMatch.setText(
                usernameValidation.replace("\n*** ERROR *** ", "").replace("\n", " ").trim());
            return;
        }

        // FSM validation: password (Model enforces 8–32 chars + categories)
        String pwd1 = adminPassword1 == null ? "" : adminPassword1;
        String passwordValidation = Model.evaluatePassword(pwd1);
        if (!passwordValidation.isEmpty()) {
            ViewFirstAdmin.label_PasswordsDoNotMatch.setText(passwordValidation);
            return;
        }

        // Passwords must match
        String pwd2 = adminPassword2 == null ? "" : adminPassword2;
        if (!pwd1.equals(pwd2)) {
            ViewFirstAdmin.text_AdminPassword1.setText("");
            ViewFirstAdmin.text_AdminPassword2.setText("");
            ViewFirstAdmin.label_PasswordsDoNotMatch.setText(
                "The two passwords must match. Please try again!");
            return;
        }

        // Create and register user
        User user = new User(uname, pwd1, "", "", "", "", "", true, false, false);
        try {
            theDatabase.register(user);
        } catch (SQLException e) {
            System.err.println("*** ERROR *** Database error trying to register a user: " + e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }

        // Navigate to User Update Page
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewFirstAdmin.theStage, user);
    }

    protected static void performQuit() {
        System.out.println("Perform Quit");
        System.exit(0);
    }
}
