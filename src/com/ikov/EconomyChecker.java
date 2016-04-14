/*
 * package com.ikov; import java.awt.BorderLayout; import java.awt.Color; import java.awt.Dimension;
 * import java.awt.Toolkit; import java.awt.event.ActionEvent; import
 * org.apache.commons.lang3.ArrayUtils; import java.util.Arrays; import
 * java.awt.event.ActionListener; import java.io.BufferedReader; import java.io.File; import
 * java.io.FileReader; import java.io.IOException; import java.math.BigInteger; import
 * java.text.DecimalFormat; import java.util.Collections; import java.util.Comparator; import
 * java.util.LinkedList; import javax.swing.DefaultListModel; import javax.swing.JEditorPane; import
 * javax.swing.JFrame; import javax.swing.JList; import javax.swing.JMenu; import
 * javax.swing.JMenuBar; import javax.swing.JMenuItem; import javax.swing.JOptionPane; import
 * javax.swing.JScrollPane; import javax.swing.JSplitPane; import javax.swing.JTextPane; import
 * javax.swing.ListSelectionModel; import javax.swing.UIManager; import
 * javax.swing.UnsupportedLookAndFeelException; import javax.swing.event.ListSelectionEvent; import
 * javax.swing.event.ListSelectionListener; import javax.swing.text.BadLocationException; import
 * javax.swing.text.Document; import javax.swing.text.SimpleAttributeSet; import
 * javax.swing.text.StyleConstants; import com.ikov.model.PlayerRights; import
 * com.ikov.model.definitions.ItemDefinition; import com.ikov.world.entity.impl.player.PlayerSaving;
 * import com.google.gson.Gson; import com.google.gson.GsonBuilder; import
 * com.google.gson.JsonObject; import com.google.gson.JsonParser; import com.ikov.model.Item; /**
 * @author Stan/Jonathan Sirens Checks economy for mass items and coins in-game.
 */
