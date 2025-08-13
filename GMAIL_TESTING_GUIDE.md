# How to Test Email Functionality with Gmail

Using a real Gmail account is a great way to see the end-to-end functionality. However, it requires a special security setup with Google. You **cannot** use your regular Google password in the application. Instead, you must generate a unique **App Password**.

Here are the detailed steps:

### 1. Enable 2-Step Verification (If Not Already Enabled)

An App Password can only be created if 2-Step Verification is active on your Google account.

1.  Go to your Google Account security settings: `https://myaccount.google.com/security`
2.  Look for the "How you sign in to Google" section.
3.  If **2-Step Verification** is "Off", you must click on it and follow the on-screen instructions to enable it. You cannot proceed without this step.

### 2. Generate a Google App Password

Once 2-Step Verification is enabled, you can create the special password for this application.

1.  Go directly to the App Passwords page: `https://myaccount.google.com/apppasswords`
2.  You may be asked to sign in again.
3.  At the bottom, under "Select the app and device you want to generate the app password for":
    *   Click on **Select app** and choose **Mail**.
    *   Click on **Select device** and choose **Other (Custom name)**.
4.  A text box will appear. Give it a descriptive name like `GaleShapley Test App` and click **GENERATE**.
5.  Google will display a **16-character password** in a yellow box (e.g., `xxxx xxxx xxxx xxxx`).
6.  **Copy this 16-character password immediately.** This is the password you will use in the next step.

### 3. Configure the Application

1.  Open the `gale.shapley/src/main/resources/application.properties` file.
2.  Update the email configuration section as follows:

    ```properties
    # --- Gmail SMTP Configuration ---
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=your.email@gmail.com
    spring.mail.password=your-16-character-app-password
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true

    # The email address where the results will be sent
    module.leader.email=recipient.email@example.com
    ```

3.  **Replace the placeholder values**:
    *   `your.email@gmail.com`: Your full Gmail address.
    *   `your-16-character-app-password`: The 16-character password you copied from Google (without the spaces).
    *   `recipient.email@example.com`: The address where you want to receive the test email.

### 4. Run and Test

1.  **Restart the Spring Boot application** to apply the new settings.
2.  In Postman, send a `POST` request to `http://localhost:8082/match/email`.
3.  Check the recipient's email inbox. You should find an email from your Gmail account with the subject "Gale-Shapley Matching Results" and the CSV and PDF attachments.
