package exceptionHandling;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class JobPlatformGUI {
    

    private ArrayList<Company> companies = new ArrayList<>();
    private ArrayList<JobSeeker> jobSeekers = new ArrayList<>();
    private ArrayList<Job> jobs = new ArrayList<>();
    private ArrayList<Application> applications = new ArrayList<>();
    private ArrayList<Admin> admins = new ArrayList<>();
    
   
    private String currentUserType = "";
    private Object currentUser = null;

    private final String COMPANIES_FILE = "companies.txt";
    private final String SEEKERS_FILE = "seekers.txt";
    private final String JOBS_FILE = "jobs.txt";
    private final String APPLICATIONS_FILE = "applications.txt";
    private final String ADMINS_FILE = "admins.txt";
    

    private JFrame mainFrame;
    private JPanel currentPanel;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JobPlatformGUI().start();
        });
    }
    
    void start() {
     
        loadAllData();
    
        mainFrame = new JFrame("Job Platform");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        
        showWelcomeScreen();
        mainFrame.setVisible(true);
    }
    
 
    
    void showWelcomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        

        JLabel title = new JLabel("Welcome to Job Platform", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.green);
        panel.add(title, BorderLayout.NORTH);
        
    
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));
        
        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");
        JButton exitBtn = new JButton("Exit");
        
    
        for (JButton btn : new JButton[]{loginBtn, signupBtn, exitBtn}) {
            btn.setFont(new Font("Georgia", Font.PLAIN, 18));
            btn.setBackground(Color.LIGHT_GRAY);
        }
        
        
        loginBtn.addActionListener(e -> showLoginScreen());
        signupBtn.addActionListener(e -> showSignupScreen());
        exitBtn.addActionListener(e -> {
            saveAllData();
            System.exit(0);
            
        });
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(signupBtn);
        buttonPanel.add(exitBtn);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        changePanel(panel);
    }
    
    void showLoginScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(Color.green);
        panel.add(title, BorderLayout.NORTH);
        
  
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        
        formPanel.add(new JLabel("User Type:"));
        JComboBox<String> userTypeBox = new JComboBox<>(new String[]{"Admin", "Company", "Job Seeker"});
        formPanel.add(userTypeBox);
        
        formPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel(""));
        formPanel.add(new JLabel(""));
        
    
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");
        
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String userType = (String) userTypeBox.getSelectedItem();
            
            if (doLogin(username, password, userType)) {
                switch (userType) {
                    case "Admin": showAdminMenu(); break;
                    case "Company": showCompanyMenu(); break;
                    case "Job Seeker": showSeekerMenu(); break;
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Login failed!");
            }
        });
        
        backBtn.addActionListener(e -> showWelcomeScreen());
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(backBtn);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        changePanel(panel);
    }
    
    void showSignupScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Sign Up", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);
        
    
        JPanel mainFormPanel = new JPanel(new BorderLayout());
       
        JPanel typePanel = new JPanel(new FlowLayout());
        typePanel.add(new JLabel("Sign Up As:"));
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Company", "Job Seeker"});
        typePanel.add(typeBox);
        mainFormPanel.add(typePanel, BorderLayout.NORTH);
        
      
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        
        formPanel.add(new JLabel("Name:*"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Username:*"));
        JTextField usernameField = new JTextField();
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:*"));
        JPasswordField passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        formPanel.add(new JLabel("Email:*"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Phone:*"));
        JTextField phoneField = new JTextField();
        formPanel.add(phoneField);
        
        formPanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField();
        formPanel.add(addressField);
        
        formPanel.add(new JLabel("Extra Info:*"));
        JTextField extraField = new JTextField();
        formPanel.add(extraField);
        
        mainFormPanel.add(formPanel, BorderLayout.CENTER);
        

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");
        
        registerBtn.addActionListener(e -> {
            try {
                String type = (String) typeBox.getSelectedItem();
                String name = nameField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                String extra = extraField.getText().trim();
                
                // Validate required fields
                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || 
                    email.isEmpty() || phone.isEmpty() || extra.isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Please fill in all required fields (marked with *)", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate username length
                if (username.length() < 4) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Username must be at least 4 characters long", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate password strength
                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Password must be at least 6 characters long", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate email format
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Please enter a valid email address", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate phone number (basic validation)
                if (!phone.matches("\\d+")) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Phone number should contain only digits", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (phone.length() < 10) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Phone number should be at least 10 digits", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (type.equals("Company")) {
                    registerCompany(name, username, password, email, phone, address, extra);
                } else {
                    registerJobSeeker(name, username, password, email, phone, address, extra);
                }
                
                showWelcomeScreen();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "An error occurred during registration: " + ex.getMessage(),
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        backBtn.addActionListener(e -> showWelcomeScreen());
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(backBtn);
        
        panel.add(mainFormPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        changePanel(panel);
    }
    

    
    void showAdminMenu() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);
        
       
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));
        
        JButton viewCompaniesBtn = createMenuButton("View All Companies");
        JButton viewSeekersBtn = createMenuButton("View All Job Seekers");
        JButton viewJobsBtn = createMenuButton("View All Jobs");
        JButton deactivateCompanyBtn = createMenuButton("Deactivate/Reactivate Company");
        JButton deactivateSeekerBtn = createMenuButton("Deactivate/Reactivate Job Seeker");
        JButton resetPassBtn = createMenuButton("Reset Password");
        JButton logoutBtn = createMenuButton("Logout");
        
       
        viewCompaniesBtn.addActionListener(e -> showAllCompanies());
        viewSeekersBtn.addActionListener(e -> showAllSeekers());
        viewJobsBtn.addActionListener(e -> showAllJobs());
        
        deactivateCompanyBtn.addActionListener(e -> deactivateReactivateCompany());
        deactivateSeekerBtn.addActionListener(e -> deactivateReactivateSeeker());
        
        resetPassBtn.addActionListener(e -> resetPassword());
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            currentUserType = "";
            showWelcomeScreen();
        });
        
        buttonPanel.add(viewCompaniesBtn);
        buttonPanel.add(viewSeekersBtn);
        buttonPanel.add(viewJobsBtn);
        buttonPanel.add(deactivateCompanyBtn);
        buttonPanel.add(deactivateSeekerBtn);
        buttonPanel.add(resetPassBtn);
        buttonPanel.add(logoutBtn);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        changePanel(panel);
    }
    
    void deactivateReactivateCompany() {
        if (companies.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No companies!");
            return;
        }
        
        String[] companyList = new String[companies.size()];
        for (int i = 0; i < companies.size(); i++) {
            companyList[i] = companies.get(i).name + " - " + 
                           (companies.get(i).isActive ? "Active" : "Inactive");
        }
        
        String choice = (String) JOptionPane.showInputDialog(mainFrame,
                "Select company to deactivate/reactivate:", "Manage Company Status",
                JOptionPane.QUESTION_MESSAGE, null, companyList, companyList[0]);
        
        if (choice != null) {
            String companyName = choice.split(" - ")[0];
            for (Company c : companies) {
                if (c.name.equals(companyName)) {
                    String action = c.isActive ? "deactivate" : "reactivate";
                    int confirm = JOptionPane.showConfirmDialog(mainFrame,
                            "Are you sure you want to " + action + " this company?",
                            "Confirm", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        c.isActive = !c.isActive;
                        saveCompanies();
                        String message = c.isActive ? "Company reactivated!" : "Company deactivated!";
                        JOptionPane.showMessageDialog(mainFrame, message);
                    }
                    break;
                }
            }
        }
    }
    
    void deactivateReactivateSeeker() {
        if (jobSeekers.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No job seekers!");
            return;
        }
        
        String[] seekerList = new String[jobSeekers.size()];
        for (int i = 0; i < jobSeekers.size(); i++) {
            seekerList[i] = jobSeekers.get(i).name + " - " + 
                          (jobSeekers.get(i).isActive ? "Active" : "Inactive");
        }
        
        String choice = (String) JOptionPane.showInputDialog(mainFrame,
                "Select job seeker to deactivate/reactivate:", "Manage Job Seeker Status",
                JOptionPane.QUESTION_MESSAGE, null, seekerList, seekerList[0]);
        
        if (choice != null) {
            String seekerName = choice.split(" - ")[0];
            for (JobSeeker js : jobSeekers) {
                if (js.name.equals(seekerName)) {
                    String action = js.isActive ? "deactivate" : "reactivate";
                    int confirm = JOptionPane.showConfirmDialog(mainFrame,
                            "Are you sure you want to " + action + " this job seeker?",
                            "Confirm", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        js.isActive = !js.isActive;
                        saveJobSeekers();
                        String message = js.isActive ? "Job seeker reactivated!" : "Job seeker deactivated!";
                        JOptionPane.showMessageDialog(mainFrame, message);
                    }
                    break;
                }
            }
        }
    }
    
    
    void showCompanyMenu() {
        Company company = (Company) currentUser;
        
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Company: " + company.name, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(9, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));
        
        JButton postJobBtn = createMenuButton("Post New Job");
        JButton viewJobsBtn = createMenuButton("View My Jobs");
        JButton deleteJobBtn = createMenuButton("Delete Job");
        JButton viewApplicantsBtn = createMenuButton("View Applicants");
        JButton scheduleBtn = createMenuButton("Schedule Interview");
        JButton updateInfoBtn = createMenuButton("Update Company Info");
        JButton resetPassBtn = createMenuButton("Reset Password");
        JButton logoutBtn = createMenuButton("Logout");
        
        postJobBtn.addActionListener(e -> postJobDialog());
        viewJobsBtn.addActionListener(e -> showCompanyJobs());
        deleteJobBtn.addActionListener(e -> deleteJobDialog());
        viewApplicantsBtn.addActionListener(e -> showCompanyApplicants());
        scheduleBtn.addActionListener(e -> scheduleInterviewDialog());
        updateInfoBtn.addActionListener(e -> updateCompanyDialog());
        resetPassBtn.addActionListener(e -> resetPassword());
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            currentUserType = "";
            showWelcomeScreen();
        });
        
        buttonPanel.add(postJobBtn);
        buttonPanel.add(viewJobsBtn);
        buttonPanel.add(deleteJobBtn);
        buttonPanel.add(viewApplicantsBtn);
        buttonPanel.add(scheduleBtn);
        buttonPanel.add(updateInfoBtn);
        buttonPanel.add(resetPassBtn);
        buttonPanel.add(logoutBtn);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        changePanel(panel);
    }
    
    
    void showSeekerMenu() {
        JobSeeker seeker = (JobSeeker) currentUser;
        
        JPanel panel = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Welcome " + seeker.name, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));
        
        JButton viewJobsBtn = createMenuButton("View Available Jobs");
        JButton applyJobBtn = createMenuButton("Apply for Job");
        JButton viewHistoryBtn = createMenuButton("View Application History");
        JButton viewStatusBtn = createMenuButton("View Application Status");
        JButton updateInfoBtn = createMenuButton("Update Profile");
        JButton deactivateBtn = createMenuButton("Deactivate Account");
        JButton resetPassBtn = createMenuButton("Reset Password");
        JButton logoutBtn = createMenuButton("Logout");
        
        
        viewJobsBtn.addActionListener(e -> showAvailableJobs());
        applyJobBtn.addActionListener(e -> applyForJob());
        viewHistoryBtn.addActionListener(e -> showApplicationHistory());
        viewStatusBtn.addActionListener(e -> showApplicationStatus());
        updateInfoBtn.addActionListener(e -> updateSeekerDialog());
        deactivateBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure you want to deactivate your account?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                seeker.isActive = false;
                saveJobSeekers();
                currentUser = null;
                currentUserType = "";
                showWelcomeScreen();
            }
        });
        resetPassBtn.addActionListener(e -> resetPassword());
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            currentUserType = "";
            showWelcomeScreen();
        });
        
        // Add buttons
        buttonPanel.add(viewJobsBtn);
        buttonPanel.add(applyJobBtn);
        buttonPanel.add(viewHistoryBtn);
        buttonPanel.add(viewStatusBtn);
        buttonPanel.add(updateInfoBtn);
        buttonPanel.add(deactivateBtn);
        buttonPanel.add(resetPassBtn);
        buttonPanel.add(logoutBtn);
        
        panel.add(buttonPanel, BorderLayout.CENTER);
        changePanel(panel);
    }
    
    // =================== DIALOG METHODS ===================
    
    void postJobDialog() {
        Company company = (Company) currentUser;
        
        JDialog dialog = new JDialog(mainFrame, "Post New Job", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(mainFrame);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        
        formPanel.add(new JLabel("Job Title:*"));
        JTextField titleField = new JTextField();
        formPanel.add(titleField);
        
        formPanel.add(new JLabel("Description:*"));
        JTextArea descArea = new JTextArea(4, 30);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descArea);
        descScrollPane.setPreferredSize(new Dimension(400, 80));
        formPanel.add(descScrollPane);
        
        formPanel.add(new JLabel("Requirements:"));
        JTextField reqField = new JTextField();
        formPanel.add(reqField);
        
        formPanel.add(new JLabel("Salary:*"));
        JTextField salaryField = new JTextField();
        formPanel.add(salaryField);
        
        formPanel.add(new JLabel("Location:*"));
        JTextField locationField = new JTextField();
        formPanel.add(locationField);
        
        JButton postBtn = new JButton("Post");
        JButton cancelBtn = new JButton("Cancel");
        
        postBtn.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String description = descArea.getText().trim();
                String requirements = reqField.getText().trim();
                String salary = salaryField.getText().trim();
                String location = locationField.getText().trim();
                
                // Validate required fields
                if (title.isEmpty() || description.isEmpty() || salary.isEmpty() || location.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please fill in all required fields (marked with *)", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate salary format
                try {
                    // Check if salary contains only numbers or proper format
                    if (!salary.matches("\\d+")) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Salary should contain only numbers", 
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please enter a valid salary amount", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Job job = new Job("JOB" + (jobs.size() + 1), 
                        title,
                        description,
                        requirements,
                        salary,
                        location,
                        company.id);
                jobs.add(job);
                saveJobs();
                JOptionPane.showMessageDialog(dialog, "Job posted successfully!");
                dialog.dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "An error occurred while posting job: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(postBtn);
        buttonPanel.add(cancelBtn);
        
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    void deleteJobDialog() {
        Company company = (Company) currentUser;
        
        ArrayList<Job> companyJobs = new ArrayList<>();
        for (Job job : jobs) {
            if (job.companyId.equals(company.id)) {
                companyJobs.add(job);
            }
        }
        
        if (companyJobs.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No jobs to delete!");
            return;
        }
        
        String[] jobTitles = new String[companyJobs.size()];
        for (int i = 0; i < companyJobs.size(); i++) {
            jobTitles[i] = companyJobs.get(i).title;
        }
        
        String choice = (String) JOptionPane.showInputDialog(mainFrame,
                "Select job to delete:", "Delete Job",
                JOptionPane.QUESTION_MESSAGE, null, jobTitles, jobTitles[0]);
        
        if (choice != null) {
            int confirm = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure you want to delete this job?",
                    "Confirm", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                for (int i = 0; i < jobs.size(); i++) {
                    if (jobs.get(i).title.equals(choice) && 
                        jobs.get(i).companyId.equals(company.id)) {
                        String jobId = jobs.get(i).id;
                        jobs.remove(i);
                        
                
                        for (int j = applications.size() - 1; j >= 0; j--) {
                            if (applications.get(j).jobId.equals(jobId)) {
                                applications.remove(j);
                            }
                        }
                        
                        saveJobs();
                        saveApplications();
                        JOptionPane.showMessageDialog(mainFrame, "Job deleted!");
                        break;
                    }
                }
            }
        }
    }
    
    void scheduleInterviewDialog() {
        Company company = (Company) currentUser;
        

        StringBuilder sb = new StringBuilder();
        boolean hasApplicants = false;
        
        for (Job job : jobs) {
            if (job.companyId.equals(company.id)) {
                for (Application app : applications) {
                    if (app.jobId.equals(job.id)) {
                        JobSeeker seeker = getJobSeekerById(app.seekerId);
                        if (seeker != null) {
                            sb.append("Job: ").append(job.title).append("\n");
                            sb.append("Applicant: ").append(seeker.name).append("\n");
                            sb.append("Status: ").append(app.status).append("\n");
                            sb.append("---------\n");
                            hasApplicants = true;
                        }
                    }
                }
            }
        }
        
        if (!hasApplicants) {
            JOptionPane.showMessageDialog(mainFrame, "No applicants found!");
            return;
        }
        
        sb.append("\nEnter details to schedule interview:");
        String result = JOptionPane.showInputDialog(mainFrame, sb.toString());
        
        if (result != null && !result.trim().isEmpty()) {
          
            JOptionPane.showMessageDialog(mainFrame, "Interview scheduled");
        }
    }
    
    void updateCompanyDialog() {
        Company company = (Company) currentUser;
        
        JDialog dialog = new JDialog(mainFrame, "Update Company Info", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(mainFrame);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Name:*"));
        JTextField nameField = new JTextField(company.name);
        panel.add(nameField);
        
        panel.add(new JLabel("Email:*"));
        JTextField emailField = new JTextField(company.email);
        panel.add(emailField);
        
        panel.add(new JLabel("Phone:*"));
        JTextField phoneField = new JTextField(company.phone);
        panel.add(phoneField);
        
        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");
        
        updateBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                
                // Validate required fields
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please fill in all required fields (marked with *)", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate email format
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please enter a valid email address", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate phone number
                if (!phone.matches("\\d+")) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Phone number should contain only digits", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (phone.length() < 10) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Phone number should be at least 10 digits", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                company.name = name;
                company.email = email;
                company.phone = phone;
                saveCompanies();
                JOptionPane.showMessageDialog(dialog, "Info updated!");
                dialog.dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "An error occurred while updating: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    void updateSeekerDialog() {
        JobSeeker seeker = (JobSeeker) currentUser;
        
        JDialog dialog = new JDialog(mainFrame, "Update Profile", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(mainFrame);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Name:*"));
        JTextField nameField = new JTextField(seeker.name);
        panel.add(nameField);
        
        panel.add(new JLabel("Email:*"));
        JTextField emailField = new JTextField(seeker.email);
        panel.add(emailField);
        
        panel.add(new JLabel("Phone:*"));
        JTextField phoneField = new JTextField(seeker.phone);
        panel.add(phoneField);
        
        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");
        
        updateBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = phoneField.getText().trim();
                
                // Validate required fields
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please fill in all required fields (marked with *)", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate email format
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please enter a valid email address", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate phone number
                if (!phone.matches("\\d+")) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Phone number should contain only digits", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (phone.length() < 10) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Phone number should be at least 10 digits", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                seeker.name = name;
                seeker.email = email;
                seeker.phone = phone;
                saveJobSeekers();
                JOptionPane.showMessageDialog(dialog, "Profile updated!");
                dialog.dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "An error occurred while updating: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
 
    
    void showAllCompanies() {
        if (companies.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No companies registered!");
            return;
        }
        
        StringBuilder sb = new StringBuilder("All Companies:\n\n");
        for (Company c : companies) {
            sb.append("Name: ").append(c.name).append("\n");
            sb.append("Email: ").append(c.email).append("\n");
            sb.append("Phone: ").append(c.phone).append("\n");
            sb.append("Active: ").append(c.isActive ? "Yes" : "No").append("\n");
            sb.append("---------\n");
        }
        
        showTextDialog("Companies List", sb.toString());
    }
    
    void showAllSeekers() {
        if (jobSeekers.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No job seekers registered!");
            return;
        }
        
        StringBuilder sb = new StringBuilder("All Job Seekers:\n\n");
        for (JobSeeker js : jobSeekers) {
            sb.append("Name: ").append(js.name).append("\n");
            sb.append("Email: ").append(js.email).append("\n");
            sb.append("Qualifications: ").append(js.qualifications).append("\n");
            sb.append("Experience: ").append(js.experience).append(" years\n");
            sb.append("Active: ").append(js.isActive ? "Yes" : "No").append("\n");
            sb.append("---------\n");
        }
        
        showTextDialog("Job Seekers List", sb.toString());
    }
    
    void showAllJobs() {
        if (jobs.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No jobs posted!");
            return;
        }
        
        StringBuilder sb = new StringBuilder("All Jobs:\n\n");
        for (Job job : jobs) {
            Company company = getCompanyById(job.companyId);
            sb.append("Title: ").append(job.title).append("\n");
            sb.append("Company: ").append(company != null ? company.name : "Unknown").append("\n");
            sb.append("Location: ").append(job.location).append("\n");
            sb.append("Salary: ").append(job.salary).append("\n");
            sb.append("Active: ").append(job.isActive ? "Yes" : "No").append("\n");
            sb.append("---------\n");
        }
        
        showTextDialog("Jobs List", sb.toString());
    }
    
    void showCompanyJobs() {
        Company company = (Company) currentUser;
        
        StringBuilder sb = new StringBuilder("My Jobs:\n\n");
        boolean hasJobs = false;
        
        for (Job job : jobs) {
            if (job.companyId.equals(company.id)) {
                sb.append("Title: ").append(job.title).append("\n");
                sb.append("Description: ").append(job.description).append("\n");
                sb.append("Salary: ").append(job.salary).append("\n");
                sb.append("Location: ").append(job.location).append("\n");
                sb.append("---------\n");
                hasJobs = true;
            }
        }
        
        if (!hasJobs) {
            sb.append("No jobs posted yet!");
        }
        
        showTextDialog("My Jobs", sb.toString());
    }
    
    void showCompanyApplicants() {
        Company company = (Company) currentUser;
        
        StringBuilder sb = new StringBuilder("Applicants:\n\n");
        boolean hasApplicants = false;
        
        for (Job job : jobs) {
            if (job.companyId.equals(company.id)) {
                for (Application app : applications) {
                    if (app.jobId.equals(job.id)) {
                        JobSeeker seeker = getJobSeekerById(app.seekerId);
                        if (seeker != null) {
                            sb.append("Job: ").append(job.title).append("\n");
                            sb.append("Applicant: ").append(seeker.name).append("\n");
                            sb.append("Email: ").append(seeker.email).append("\n");
                            sb.append("Status: ").append(app.status).append("\n");
                            if (app.interviewDate != null) {
                                sb.append("Interview: ").append(app.interviewDate).append("\n");
                            }
                            sb.append("---------\n");
                            hasApplicants = true;
                        }
                    }
                }
            }
        }
        
        if (!hasApplicants) {
            sb.append("No applicants yet!");
        }
        
        showTextDialog("Applicants", sb.toString());
    }
    
    void showAvailableJobs() {
        JobSeeker seeker = (JobSeeker) currentUser;
        
        StringBuilder sb = new StringBuilder("Available Jobs:\n\n");
        boolean hasJobs = false;
        
        for (Job job : jobs) {
            if (job.isActive) {
                // Check if already applied
                boolean alreadyApplied = false;
                for (Application app : applications) {
                    if (app.jobId.equals(job.id) && app.seekerId.equals(seeker.id)) {
                        alreadyApplied = true;
                        break;
                    }
                }
                
                if (!alreadyApplied) {
                    Company company = getCompanyById(job.companyId);
                    sb.append("Title: ").append(job.title).append("\n");
                    sb.append("Company: ").append(company != null ? company.name : "Unknown").append("\n");
                    sb.append("Location: ").append(job.location).append("\n");
                    sb.append("Salary: ").append(job.salary).append("\n");
                    sb.append("Requirements: ").append(job.requirements).append("\n");
                    sb.append("---------\n");
                    hasJobs = true;
                }
            }
        }
        
        if (!hasJobs) {
            sb.append("No available jobs or you've applied to all!");
        }
        
        showTextDialog("Available Jobs", sb.toString());
    }
    
    void applyForJob() {
        JobSeeker seeker = (JobSeeker) currentUser;
        
    
        ArrayList<Job> availableJobs = new ArrayList<>();
        for (Job job : jobs) {
            if (job.isActive) {
                boolean alreadyApplied = false;
                for (Application app : applications) {
                    if (app.jobId.equals(job.id) && app.seekerId.equals(seeker.id)) {
                        alreadyApplied = true;
                        break;
                    }
                }
                if (!alreadyApplied) {
                    availableJobs.add(job);
                }
            }
        }
        
        if (availableJobs.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No jobs available to apply!");
            return;
        }
        
        String[] jobTitles = new String[availableJobs.size()];
        for (int i = 0; i < availableJobs.size(); i++) {
            jobTitles[i] = availableJobs.get(i).title;
        }
        
        String choice = (String) JOptionPane.showInputDialog(mainFrame,
                "Select job to apply:", "Apply for Job",
                JOptionPane.QUESTION_MESSAGE, null, jobTitles, jobTitles[0]);
        
        if (choice != null) {
            Job selectedJob = null;
            for (Job job : availableJobs) {
                if (job.title.equals(choice)) {
                    selectedJob = job;
                    break;
                }
            }
            	
            if (selectedJob != null) {
                Application app = new Application("APP" + (applications.size() + 1),
                        selectedJob.id, seeker.id);
                applications.add(app);
                saveApplications();
                JOptionPane.showMessageDialog(mainFrame, "Application submitted!");
            }
        }
    }
    
    void showApplicationHistory() {
        JobSeeker seeker = (JobSeeker) currentUser;
        
        StringBuilder sb = new StringBuilder("My Applications:\n\n");
        boolean hasApplications = false;
        
        for (Application app : applications) {
            if (app.seekerId.equals(seeker.id)) {
                Job job = getJobById(app.jobId);
                if (job != null) {
                    Company company = getCompanyById(job.companyId);
                    sb.append("Job: ").append(job.title).append("\n");
                    sb.append("Company: ").append(company != null ? company.name : "Unknown").append("\n");
                    sb.append("Applied: ").append(app.applicationDate).append("\n");
                    sb.append("Status: ").append(app.status).append("\n");
                    sb.append("---------\n");
                    hasApplications = true;
                }
            }
        }
        
        if (!hasApplications) {
            sb.append("No applications found!");
        }
        
        showTextDialog("Application History", sb.toString());
    }
    
    void showApplicationStatus() {
        JobSeeker seeker = (JobSeeker) currentUser;
        
        StringBuilder sb = new StringBuilder("Application Status:\n\n");
        boolean hasApplications = false;
        
        for (Application app : applications) {
            if (app.seekerId.equals(seeker.id)) {
                Job job = getJobById(app.jobId);
                if (job != null) {
                    sb.append("Job: ").append(job.title).append("\n");
                    sb.append("Status: ").append(app.status).append("\n");
                    if (app.interviewDate != null) {
                        sb.append("Interview: ").append(app.interviewDate).append("\n");
                    }
                    sb.append("---------\n");
                    hasApplications = true;
                }
            }
        }
        
        if (!hasApplications) {
            sb.append("No applications found!");
        }
        
        showTextDialog("Application Status", sb.toString());
    }
    
    void resetPassword() {
        JDialog dialog = new JDialog(mainFrame, "Reset Password", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(mainFrame);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Current Password:"));
        JPasswordField currentField = new JPasswordField();
        panel.add(currentField);
        
        panel.add(new JLabel("New Password:"));
        JPasswordField newField = new JPasswordField();
        panel.add(newField);
        
        panel.add(new JLabel("Confirm Password:"));
        JPasswordField confirmField = new JPasswordField();
        panel.add(confirmField);
        
        JCheckBox showPassCheckbox = new JCheckBox("Show Passwords");
        panel.add(new JLabel(""));
        panel.add(showPassCheckbox);
        
        showPassCheckbox.addActionListener(e -> {
            char echoChar = showPassCheckbox.isSelected() ? (char) 0 : '*';
            currentField.setEchoChar(echoChar);
            newField.setEchoChar(echoChar);
            confirmField.setEchoChar(echoChar);
        });
        
        JButton resetBtn = new JButton("Reset");
        JButton cancelBtn = new JButton("Cancel");
        
        resetBtn.addActionListener(e -> {
            try {
                String current = new String(currentField.getPassword());
                String newPass = new String(newField.getPassword());
                String confirm = new String(confirmField.getPassword());
                
                // Validate password strength
                if (newPass.length() < 6) {
                    JOptionPane.showMessageDialog(dialog, 
                        "New password must be at least 6 characters long", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
    
                boolean valid = false;
                if (currentUser instanceof Admin) {
                    Admin admin = (Admin) currentUser;
                    valid = admin.password.equals(current);
                } else if (currentUser instanceof Company) {
                    Company company = (Company) currentUser;
                    valid = company.password.equals(current);
                } else if (currentUser instanceof JobSeeker) {
                    JobSeeker seeker = (JobSeeker) currentUser;
                    valid = seeker.password.equals(current);
                }
                
                if (!valid) {
                    JOptionPane.showMessageDialog(dialog, "Wrong current password!");
                    return;
                }
                
                if (!newPass.equals(confirm)) {
                    JOptionPane.showMessageDialog(dialog, "Passwords don't match!");
                    return;
                }
                
                
                if (currentUser instanceof Admin) {
                    ((Admin) currentUser).password = newPass;
                    saveAdmins();
                } else if (currentUser instanceof Company) {
                    ((Company) currentUser).password = newPass;
                    saveCompanies();
                } else if (currentUser instanceof JobSeeker) {
                    ((JobSeeker) currentUser).password = newPass;
                    saveJobSeekers();
                }
                
                JOptionPane.showMessageDialog(dialog, "Password reset successfully!");
                dialog.dispose();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "An error occurred while resetting password: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(resetBtn);
        buttonPanel.add(cancelBtn);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    

    
    JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(Color.LIGHT_GRAY);
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }
    
    void changePanel(JPanel newPanel) {
        if (currentPanel != null) {
            mainFrame.remove(currentPanel);
        }
        currentPanel = newPanel;
        mainFrame.add(currentPanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    void showTextDialog(String title, String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(mainFrame, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
  
    
    boolean doLogin(String username, String password, String userType) {
        try {
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Username and password are required!", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            switch (userType) {
                case "Admin":
                    for (Admin admin : admins) {
                        if (admin.username.equals(username) && admin.password.equals(password)) {
                            currentUser = admin;
                            currentUserType = "Admin";
                            return true;
                        }
                    }
                    break;
                    
                case "Company":
                    for (Company company : companies) {
                        if (company.username.equals(username) && company.password.equals(password)) {
                            if (company.isActive) {
                                currentUser = company;
                                currentUserType = "Company";
                                return true;
                            } else {
                                JOptionPane.showMessageDialog(mainFrame, "Account is deactivated!");
                                return false;
                            }
                        }
                    }
                    break;
                    
                case "Job Seeker":
                    for (JobSeeker seeker : jobSeekers) {
                        if (seeker.username.equals(username) && seeker.password.equals(password)) {
                            if (seeker.isActive) {
                                currentUser = seeker;
                                currentUserType = "Job Seeker";
                                return true;
                            } else {
                                JOptionPane.showMessageDialog(mainFrame, "Account is deactivated!");
                                return false;
                            }
                        }
                    }
                    break;
            }
            return false;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Login error: " + e.getMessage(),
                "Login Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    void registerCompany(String name, String username, String password, String email,
                        String phone, String address, String extra) {
        try {
            
            for (Company c : companies) {
                if (c.username.equals(username)) {
                    JOptionPane.showMessageDialog(mainFrame, "Username already exists!");
                    return;
                }
            }
            
            Company company = new Company("COMP" + (companies.size() + 1),
                    name, username, password, email, phone, address, 0, extra);
            companies.add(company);
            saveCompanies();
            JOptionPane.showMessageDialog(mainFrame, "Company registered successfully!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error registering company: " + e.getMessage(),
                "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    void registerJobSeeker(String name, String username, String password, String email,
                          String phone, String address, String extra) {
        try {
            
            for (JobSeeker js : jobSeekers) {
                if (js.username.equals(username)) {
                    JOptionPane.showMessageDialog(mainFrame, "Username already exists!");
                    return;
                }
            }
            
            
            int experience = 0;
            try {
                experience = Integer.parseInt(extra);
                if (experience < 0) {
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Experience cannot be negative", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
               
            }
            
            JobSeeker seeker = new JobSeeker("JS" + (jobSeekers.size() + 1),
                    name, username, password, email, phone, address, extra, experience, 0, "");
            jobSeekers.add(seeker);
            saveJobSeekers();
            JOptionPane.showMessageDialog(mainFrame, "Job seeker registered successfully!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(mainFrame, 
                "Error registering job seeker: " + e.getMessage(),
                "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    Company getCompanyById(String id) {
        for (Company c : companies) {
            if (c.id.equals(id)) {
                return c;
            }
        }
        return null;
    }
    
    JobSeeker getJobSeekerById(String id) {
        for (JobSeeker js : jobSeekers) {
            if (js.id.equals(id)) {
                return js;
            }
        }
        return null;
    }
    
    Job getJobById(String id) {
        for (Job job : jobs) {
            if (job.id.equals(id)) {
                return job;
            }
        }
        return null;
    }
    

    
    void loadAllData() {
        loadAdmins();
        loadCompanies();
        loadJobSeekers();
        loadJobs();
        loadApplications();
    }
    
    void saveAllData() {
        saveAdmins();
        saveCompanies();
        saveJobSeekers();
        saveJobs();
        saveApplications();
    }
    
    void loadAdmins() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ADMINS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    admins.add(new Admin(parts[0], parts[1], parts[2]));
                }
            }
            reader.close();
        } catch (IOException e) {
            admins.add(new Admin("admin", "admin123", "admin@jobplatform.com"));
            saveAdmins();
        } catch (Exception e) {
            System.out.println("Error loading admins: " + e.getMessage());
        }
    }
    
    void loadCompanies() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(COMPANIES_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    Company c = new Company(parts[0], parts[1], parts[2], parts[3],
                            parts[4], parts[5], parts[6], 0, parts[7]);
                    if (parts.length > 8) {
                        c.isActive = Boolean.parseBoolean(parts[8]);
                    }
                    companies.add(c);
                }
            }
            reader.close();
        } catch (IOException e) {
            
        } catch (Exception e) {
            System.out.println("Error loading companies: " + e.getMessage());
        }
    }
    
    void loadJobSeekers() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SEEKERS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    int experience = 0;
                    try {
                        experience = Integer.parseInt(parts[8]);
                    } catch (NumberFormatException e) {
                        experience = 0;
                    }
                    
                    JobSeeker js = new JobSeeker(parts[0], parts[1], parts[2], parts[3],
                            parts[4], parts[5], parts[6], parts[7], experience, 0, "");
                    if (parts.length > 9) {
                        js.isActive = Boolean.parseBoolean(parts[9]);
                    }
                    jobSeekers.add(js);
                }
            }
            reader.close();
        } catch (IOException e) {
            
        } catch (Exception e) {
            System.out.println("Error loading job seekers: " + e.getMessage());
        }
    }
    
    void loadJobs() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(JOBS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Job j = new Job(parts[0], parts[1], parts[2], parts[3],
                            parts[4], parts[5], parts[6]);
                    if (parts.length > 7) {
                        j.isActive = Boolean.parseBoolean(parts[7]);
                    }
                    jobs.add(j);
                }
            }
            reader.close();
        } catch (IOException e) {
        
        } catch (Exception e) {
            System.out.println("Error loading jobs: " + e.getMessage());
        }
    }
    
    void loadApplications() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(APPLICATIONS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    Application app = new Application(parts[0], parts[1], parts[2]);
                    app.status = parts[3];
                    
                    if (parts.length > 4 && !parts[4].equals("null")) {
                        try {
                            app.interviewDate = LocalDate.parse(parts[4]);
                        } catch (Exception e) {
                            app.interviewDate = null;
                        }
                    }
                    
                    if (parts.length > 5) {
                        try {
                            app.applicationDate = LocalDate.parse(parts[5]);
                        } catch (Exception e) {
                            app.applicationDate = LocalDate.now();
                        }
                    }
                    
                    applications.add(app);
                }
            }
            reader.close();
        } catch (IOException e) {
        
        } catch (Exception e) {
            System.out.println("Error loading applications: " + e.getMessage());
        }
    }
    
    void saveAdmins() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(ADMINS_FILE));
            for (Admin a : admins) {
                writer.write(a.username + "," + a.password + "," + a.email);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving admins: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error saving admins: " + e.getMessage());
        }
    }
    
    void saveCompanies() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(COMPANIES_FILE));
            for (Company c : companies) {
                writer.write(c.id + "," + c.name + "," + c.username + "," + c.password + "," +
                        c.email + "," + c.phone + "," + c.address + "," + c.secretAnswer + "," + c.isActive);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving companies: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error saving companies: " + e.getMessage());
        }
    }
    
    void saveJobSeekers() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(SEEKERS_FILE));
            for (JobSeeker js : jobSeekers) {
                writer.write(js.id + "," + js.name + "," + js.username + "," + js.password + "," +
                        js.email + "," + js.phone + "," + js.address + "," + 
                        js.qualifications + "," + js.experience + "," + js.isActive);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving job seekers: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error saving job seekers: " + e.getMessage());
        }
    }
    
    void saveJobs() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(JOBS_FILE));
            for (Job j : jobs) {
                writer.write(j.id + "," + j.title + "," + j.description + "," + j.requirements + "," +
                        j.salary + "," + j.location + "," + j.companyId + "," + j.isActive);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving jobs: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error saving jobs: " + e.getMessage());
        }
    }
    
    void saveApplications() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(APPLICATIONS_FILE));
            for (Application app : applications) {
                String interviewStr = (app.interviewDate != null) ? app.interviewDate.toString() : "null";
                String appDateStr = (app.applicationDate != null) ? app.applicationDate.toString() : LocalDate.now().toString();
                writer.write(app.id + "," + app.jobId + "," + app.seekerId + "," + 
                        app.status + "," + interviewStr + "," + appDateStr);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving applications: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error saving applications: " + e.getMessage());
        }
    }
}