/*
 * public class EconomyChecker extends JFrame implements ListSelectionListener, ActionListener {
 * public static void main(String[] args) throws Exception { Dimension screenSize =
 * Toolkit.getDefaultToolkit().getScreenSize(); ItemDefinition.init(); EconomyChecker economyChecker
 * = new EconomyChecker("Ikov Economy Checker", 800, screenSize.height - 300, JFrame.EXIT_ON_CLOSE);
 * UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); economyChecker.refresh();
 * economyChecker.buildFrame(); economyChecker.setLocationRelativeTo(null);
 * economyChecker.viewAccount(economyChecker.accounts.get(0)); economyChecker.setVisible(true); }
 * public EconomyChecker(String title, int width, int height, int closeOperation) throws
 * ClassNotFoundException, InstantiationException, IllegalAccessException,
 * UnsupportedLookAndFeelException { super(title); setSize(width, height);
 * setDefaultCloseOperation(closeOperation); } private DefaultListModel<Account> listModel; private
 * JList<Account> list; private JTextPane textPane; private int showTopAmount = 50; private void
 * buildFrame() { final JMenu fileMenu = new JMenu("File"); final JMenu top20 = new JMenu("Show Top"
 * ); String[] mainButtons = new String[] { "Commands", "Refresh Data", "-", "Set Top Amount", "-",
 * "Exit" }; String[] quickTops = new String[] { "Top 20 Player's Networth (Items)", "-",
 * "Top 20 Player's Networth (Coins)"}; for (String name : mainButtons) { JMenuItem menuItem = new
 * JMenuItem(name); if (name.equalsIgnoreCase("-")) { fileMenu.addSeparator(); } else {
 * menuItem.addActionListener(this); fileMenu.add(menuItem); } } for (String name : quickTops) {
 * JMenuItem menuItem = new JMenuItem(name); if (name.equalsIgnoreCase("-")) {
 * fileMenu.addSeparator(); } else { menuItem.addActionListener(this); top20.add(menuItem); } }
 * JMenuBar menuBar = new JMenuBar(); JMenuBar jmenubar = new JMenuBar(); add(jmenubar);
 * menuBar.add(fileMenu); menuBar.add(top20); getContentPane().add(menuBar, BorderLayout.NORTH);
 * final int size = accounts.size(); listModel = new DefaultListModel<Account>(); int index = 0; for
 * (int i = 0; i < size; i++) { listModel.add(index, accounts.get(i)); index++; } list = new
 * JList<Account>(listModel); list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 * list.setSelectedIndex(0); list.addListSelectionListener(this); list.setVisibleRowCount(size);
 * final JScrollPane accountsPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
 * JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); textPane = new JTextPane(); textPane.setEditable(false);
 * textPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true); final JScrollPane
 * informationPane = new JScrollPane(textPane); final JSplitPane splitPane = new
 * JSplitPane(JSplitPane.HORIZONTAL_SPLIT, accountsPane, informationPane);
 * splitPane.setDividerLocation(getWidth() / 4); add(splitPane); } private void chooseCommand()
 * throws BadLocationException { String[] choices = { "List Top " + showTopAmount +
 * " Player's Networth (Items)", "List Top " + showTopAmount + " Player's Networth (Coins)",
 * "Total Economy Networth (Items)", "Total Economy Networth (Coins)", "Specific Item Searcher",
 * "Cancel" }; String input = (String) JOptionPane.showInputDialog(this, "Pick a Command...",
 * "Choose A Command To Execute", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]); if
 * (input == null) return; if (input.equals(choices[0])) { //player networth (total) final
 * LinkedList<Account> networthAccounts = new LinkedList<>(); networthAccounts.addAll(accounts);
 * Collections.sort(networthAccounts, new Comparator<Account>() {
 * @Override public int compare(Account oldAccount, Account newAccount) { if (newAccount == null)
 * return 1; if (oldAccount == null) return 0; return (long) newAccount.getNetworth() > (long)
 * oldAccount.getNetworth() ? 1 : -1; }; }); textPane.setText("Top " + showTopAmount +
 * " Players (Items)" + "\n"); Document document = textPane.getDocument(); final DecimalFormat
 * formatter = new DecimalFormat(); for (int i = 0 ; i < showTopAmount; i++) { Account account =
 * networthAccounts.get(i); if (account != null) { document.insertString( document.getLength(),
 * "\n\n" + "#" + (i + 1) + " - " + account.toString() + ": " +
 * formatter.format(account.getNetworth()) + " gp", null ); } } textPane.setCaretPosition(0); } else
 * if (input.equals(choices[1])) { //player networth (coins) final LinkedList<Account>
 * networthAccounts = new LinkedList<>(); networthAccounts.addAll(accounts);
 * Collections.sort(networthAccounts, new Comparator<Account>() {
 * @Override public int compare(Account oldAccount, Account newAccount) { if (newAccount == null)
 * return 1; if (oldAccount == null) return 0; return (long) newAccount.getTotalAmountOfCoins() >
 * (long) oldAccount.getTotalAmountOfCoins() ? 1 : -1; }; }); textPane.setText("Top " +
 * showTopAmount + " Players (Coins)" + "\n"); Document document = textPane.getDocument(); final
 * DecimalFormat formatter = new DecimalFormat(); for (int i = 0 ; i < showTopAmount; i++) { Account
 * account = networthAccounts.get(i); if (account != null) { document.insertString(
 * document.getLength(), "\n\n" + "#" + (i + 1) + " - " + account.toString() + ": " +
 * formatter.format(account.getTotalAmountOfCoins()) + " gp", null ); } }
 * textPane.setCaretPosition(0); } else if (input.equals(choices[2])) { //economy networth (total)
 * BigInteger networth = BigInteger.valueOf(0); for (Account account : accounts) { networth =
 * networth.add(BigInteger.valueOf(account.getNetworth())); } JOptionPane.showMessageDialog(this,
 * "Economy Networth (Items): " + new DecimalFormat().format(networth)); } else if
 * (input.equals(choices[3])) { //economy networth (coins) BigInteger networth =
 * BigInteger.valueOf(0); for (Account account : accounts) { networth =
 * networth.add(BigInteger.valueOf(account.getTotalAmountOfCoins())); }
 * JOptionPane.showMessageDialog(this, "Economy Networth (Coins): " + new
 * DecimalFormat().format(networth)); } else if (input.equals(choices[4])) { //item search String
 * item = JOptionPane.showInputDialog(this, "Enter an item name or id"); int itemId = -1; int
 * itemAmount = -1; String itemName = null; if (item == null) return; try { itemId =
 * Integer.valueOf(item); } catch (Exception exception) {} if (itemId == -1) itemName = item; String
 * amount = JOptionPane.showInputDialog(this, "Enter an amount to search for"); if (itemId != -1 ||
 * itemName != null) { try { itemAmount = Integer.valueOf(amount); } catch (Exception exception) {
 * JOptionPane.showMessageDialog(this, "Enter a valid amount", "Error!", JOptionPane.ERROR_MESSAGE);
 * //exception.printStackTrace(); } } if (itemAmount != -1) { if (itemId != -1) searchItem(new
 * Item(itemId, itemAmount)); else if (itemName != null) searchItem(itemName, itemAmount); } } }
 * private void viewAccount(Account account) throws BadLocationException { final DecimalFormat
 * format = new DecimalFormat(); textPane.setText(""); final Document document =
 * textPane.getDocument(); document.insertString(0, "Username: " + account.toString() + "\n" +
 * "Rights: " + account.rights + "\n" + "Seconds Played: " +
 * format.format(account.totalSecondsPlayed) + "\n" + "Networth: " +
 * format.format(account.getNetworth()) + "\n" + "Amount of Coins: " +
 * format.format(account.getTotalAmountOfCoins()) + "\n" + "Money Pouch: " +
 * format.format(account.moneyPouch) + "\n" + "PK Points: " + format.format(account.pkPoints) + "\n"
 * + "Vote Points: " + format.format(account.votePoints) + "\n" + "Credits: " +
 * format.format(account.credits) + "\n" + null ); document.insertString(document.getLength(), "\n"
 * + "[Inventory]" + "\n", null); for (Item item : account.inventory) { if (item == null) continue;
 * final String name = "Item: " + ItemDefinition.forId(item.getId()).name + " - [" + item.getId() +
 * " : " + format.format(item.getAmount()) + "]" + "\n"; if (item.getDefinition().getValue() *
 * item.getAmount() >= 10_000_000) { final SimpleAttributeSet attributes = new SimpleAttributeSet();
 * StyleConstants.setForeground(attributes, Color.RED); StyleConstants.setBold(attributes, true);
 * document.insertString(document.getLength(), name, attributes); } else {
 * document.insertString(document.getLength(), name, null); } }
 * document.insertString(document.getLength(), "\n" + "[Equipment]" + "\n", null); for (Item item :
 * account.equipment) { if (item == null) continue; final String name = "Item: " + item.name() +
 * " - [" + item.getId() + " : " + format.format(item.getAmount()) + "]" + "\n"; if
 * (item.getDefinition().getValue() * item.getAmount() >= 10_000_000) { final SimpleAttributeSet
 * attributes = new SimpleAttributeSet(); StyleConstants.setForeground(attributes, Color.RED);
 * StyleConstants.setBold(attributes, true); document.insertString(document.getLength(), name,
 * attributes); } else { document.insertString(document.getLength(), name, null); } }
 * document.insertString(document.getLength(), "\n" + "[Bank]" + "\n", null); for (Item item :
 * account.bank) { if (item == null) continue; final String name = "Item: " + item.name() + " - [" +
 * item.getId() + " : " + format.format(item.getAmount()) + "]" + "\n"; if
 * (item.getDefinition().getValue() * item.getAmount() >= 10_000_000) { final SimpleAttributeSet
 * attributes = new SimpleAttributeSet(); StyleConstants.setForeground(attributes, Color.RED);
 * StyleConstants.setBold(attributes, true); document.insertString(document.getLength(), name,
 * attributes); } else { document.insertString(document.getLength(), name, null); } }
 * textPane.setCaretPosition(0); } private void refresh() throws Exception { final File folder = new
 * File(FOLDER_DIRECTORY); if (!folder.isDirectory()) throw new Exception(folder.getAbsolutePath() +
 * " is not a directory!"); accounts.clear(); File[] files = folder.listFiles(); for (int i = 0; i <
 * files.length; i++) { final Account account = new Account(files[i]); account.parse();
 * accounts.add(i, account); } } private void searchItem(Item item) throws BadLocationException {
 * textPane.setText(""); final Document document = textPane.getDocument(); final DecimalFormat
 * formatter = new DecimalFormat(); long total = 0; for (Account account : accounts) { for (Item
 * accountItem : account.inventory) { if (accountItem != null && accountItem.getId() == item.getId()
 * && accountItem.getAmount() >= item.getAmount()) { document.insertString(document.getLength(),
 * account.toString() + ": " + accountItem.name() + " - [" + accountItem.getId() + " : " +
 * formatter.format(accountItem.getAmount()) + "]\n\n", null); total += accountItem.getAmount(); } }
 * for (Item accountItem : account.equipment) { if (accountItem != null && accountItem.getId() ==
 * item.getId() && accountItem.getAmount() >= item.getAmount()) {
 * document.insertString(document.getLength(), account.toString() + ": " + accountItem.name() +
 * " - [" + accountItem.getId() + " : " + formatter.format(accountItem.getAmount()) + "]\n\n",
 * null); total += accountItem.getAmount(); } } for (Item accountItem : account.bank) { if
 * (accountItem != null && accountItem.getId() == item.getId() && accountItem.getAmount() >=
 * item.getAmount()) { document.insertString(document.getLength(), account.toString() + ": " +
 * accountItem.name() + " - [" + accountItem.getId() + " : " +
 * formatter.format(accountItem.getAmount()) + "]\n\n", null); total += accountItem.getAmount(); } }
 * } document.insertString(document.getLength(), "\nTotal Search Amount: " + total, null); } private
 * void searchItem(String itemName, int amount) throws BadLocationException { textPane.setText("");
 * final Document document = textPane.getDocument(); final DecimalFormat formatter = new
 * DecimalFormat(); itemName = itemName.toLowerCase(); long total = 0; for (Account account :
 * accounts) { for (Item accountItem : account.inventory) { if (accountItem == null) continue;
 * String accountItemName = accountItem.name().toLowerCase(); if (accountItemName.contains(itemName)
 * && accountItem.getAmount() >= amount) { document.insertString(document.getLength(),
 * account.toString() + ": " + accountItem.name() + " - [" + accountItem.getId() + " : " +
 * formatter.format(accountItem.getAmount()) + "]\n\n", null); total += accountItem.getAmount(); } }
 * for (Item accountItem : account.equipment) { if (accountItem == null) continue; String
 * accountItemName = accountItem.name().toLowerCase(); if (accountItemName.contains(itemName) &&
 * accountItem.getAmount() >= amount) { document.insertString(document.getLength(),
 * account.toString() + ": " + accountItem.name() + " - [" + accountItem.getId() + " : " +
 * formatter.format(accountItem.getAmount()) + "]\n\n", null); total += accountItem.getAmount(); } }
 * for (Item accountItem : account.bank) { if (accountItem == null) continue; String accountItemName
 * = accountItem.name().toLowerCase(); if (accountItemName.contains(itemName) &&
 * accountItem.getAmount() >= amount) { document.insertString(document.getLength(),
 * account.toString() + ": " + accountItem.name() + " - [" + accountItem.getId() + " : " +
 * formatter.format(accountItem.getAmount()) + "]\n\n", null); total += accountItem.getAmount(); } }
 * } document.insertString(document.getLength(), "\n\nTotal Search Amount: " + total, null); }
 * public static void cleanCharacterFiles() throws Exception { final File folder = new
 * File(FOLDER_DIRECTORY); if (!folder.isDirectory()) throw new Exception(folder.getAbsolutePath() +
 * " is not a directory!"); for (File file : folder.listFiles()) { if (file.getName().contains(
 * "conflicted copy")) { file.delete(); System.out.println("Deleting conflicted copy: " +
 * file.getName()); continue; } final Account account = new Account(file); account.parse(); if
 * (account.totalSecondsPlayed < 60) { file.delete(); System.out.println("Deleted " + file.getName()
 * + "'s file with " + account.totalSecondsPlayed + " total gameplay seconds"); } } } /** Checks the
 * top networth of the prefered amount of players
 * @param amountOfPlayers the amount of players that should be placed in the top
 */
