package com.csc3402.lab.formlogin.controller;

import com.csc3402.lab.formlogin.model.Group;
import com.csc3402.lab.formlogin.model.Transaction;
import com.csc3402.lab.formlogin.model.User;
import com.csc3402.lab.formlogin.repository.GroupRepository;
import com.csc3402.lab.formlogin.repository.UserRepository;
import com.csc3402.lab.formlogin.service.GroupService;
import com.csc3402.lab.formlogin.service.TransactionService;
import com.csc3402.lab.formlogin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupService groupService;

    private final GroupRepository groupRepository;
    private final TransactionService transactionService;

    public UserController(UserService userService, UserRepository userRepository, GroupService groupService, GroupRepository groupRepository, TransactionService transactionService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.transactionService = transactionService;
    }

    //    ---------     DASHBOARD     ----------     //

    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        List<Group> userGroups = groupService.findByUser(user);

        // Get all transactions for the user
        List<Transaction> allTransactions = new ArrayList<>();
        for (Group group : userGroups) {
            List<Transaction> transactions = transactionService.listTransactionsByBudgetId(group.getBudgetId());
            allTransactions.addAll(transactions);
        }

        // Get the current month
        String currentMonth = getCurrentMonth();

        // Filter transactions for the current month
        List<Transaction> currentMonthTransactions = filterTransactionsByMonth(allTransactions, currentMonth);

        // Get the latest transactions for the current month
        List<Transaction> latestTransactions = getLatestTransactions(currentMonthTransactions, 3);

        // Fetch expenses by category for the current month
        Map<String, Double> expensesByCategory = currentMonthTransactions.stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getGroup().getCategory(), // Assuming Group has getCategory() method
                        Collectors.summingDouble(Transaction::getAmount)
                ));


        // Prepare data for chart.js
        List<String> categories = new ArrayList<>(expensesByCategory.keySet());
        List<Double> expenses = new ArrayList<>(expensesByCategory.values());

        // Handle total income
        double totalIncome = user.getTotamount() != null ? user.getTotamount().doubleValue() : 0.0;

        double totalExpenses = currentMonthTransactions.stream().mapToDouble(Transaction::getAmount).sum();
        double remainingAmount = totalIncome - totalExpenses;

        model.addAttribute("user", user);
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("remainingAmount", remainingAmount);
        model.addAttribute("transactions", latestTransactions); // Displaying latest transactions for the current month
        model.addAttribute("categories", categories); // Categories for the chart
        model.addAttribute("expenses", expenses); // Expenses for the chart
        model.addAttribute("categoryExpenses", expensesByCategory);

        return "dashboard";
    }
    private List<Transaction> filterTransactionsByMonth(List<Transaction> transactions, String month) {
        return transactions.stream()
                .filter(transaction -> isMonthInRange(transaction, month))
                .collect(Collectors.toList());
    }

    private List<Transaction> getLatestTransactions(List<Transaction> transactions, int count) {
        return transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed()) // Sort by date descending
                .limit(count) // Limit to latest three transactions
                .collect(Collectors.toList());
    }

    private String getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return currentDate.format(formatter);
    }



    //    ---------     PROFILE     ----------     //
    @GetMapping("/profile")
    public String userProfile(Model model) {

        // Get the logged-in user's email from the security context
        String email = getCurrentUserEmail();

        // Find the user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login?logout"; // Updating email logs user out
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateUserProfile(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {

        // Check if the new email already exists in the database
        User existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail != null && !existingUserByEmail.getUserId().equals(user.getUserId())) {
            return "redirect:/user/profile?error";
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "profile";
        }

        // Update user details in the database
        userService.updateUser(user);
        return "redirect:/user/profile?success";
    }

    //    ---------     TRANSACTION     ----------     //
    @GetMapping("/transaction")
    public String getTransactions(Model model) {
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login?logout"; // Log out if the user is not found
        }
        List<Group> groups = groupService.listGroupsByUserId(user.getUserId());
        List<Group> transactionGroup = groupRepository.findByUsers(user);

        List<Transaction> allTransactions = new ArrayList<>();
        for (Group group : transactionGroup) {
            List<Transaction> transactions = transactionService.listTransactionsByBudgetId(group.getBudgetId());
            allTransactions.addAll(transactions);
        }
        model.addAttribute("groups", groups);
        model.addAttribute("allTransaction", allTransactions);
        model.addAttribute("transaction", new Transaction());
        return "transaction";
    }

    @GetMapping("/transaction/category")
    @ResponseBody
    public List<Transaction> getTransactionData(@RequestParam("month") String month) {
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return Collections.emptyList();
        }
        List<Group> transactionGroup = groupRepository.findByUsers(user);

        List<Transaction> allTransactions = new ArrayList<>();
        for (Group group : transactionGroup) {
            List<Transaction> transactions = transactionService.listTransactionsByBudgetId(group.getBudgetId());
            allTransactions.addAll(transactions);
        }

        // If month is "00", return all groups without filtering
        if ("00".equals(month)) {
            return allTransactions;
        }

        // Filter groups based on the month
        List<Transaction> filteredTransactions = allTransactions.stream()
                .filter(transactions -> isMonthInRange(transactions, month))
                .collect(Collectors.toList());

        return filteredTransactions;
    }

    @PostMapping("/transaction/save")
    public String saveTransaction(@ModelAttribute("transaction") Transaction transaction, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("groups", groupService.listAllGroups());  // Ensure groups are available if there's an error
            return "transaction";
        }

        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login?logout";
        }

        // Retrieve the group for the transaction
        Group group = groupService.findGroupById(transaction.getGroup().getBudgetId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));

        // Check if transaction amount exceeds budget left
        if (transaction.getAmount() > group.getBudgetLeft()) {
            return "redirect:/user/transaction?error";
        }

        transactionService.addNewTransaction(transaction);
        return "redirect:/user/transaction?success3";
    }

    @GetMapping("/transaction/delete/{id}")
    public String deleteTransaction(@PathVariable("id") Integer id) {
        transactionService.deleteTransactionById(id);
        return "redirect:/user/transaction?success2";
    }

    @PostMapping("/transaction/update")
    public String updateTransaction(@ModelAttribute("transaction") Transaction transaction, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("groups", groupService.listAllGroups());
            return "transaction";
        }
        Group group = groupService.findGroupById(transaction.getGroup().getBudgetId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID"));

        // Check if transaction amount exceeds budget left
        if (transaction.getAmount() > group.getBudgetLeft()) {
            return "redirect:/user/transaction?error";
        }

        transactionService.updateTransaction(transaction);
        return "redirect:/user/transaction?success1";
    }



    //    ---------     BUDGET     ----------     //
    @GetMapping("/budget")
    public String userBudget(Model model) {

        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login?logout"; // Log out if the user is not found
        }

        // Get the groups for the current user
        List<Group> groups = groupService.listGroupsByUserId(user.getUserId());
        model.addAttribute("groups", groups);
        model.addAttribute("user", user);
        return "budget";
    }

    @GetMapping("/budget/category")
    @ResponseBody
    public List<Group> getCategoryData(@RequestParam("month") String month) {
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return Collections.emptyList();
        }
        List<Group> groups = groupService.listGroupsByUserId(user.getUserId());

        // If month is "00", return all groups without filtering
        if ("00".equals(month)) {
            return groups;
        }

        // Filter groups based on the month
        List<Group> filteredGroups = groups.stream()
                .filter(group -> isMonthInRange(group, month))
                .collect(Collectors.toList());

        return filteredGroups;
    }

    @GetMapping("/budget/add")
    public String showAddNewGroupForm(Model model) {
        model.addAttribute("group", new Group());
        return "add-budget";
    }

    @PostMapping("/budget/add")
    public String addGroup(@Valid @ModelAttribute("group") Group group, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-budget";
        }

        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login?logout";
        }

        // Extract the month from the startDate or endDate
        String month = group.getStartDate().split("-")[1];

        // Get the list of groups for the same month
        List<Group> allGroups = groupService.listGroupsByUserId(user.getUserId());
        List<Group> sameMonthGroups = allGroups.stream()
                .filter(g -> {
                    String groupMonth = g.getStartDate().split("-")[1];
                    return groupMonth.equals(month);
                })
                .collect(Collectors.toList());

        // Check if a group with the same name exists for the specified month
        boolean nameExists = sameMonthGroups.stream()
                .anyMatch(g -> g.getCategory().equalsIgnoreCase(group.getCategory()));
        if (nameExists) {
            return "redirect:/user/budget/add?error2";
        }

        // Calculate the total budget for that month
        double totalBudget = sameMonthGroups.stream()
                .mapToDouble(Group::getBamount)
                .sum();

        // Check if adding the new group's budget exceeds the limit
        if (totalBudget + group.getBamount() <= user.getTotamount()) {
            if (group.getUser() == null) {
                group.setUser(user); // Initialize the user if it's null
            } else {
                group.getUser().setUserId(user.getUserId());
            }
            groupService.addNewGroup(group);
            return "redirect:/user/budget?success3";
        } else {
            // Redirect to the form with an error message
            return "redirect:/user/budget/add?error1";
        }
    }

    @GetMapping("/budget/update")
    public String showUpdateMainForm(Model model) {

        // Get the list of groups for June (month = 6)
        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login?logout";
        }
        List<Group> allGroups = groupService.listGroupsByUserId(user.getUserId());

        // Filter groups for the month of June (month = 6)
        List<Group> juneGroups = allGroups.stream()
                .filter(g -> {
                    // Extract month from startDate
                    String groupMonth = g.getStartDate().split("-")[1];
                    return groupMonth.equals("06"); // Filter for June (month = 6)
                })
                .collect(Collectors.toList());

        model.addAttribute("groups", juneGroups);
        return "choose-budget-to-update";
    }

    @GetMapping("/budget/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Group group = groupService.findGroupById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid budget Id:" + id));
        model.addAttribute("group", group);
        return "update-budget";
    }

    @PostMapping("/budget/update/{id}")
    public String updateBudget(@PathVariable("id") long id, @Valid @ModelAttribute("group") Group group, BindingResult result, Model model) {
        if (result.hasErrors()) {
            group.setBudgetId(id);
            return "update-budget";
        }

        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login?logout";
        }

        // Extract the month from the startDate or endDate
        String month = group.getStartDate().split("-")[1]; // Assuming your date format allows this

        // Get the list of groups for the same month (June)
        List<Group> allGroups = groupService.listGroupsByUserId(user.getUserId());
        List<Group> juneGroups = allGroups.stream()
                .filter(g -> isMonthInRange(g, "6")) // Assuming "6" is June
                .collect(Collectors.toList());

        // Remove the current group being updated from the list
        juneGroups = juneGroups.stream()
                .filter(g -> g.getBudgetId() != id)
                .collect(Collectors.toList());

        // Check if a group with the same name exists for the specified month
        boolean nameExists = juneGroups.stream()
                .anyMatch(g -> g.getCategory().equalsIgnoreCase(group.getCategory()));
        if (nameExists) {
            return "redirect:/user/budget/edit/" + id + "?error2";
        }

        // Calculate the total budget for June
        double totalBudget = juneGroups.stream()
                .mapToDouble(Group::getBamount)
                .sum();

        // Check if adding the new group's budget exceeds the limit
        if (totalBudget + group.getBamount() <= user.getTotamount()) {
            groupService.updateGroup(id, group, user);
            return "redirect:/user/budget?success1";
        } else {
            // Redirect to the form with an error message
            return "redirect:/user/budget/edit/" + id + "?error1";
        }
    }

    @GetMapping("/budget/delete")
    public String showDeleteMainForm(Model model) {

        String email = getCurrentUserEmail();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return "redirect:/login?logout";
        }

        // Get the list of groups for June (month = 6)
        List<Group> allGroups = groupService.listGroupsByUserId(user.getUserId());

        // Filter groups for the month of June (month = 6)
        List<Group> juneGroups = allGroups.stream()
                .filter(g -> {
                    // Extract month from startDate
                    String groupMonth = g.getStartDate().split("-")[1];
                    return groupMonth.equals("06"); // Filter for June (month = 6)
                })
                .collect(Collectors.toList());

        model.addAttribute("groups", juneGroups);
        return "choose-budget-to-delete";
    }

    @GetMapping("/budget/delete/{id}")
    public String deleteBudget(@PathVariable("id") long id, Model model) {
        groupService.deleteGroup(id);
        return "redirect:/user/budget?success2";
    }

    //    ---------     METHODS     ----------     //
    private boolean isMonthInRange(Transaction transaction, String month) {
        try {
            String date = String.valueOf(transaction.getDate());
            if (date == null) {
                return false;
            }
            int setMonth = Integer.parseInt(date.split("-")[1]);
            int filterMonth = Integer.parseInt(month);
            return setMonth == filterMonth;
        } catch (Exception e) {
            // Log the exception and the problematic group details for debugging
            System.err.println("Error processing transaction: " + transaction);
            e.printStackTrace();
            return false;
        }
    }

    private boolean isMonthInRange(Group group, String month) {
        try {
            String startDate = group.getStartDate();
            String endDate = group.getEndDate();
            if (startDate == null || endDate == null) {
                return false;
            }
            int startMonth = Integer.parseInt(startDate.split("-")[1]);
            int endMonth = Integer.parseInt(endDate.split("-")[1]);
            int filterMonth = Integer.parseInt(month);
            return startMonth == filterMonth || endMonth == filterMonth;
        } catch (Exception e) {
            // Log the exception and the problematic group details for debugging
            System.err.println("Error processing group: " + group);
            e.printStackTrace();
            return false;
        }
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
