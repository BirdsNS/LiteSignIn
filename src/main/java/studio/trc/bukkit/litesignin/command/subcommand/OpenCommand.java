package studio.trc.bukkit.litesignin.command.subcommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import studio.trc.bukkit.litesignin.command.SignInSubCommand;
import studio.trc.bukkit.litesignin.command.SignInSubCommandType;
import studio.trc.bukkit.litesignin.event.Menu;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OpenCommand implements SignInSubCommand {
    @Override
    public void execute(CommandSender sender, String subCommand, String... args) {
        if (!(sender instanceof ConsoleCommandSender) && args.length == 0) return;
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage("Player not found");
            return;
        }
        switch (args.length) {
            case 2:
                Menu.openGUI(player);
                break;
            case 3:
                int month;
                try {
                    month = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    Menu.openGUI(player);
                    return;
                }
                Menu.openGUI(player, month);
                break;
            case 4:
                try {
                    month = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    Menu.openGUI(player);
                    return;
                }
                int year;
                try {
                    year = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    Menu.openGUI(player);
                    return;
                }
                Menu.openGUI(player, month, year);
                break;
        }
    }

    @Override
    public String getName() {
        return "open";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String subCommand, String... args) {
        if (args.length == 2) return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        if (args.length == 3) return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
        return Collections.emptyList();
    }

    @Override
    public SignInSubCommandType getCommandType() {
        return SignInSubCommandType.GUI;
    }
}