class Admin {
    String username;
    String password;
    String email;
    
    Admin(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}

class Company {
    String id;
    String name;
    String username;
    String password;
    String email;
    String phone;
    String address;
    int secretQuestionIndex;
    String secretAnswer;
    boolean isActive = true;
    
    Company(String id, String name, String username, String password, String email,
            String phone, String address, int secretQuestionIndex, String secretAnswer) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.secretQuestionIndex = secretQuestionIndex;
        this.secretAnswer = secretAnswer;
    }
}

class JobSeeker {
    String id;
    String name;
    String username;
    String password;
    String email;
    String phone;
    String address;
    String qualifications;
    int experience;
    int secretQuestionIndex;
    String secretAnswer;
    boolean isActive = true;
    
    JobSeeker(String id, String name, String username, String password, String email,
              String phone, String address, String qualifications, int experience,
              int secretQuestionIndex, String secretAnswer) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.qualifications = qualifications;
        this.experience = experience;
        this.secretQuestionIndex = secretQuestionIndex;
        this.secretAnswer = secretAnswer;
    }
}

class Job {
    String id;
    String title;
    String description;
    String requirements;
    String salary;
    String location;
    String companyId;
    boolean isActive = true;
    
    Job(String id, String title, String description, String requirements,
        String salary, String location, String companyId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.salary = salary;
        this.location = location;
        this.companyId = companyId;
    }
}

class Application {
    String id;
    String jobId;
    String seekerId;
    String status = "Pending";
    LocalDate applicationDate;
    LocalDate interviewDate;
    
    Application(String id, String jobId, String seekerId) {
        this.id = id;
        this.jobId = jobId;
        this.seekerId = seekerId;
        this.applicationDate = LocalDate.now();
    }
}