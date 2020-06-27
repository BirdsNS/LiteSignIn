package studio.trc.bukkit.litesignin.reward.util;

import java.util.Date;

import studio.trc.bukkit.litesignin.config.ConfigurationUtil;
import studio.trc.bukkit.litesignin.config.ConfigurationType;
import studio.trc.bukkit.litesignin.util.SignInDate;

public class SignInTimePeriod
{
    public static String getSetting(SignInGroup group, SignInDate time) {
        if (!ConfigurationUtil.getConfig(ConfigurationType.REWARDSETTINGS).contains("Reward-Settings.Permission-Groups." + group.getGroupName() + ".Special-Time-periods")) {
            return null;
        }
        for (String value : ConfigurationUtil.getConfig(ConfigurationType.REWARDSETTINGS).getConfigurationSection("Reward-Settings.Permission-Groups." + group.getGroupName() + ".Special-Time-periods").getKeys(false)) {
            SignInDate timePeriod = SignInDate.getInstanceAsTimePeriod(value);
            if (timePeriod == null) {
                continue;
            }
            SignInTimePeriodType timePeriodType = getFromName(ConfigurationUtil.getConfig(ConfigurationType.REWARDSETTINGS).getString("Reward-Settings.Permission-Groups." + group.getGroupName() + ".Special-Time-periods." + value + ".Option"));
            if (timePeriodType == null) {
                return null;
            }
            SignInDate now = SignInDate.getInstance(new Date());
            switch (timePeriodType) {
                case ON_TIME: {
                    String[] section = value.split(":");
                    switch (section.length) {
                        case 1: {
                            if (now.getHour() == timePeriod.getHour()) {
                                return value;
                            }
                            break;
                        }
                        case 2: {
                            if (now.getHour() == timePeriod.getHour() &&
                                now.getMinute() == timePeriod.getMinute()) {
                                return value;
                            }
                            break;
                        }
                        case 3: {
                            if (now.getHour() == timePeriod.getHour() &&
                                now.getMinute() == timePeriod.getMinute() &&
                                now.getSecond() == timePeriod.getSecond()) {
                                return value;
                            }
                            break;
                        }
                    }
                    break;
                }
                case AFTER_THIS_TIME: {
                    SignInDate limit;
                    if (!ConfigurationUtil.getConfig(ConfigurationType.REWARDSETTINGS).contains("Reward-Settings.Permission-Groups." + group.getGroupName() + ".Special-Time-periods." + value + ".Time-Limit")) {
                        limit = SignInDate.getInstanceAsTimePeriod("23:59:59");
                    } else {
                        limit = SignInDate.getInstanceAsTimePeriod(ConfigurationUtil.getConfig(ConfigurationType.REWARDSETTINGS).getString("Reward-Settings.Permission-Groups." + group.getGroupName() + ".Special-Time-periods." + value + ".Time-Limit"));
                    }
                    if (limit == null) {
                        continue;
                    }
                    if (now.compareTo(timePeriod) >= 0 && now.compareTo(limit) <= 0) {
                        return value;
                    }
                    break;
                }
                case BEFORE_THIS_TIME: {
                    SignInDate limit;
                    if (!ConfigurationUtil.getConfig(ConfigurationType.REWARDSETTINGS).contains("Reward-Settings.Permission-Groups." + group.getGroupName() + ".Special-Time-periods." + value + ".Time-Limit")) {
                        limit = SignInDate.getInstanceAsTimePeriod("00:00:00");
                    } else {
                        limit = SignInDate.getInstanceAsTimePeriod(ConfigurationUtil.getConfig(ConfigurationType.REWARDSETTINGS).getString("Reward-Settings.Permission-Groups." + group.getGroupName() + ".Special-Time-periods." + value + ".Time-Limit"));
                    }
                    if (limit == null) {
                        continue;
                    }
                    if (now.compareTo(timePeriod) <= 0 && now.compareTo(limit) >= 0) {
                        return value;
                    }
                    break;
                }
            }
        }
        return null;
    }
    
    public static SignInTimePeriodType getFromName(String name) {
        if (name == null) return null;
        for (SignInTimePeriodType timePeriod : SignInTimePeriodType.values()) {
            if (name.equalsIgnoreCase(timePeriod.name())) {
                return timePeriod;
            }
        }
        return null;
    }
}