/*
 * public void checkTopNetworth(int amountOfPlayers){ }
 * @Override public void valueChanged(ListSelectionEvent e) { if (e.getValueIsAdjusting() == false)
 * { if (list.getSelectedIndex() != -1) { try { viewAccount(accounts.get(list.getSelectedIndex()));
 * } catch (BadLocationException exception) { exception.printStackTrace(); } } } }
 * @Override public void actionPerformed(ActionEvent e) { String cmd = e.getActionCommand(); if (cmd
 * == null) return; if (cmd.equals("Commands")) { try { chooseCommand(); } catch
 * (BadLocationException exception) { exception.printStackTrace(); } } else if (cmd.equals(
 * "Top 20 Player's Networth (Items)")) { try { showTopAmount = 20; //player networth (total) final
 * LinkedList<Account> networthAccounts = new LinkedList<>(); networthAccounts.addAll(accounts);
 * Collections.sort(networthAccounts, new Comparator<Account>() {
 * @Override public int compare(Account oldAccount, Account newAccount) { if (newAccount == null)
 * return 1; if (oldAccount == null) return 0; return (long) newAccount.getNetworth() > (long)
 * oldAccount.getNetworth() ? 1 : -1; }; }); textPane.setText("Top " + showTopAmount +
 * " Players (Items)" + "\n"); Document document = textPane.getDocument(); final DecimalFormat
 * formatter = new DecimalFormat(); for (int i = 0 ; i < showTopAmount; i++) { Account account =
 * networthAccounts.get(i); if (account != null) { document.insertString( document.getLength(),
 * "\n\n" + "RANK#" + (i + 1) + " - " + account.toString() + ": " +
 * formatter.format(account.getNetworth()) + " gp", null ); } } textPane.setCaretPosition(0); }
 * catch (Exception exception) { exception.printStackTrace(); } }else if (cmd.equals(
 * "Top 20 Player's Networth (Coins)")) { try { showTopAmount = 20; //player networth (coins) final
 * LinkedList<Account> networthAccounts = new LinkedList<>(); networthAccounts.addAll(accounts);
 * Collections.sort(networthAccounts, new Comparator<Account>() {
 * @Override public int compare(Account oldAccount, Account newAccount) { if (newAccount == null)
 * return 1; if (oldAccount == null) return 0; return (long) newAccount.getTotalAmountOfCoins() >
 * (long) oldAccount.getTotalAmountOfCoins() ? 1 : -1; }; }); textPane.setText("Top " +
 * showTopAmount + " Players (Coins)" + "\n"); Document document = textPane.getDocument(); final
 * DecimalFormat formatter = new DecimalFormat(); for (int i = 0 ; i < showTopAmount; i++) { Account
 * account = networthAccounts.get(i); if (account != null) { document.insertString(
 * document.getLength(), "\n\n" + "RANK#" + (i + 1) + " - " + account.toString() + ": " +
 * formatter.format(account.getTotalAmountOfCoins()) + " gp", null ); } }
 * textPane.setCaretPosition(0); } catch (Exception exception) { exception.printStackTrace(); } }
 * else if (cmd.equals("Refresh Data")) { try { refresh(); listModel.clear(); int index = 0; for
 * (int i = 0; i < accounts.size(); i++) { listModel.add(index, accounts.get(i)); index++; } } catch
 * (Exception exception) { exception.printStackTrace(); } } else if (cmd.equals("Set Top Amount")) {
 * final String input = JOptionPane.showInputDialog("Enter a Number:"); try { showTopAmount =
 * Integer.valueOf(input); } catch (Exception exception) { JOptionPane.showMessageDialog(this,
 * "Enter a valid number", "Error!", JOptionPane.ERROR_MESSAGE); exception.printStackTrace(); } }
 * else if (cmd.equals("Exit")) { System.exit(0); } } private final LinkedList<Account> accounts =
 * new LinkedList<>(); private static final String FOLDER_DIRECTORY = "./characters/"; private
 * static final long serialVersionUID = 1L; private static final class Account { private
 * Account(File file) { this.file = file; } private final File file; private String username;
 * private PlayerRights rights; private long moneyPouch; private int totalSecondsPlayed; private int
 * pkPoints; private int credits; private int boss_Points; private int votePoints; private Item[]
 * inventory = new Item[28]; private Item[] equipment = new Item[14]; private Item[] bank = new
 * Item[350 + (8 * 100)]; //350 = main tab space, 8 tabs - 100 slots each private long getNetworth()
 * { long networth = moneyPouch; for (Item item : inventory) { if (item != null) { networth +=
 * item.getDefinition().getValue() * item.getAmount(); } } for (Item item : equipment) { if (item !=
 * null) { networth += item.getDefinition().getValue() * item.getAmount(); } } for (Item item :
 * bank) { if (item != null) { networth += item.getDefinition().getValue() * item.getAmount(); } }
 * return networth; } private long getTotalAmountOfCoins() { long networth = moneyPouch; for (Item
 * item : inventory) { if (item != null && item.getId() == 995) { networth += item.getAmount(); } }
 * for (Item item : equipment) { if (item != null && item.getId() == 995) { networth +=
 * item.getAmount(); } } for (Item item : bank) { if (item != null && item.getId() == 995) {
 * networth += item.getAmount(); } } return networth; } private void parse() { // Now read the
 * properties from the json parser. try (FileReader fileReader = new FileReader(file)) { JsonParser
 * fileParser = new JsonParser(); Gson builder = new GsonBuilder().create(); JsonObject reader =
 * (JsonObject) fileParser.parse(fileReader); if (reader.has("username")) { username =
 * reader.get("username").getAsString(); } if (reader.has("staff-rights")) { rights =
 * PlayerRights.valueOf(reader.get("staff-rights").getAsString()); } if (reader.has("money-pouch"))
 * { moneyPouch = reader.get("money-pouch").getAsLong(); } if (reader.has("credits")) { credits =
 * reader.get("credits").getAsInt(); } if(reader.has("voting-points")) { votePoints =
 * reader.get("voting-points").getAsInt(); } if(reader.has("slayer-points")) { votePoints =
 * reader.get("slayer-points").getAsInt(); } if(reader.has("pk-points")) { pkPoints =
 * reader.get("pk-points").getAsInt(); } if (reader.has("inventory")) { inventory =
 * builder.fromJson(reader.get("inventory").getAsJsonArray(), Item[].class); } if
 * (reader.has("equipment")) { equipment =
 * builder.fromJson(reader.get("equipment").getAsJsonArray(), Item[].class); } /** BANK
 **/
/*
 * for(int i = 0; i < 9; i++) { if(reader.has("bank-"+i+"")) Arrays.add(bank,
 * builder.fromJson(reader.get("bank-"+i+"").getAsJsonArray(), Item[].class)); }
 * if(reader.has("bank-0")) { player.setBank(0, new
 * Bank(player)).getBank(0).addItems(builder.fromJson(reader.get("bank-0").getAsJsonArray(),
 * Item[].class), false); } if(reader.has("bank-1")) { player.setBank(1, new
 * Bank(player)).getBank(1).addItems(builder.fromJson(reader.get("bank-1").getAsJsonArray(),
 * Item[].class), false); } if(reader.has("bank-2")) { player.setBank(2, new
 * Bank(player)).getBank(2).addItems(builder.fromJson(reader.get("bank-2").getAsJsonArray(),
 * Item[].class), false); } if(reader.has("bank-3")) { player.setBank(3, new
 * Bank(player)).getBank(3).addItems(builder.fromJson(reader.get("bank-3").getAsJsonArray(),
 * Item[].class), false); } if(reader.has("bank-4")) { player.setBank(4, new
 * Bank(player)).getBank(4).addItems(builder.fromJson(reader.get("bank-4").getAsJsonArray(),
 * Item[].class), false); } if(reader.has("bank-5")) { player.setBank(5, new
 * Bank(player)).getBank(5).addItems(builder.fromJson(reader.get("bank-5").getAsJsonArray(),
 * Item[].class), false); } if(reader.has("bank-6")) { player.setBank(6, new
 * Bank(player)).getBank(6).addItems(builder.fromJson(reader.get("bank-6").getAsJsonArray(),
 * Item[].class), false); } if(reader.has("bank-7")) { player.setBank(7, new
 * Bank(player)).getBank(7).addItems(builder.fromJson(reader.get("bank-7").getAsJsonArray(),
 * Item[].class), false); } if(reader.has("bank-8")) { player.setBank(8, new
 * Bank(player)).getBank(8).addItems(builder.fromJson(reader.get("bank-8").getAsJsonArray(),
 * Item[].class), false); } } catch (Exception e) { e.printStackTrace(); } /* BufferedReader reader
 * = null; try { reader = new BufferedReader(new FileReader(file)); String line = null; while ((line
 * = reader.readLine()) != null) { if (!line.contains(": ")) continue; final String[] token =
 * line.split(": "); if (token == null || token.length < 1) continue; final String tag = token[0];
 * final String value = token.length > 1 ? token[1] : ""; if (tag.equals("username")) { username =
 * value; } else if (tag.equals("rights")) { rights = PlayerRights.valueOf(value); } else if
 * (tag.equals("money_pouch")) { moneyPouch = Long.valueOf(value); } else if
 * (tag.equals("total_seconds_played")) { totalSecondsPlayed = Integer.valueOf(value); } else if
 * (tag.equals("pk_points")) { pkPoints = Integer.valueOf(value); } else if
 * (tag.equals("vote_points")) { votePoints = Integer.valueOf(value); } else if
 * (tag.equals("donator_points")) { donatorPoints = Integer.valueOf(value); } else if
 * (tag.equals("merchant_points")) { merchantPoints = Long.valueOf(value); } else if
 * (tag.startsWith("inventory")) { final String substring = tag.substring(tag.indexOf('[') + 1,
 * tag.indexOf(']')); final int index = Integer.valueOf(substring); final String[] split =
 * value.split(" - "); final Item item = new Item(Integer.valueOf(split[0]),
 * Integer.valueOf(split[1])); inventory[index] = item; } else if (tag.startsWith("equipment")) {
 * final String substring = tag.substring(tag.indexOf('[') + 1, tag.indexOf(']')); final int index =
 * Integer.valueOf(substring); final String[] split = value.split(" - "); final Item item = new
 * Item(Integer.valueOf(split[0]), Integer.valueOf(split[1])); equipment[index] = item; } else if
 * (tag.startsWith("bank") && !tag.contains("tab") && !tag.contains("pin")) { final String substring
 * = tag.substring(tag.indexOf('[') + 1, tag.indexOf(']')); final int index =
 * Integer.valueOf(substring); final String[] split = value.split(" - "); final Item item = new
 * Item(Integer.valueOf(split[0]), Integer.valueOf(split[1])); bank[index] = item; } else if
 * (tag.startsWith("bank_tab")) { final String tab_substring = tag.substring(tag.indexOf('(') + 1,
 * tag.indexOf(')')); final int tab_index = Integer.valueOf(tab_substring); final String
 * item_substring = tag.substring(tag.indexOf('[') + 1, tag.indexOf(']')); final int item_index =
 * Integer.valueOf(item_substring); final String[] split = value.split(" - "); final Item item = new
 * Item(Integer.valueOf(split[0]), Integer.valueOf(split[1])); int amountAddition = (tab_index - 1)
 * * 100 + 350; bank[item_index + amountAddition] = item; } } reader.close(); } catch (IOException
 * exception) { try { if (reader != null) reader.close(); } catch (IOException io) {
 * io.printStackTrace(); } }
 */
/*
 * }
 * @Override public String toString() { return username; } } /* }
 */
